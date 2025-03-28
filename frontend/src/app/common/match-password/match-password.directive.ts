import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, Validator, AbstractControl, ValidationErrors } from '@angular/forms';

@Directive({
  selector: '[matchPassword]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: MatchPasswordDirective,
      multi: true
    }
  ]
})
export class MatchPasswordDirective implements Validator {
  @Input({required: true}) matchPassword!: string;

  validate(control: AbstractControl): ValidationErrors | null {
    if (!this.matchPassword) {
      return null;
    }

    const value = control.value;

    return value === this.matchPassword ? null : { passwordsMismatch: true };
  }
}
