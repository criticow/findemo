import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpService } from "../common/http/http.service";
import { catchError, of, tap } from "rxjs";
import { ToastService } from "../common/toast/toast.service";
import { Transaction, TransactionComponent } from "../transactions/transaction/transaction.component";
import { MenuComponent } from "../menu/menu.component";

type Month = { name: string, value: number };
type YearsAndMonths = {
  [key: number]: Month[]
};

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, TransactionComponent, MenuComponent]
})
export class DashboardComponent {
  private httpService: HttpService;
  private toastService: ToastService;

  public yearsAndMonths: YearsAndMonths;

  public years: number[];
  public selectedYear: number;
  public availableMonths: Month[];
  public selectedMonth: number;

  public transactions: Transaction[];

  public tab: number;

  constructor() {
    this.httpService = inject(HttpService);
    this.toastService = inject(ToastService);

    this.yearsAndMonths = {};
    this.years = [];
    const now = new Date();
    this.selectedYear = now.getFullYear();
    this.selectedMonth = now.getMonth() - 1;
    this.availableMonths = [];
    this.transactions = [];
    this.tab = 1;
  }

  public ngOnInit() {
    this.getYearsAndMonths();
  }

  public getMonthlyTransactions() {
    const params = {
      month: String(this.selectedMonth),
      year: String(this.selectedYear)
    };

    this.httpService.get<Transaction[]>("/transactions/monthly", params)
      .pipe(
        tap((response) => {
          this.transactions = [];

          if(response.data) {
            this.transactions = response.data;
          }
        }),
        catchError((error) => {
          console.log(error)
          this.toastService.showMessage({
            text: error?.error?.message || "Erro inesperado ao buscar transações recentes",
            type: 'error'
          });

          return of(null);
        })
      )
      .subscribe();
  }

  private getMonthName(month: number) {
    const date = new Date();
    date.setDate(1);
    date.setMonth(month - 1);
    const monthName = date.toLocaleString('pt-BR', {
      month: 'long'
    });
    return monthName[0].toUpperCase() + monthName.substring(1);
  }

  private getYearsAndMonths() {
    this.httpService.get<{[key: number]: number[]}>("/transactions/grouped-by-year")
      .pipe(
        tap((response) => {
          let data: {[key: number]: number[]} = {};

          if(response.data) {
            data = response.data;
          };

          const currentMonth = new Date().getMonth() + 1;
          const currentYear = new Date().getFullYear();

          if(Object.keys(data).map((item) => Number(item)).includes(currentYear)) {
            if(!data[currentYear].includes(currentMonth)) {
              data[currentYear].push(currentMonth);
            }
          } else {
            data[currentYear] = [currentMonth];
          }

          this.yearsAndMonths = Object.entries(data).reduce((acc, [key, item]) => {
            acc[Number(key)] = item.map((month) => {
              return {
                name: this.getMonthName(month),
                value: month
              }
            });

            return acc;
          }, {} as YearsAndMonths);

          this.years = Object.keys(this.yearsAndMonths).map((year) => Number(year));
          this.onYearChange();
        }),
        catchError((error) => {
          console.log(error)
          this.toastService.showMessage({
            text: error?.error?.message || "Erro inesperado ao buscar transações recentes",
            type: 'error'
          });

          return of(null);
        })
      )
      .subscribe();
  }

  get totals() {
    return this.transactions.reduce(
      (acc, entry) => {
        if (entry.amount > 0) {
          acc.positiveSum += entry.amount;
          acc.positiveCount++;
        } else if (entry.amount < 0) {
          acc.negativeSum += entry.amount;
          acc.negativeCount++;
        }

        acc.totalSum += entry.amount;
        acc.totalCount++;

        return acc;
      },
      {
        positiveSum: 0,
        positiveCount: 0,
        negativeSum: 0,
        negativeCount: 0,
        totalSum: 0,
        totalCount: 0
      }
    );
  }

  public onYearChange() {
    this.availableMonths = this.yearsAndMonths[this.selectedYear];
    this.selectedMonth = this.availableMonths[this.availableMonths.length -1].value;

    this.getMonthlyTransactions();
  }
}