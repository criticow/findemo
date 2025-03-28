import { Component } from "@angular/core";
import { BaseIcon } from "./base.icon";

@Component({
  selector: 'minus-icon',
  template: `
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="currentColor"
    viewBox="0 0 16 16"
  >
    <path d="M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8"/>
  </svg>
  `,
  standalone: true
})
export class MinusIconComponent extends BaseIcon {}