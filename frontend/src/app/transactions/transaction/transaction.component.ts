import { CommonModule } from "@angular/common";
import { Component, HostBinding, Input } from "@angular/core";

export interface Transaction {
  id?: number;
  description: string;
  createdAt?: string;
  date: string;
  amount: number;
}

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  standalone: true,
  imports: [CommonModule]
})
export class TransactionComponent {
  @Input({required: true}) transaction!: Transaction;
  @HostBinding('class')
  private _class = "bg-white p-2 flex items-center gap-2 w-full rounded-md shadow-md";
}