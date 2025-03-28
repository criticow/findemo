import { Component, inject, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, NgForm } from "@angular/forms";
import { catchError, of, tap } from "rxjs";
import { ToastService } from "../common/toast/toast.service";
import { HttpService } from "../common/http/http.service";
import { MaskDirective } from "../common/mask/mask.directive";
import { SpinnerIconComponent } from "../common/icons/spinner.icon";
import { MinusIconComponent } from "../common/icons/minus.icon";
import { PlusIconComponent } from "../common/icons/plus.icon";
import { XIconComponent } from "../common/icons/x.icon";
import { dateProcessor } from "../common/mask/processors/date.processor";
import { TransactionSkeletonComponent } from "./transaction/transaction-skeleton.component";
import { Transaction, TransactionComponent } from "./transaction/transaction.component";
import { MenuComponent } from "../menu/menu.component";

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MaskDirective,
    SpinnerIconComponent,
    MinusIconComponent,
    PlusIconComponent,
    XIconComponent,
    TransactionSkeletonComponent,
    TransactionComponent,
    MenuComponent
]
})
export class TransactionsComponent implements OnInit {
  public transactions: Transaction[];
  public transaction: Transaction;
  private httpService: HttpService;
  private toastService: ToastService;
  public submiting: boolean;
  public loadingTransactions: boolean;
  public signed: boolean;

  constructor(){
    this.httpService = inject(HttpService);
    this.toastService = inject(ToastService);
    this.transaction = {
      date: dateProcessor.init()
    } as Transaction;
    this.transactions = [];
    this.loadingTransactions = true;
    this.submiting = false;
    this.signed = false;
  }

  ngOnInit(): void {
    this.getTransactions();
  }

  private resetForm(){
    this.transaction = {
      description: '',
      amount: 0,
      date: this.transaction.date
    };
  }

  public onSubmit(form: NgForm){
    if(form.invalid) return;

    this.submiting = true;

    const amount = this.transaction.amount * (this.signed ? -1 : 1);

    this.httpService
      .post<Transaction, Transaction>("/transactions", {
        ...this.transaction,
        amount
      })
      .pipe(
        tap((response) => {
          if(!response || !response.data) return;

          this.transactions.unshift(response.data);

          this.submiting = false;

          this.toastService.showMessage({
            text: response?.message || "Lançamento salvo com sucesso!",
            type: 'success'
          });

          this.resetForm();
        }),
        catchError((error) => {
          this.toastService.showMessage({
            text: error?.error?.message || "Erro inesperado ao gravar transação",
            type: 'error'
          });

          this.submiting = false;

          return of(null);
        })
      )
      .subscribe();
  }

  public getTransactions() {
    const date = new Date();

    this.httpService
      .get<Transaction[]>("/transactions", { createdAt: date.toISOString().split("T")[0] })
      .pipe(
        tap((response) => {
          this.loadingTransactions = false;

          if(!response.data) return;

          this.transactions = [...this.transactions, ...response.data]
        }),
        catchError((error) => {
          debugger
          this.toastService.showMessage({
            text: error?.error?.message || "Erro inesperado ao buscar transações recentes",
            type: 'error'
          });

          this.loadingTransactions = false;

          return of(null);
        })
      )
      .subscribe();
  }
}