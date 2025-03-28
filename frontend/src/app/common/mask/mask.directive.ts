import {
  Directive,
  ElementRef,
  HostListener,
  Input,
  OnInit,
  forwardRef
} from '@angular/core';
import {
  NG_VALUE_ACCESSOR,
  ControlValueAccessor,
  NG_VALIDATORS,
  Validator,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { dateProcessor } from './processors/date.processor';
import { amountProcessor } from './processors/amount.processor';

export interface MaskProcessor {
  format: (value: any) => any;
  convert: (value: any) => any;
  init: () => any;
  validate: (value: any) => ValidationErrors | null;
}

const processors = {
  date: dateProcessor,
  amount: amountProcessor,
};

type MaskType = keyof typeof processors; // 'date' | 'amount'

@Directive({
  selector: '[mask]',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => MaskDirective),
      multi: true,
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => MaskDirective),
      multi: true,
    }
  ],
})
export class MaskDirective implements OnInit, ControlValueAccessor, Validator {
  @Input({ required: true }) mask!: MaskType;
  @Input() selectOnFocus: boolean;
  private processor!: typeof processors[MaskType];

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  constructor(private el: ElementRef) {
    this.selectOnFocus = true;
  }

  ngOnInit(): void {
    this.processor = processors[this.mask];
    this.el.nativeElement.value = this.processor.init();
  }

  @HostListener('input', ['$event'])
  onInput(event: Event): void {
    let input = (event.target as HTMLInputElement).value;

    const value = this.processor.format(input);
    const converted = this.processor.convert(value);

    this.onChange(converted);
    this.el.nativeElement.value = value;
  }

  @HostListener('focus')
  onFocus(): void {
    if(!this.selectOnFocus) return;

    const input = this.el.nativeElement as HTMLInputElement;

    setTimeout(() => {
      input.setSelectionRange(0, input.value.length);
    }, 1);
  }

  writeValue(value: string): void {
    if(value === undefined || value === null) return;

    const formatted = this.processor.format(value);
    const converted = this.processor.convert(value);

    this.onChange(converted);
    this.el.nativeElement.value = formatted;
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  validate(control: AbstractControl): ValidationErrors | null {
    return this.processor.validate(control.value);
  }
}
