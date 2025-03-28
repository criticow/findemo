import { Component, HostBinding } from "@angular/core";

@Component({
  selector: 'app-transaction-skeleton',
  template: `
  <div class="grow whitespace-nowrap animate-pulse">
    <div class="h-2.5 bg-gray-300 rounded-full w-48 mb-1"></div>
    <div class="h-2 w-32 bg-gray-300 rounded-full my-2"></div>
    <div class="h-2 w-36 bg-gray-300 rounded-full my-2"></div>
  </div>
  <div class="animate-pulse h-3 w-28 bg-gray-300 rounded-full"></div>
  `,
  standalone: true
})
export class TransactionSkeletonComponent {
  @HostBinding('class')
  private _class = "bg-white p-2 flex items-center gap-2 w-full rounded-md shadow-md";
}