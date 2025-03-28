import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { HttpService } from "../common/http/http.service";
import { ToastService } from "../common/toast/toast.service";
import { catchError, of, tap } from "rxjs";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  standalone: true,
  imports: [FormsModule, RouterLink]
})
export class ForgotPasswordComponent {
  public email: string;

  private httpService: HttpService;
  private toastService: ToastService;

  public loading: boolean;
  public emailSent: boolean;

  constructor() {
    this.httpService = inject(HttpService);
    this.toastService = inject(ToastService);
    this.email = '';
    this.loading = false;
    this.emailSent = false;
  }

  onSubmit() {
    this.loading = true;

    this.httpService.post("/auth/forgot-password", {
      email: this.email,
    }).pipe(
      tap((response: any) => {
        this.toastService.showMessage({
          text: response.message,
          type: 'success'
        })

        this.loading = false;
        this.emailSent = true;
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