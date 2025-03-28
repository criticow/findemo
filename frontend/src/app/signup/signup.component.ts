import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpService } from '../common/http/http.service';
import { catchError, of, tap } from 'rxjs';
import { ToastService } from '../common/toast/toast.service';
import { MatchPasswordDirective } from '../common/match-password/match-password.directive';
import { SpinnerIconComponent } from "../common/icons/spinner.icon";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  standalone: true,
  imports: [FormsModule, RouterLink, MatchPasswordDirective, SpinnerIconComponent]
})
export class SignupComponent {
  public email: string;
  public confirmPassword: string;
  public password: string;

  private httpService: HttpService;
  private toastService: ToastService;
  private router: Router;
  public loading: boolean;

  constructor(){
    this.httpService = inject(HttpService);
    this.toastService = inject(ToastService);
    this.router = inject(Router);
    this.email = '';
    this.confirmPassword = '';
    this.password = '';
    this.loading = false;
  }

  public validateMatchPasswords(form: NgForm) {
    form.controls['confirmPassword'].updateValueAndValidity();
  }

  onSubmit() {
    this.loading = true;

    this.httpService.post("/auth/signup", {
      email: this.email,
      password: this.password
    }).pipe(
      tap((response: any) => {
        this.toastService.showMessage({
          text: response.message,
          type: 'success'
        })

        this.loading = false;
        this.router.navigate(['/dashboard']);
      }),
      catchError((error) => {
        this.toastService.showMessage({
          text: error.error.message,
          type: 'error'
        })

        this.loading = false;
        return of(null);
      })
    )
    .subscribe();

  }
}