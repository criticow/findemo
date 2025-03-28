import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { catchError, of, tap } from 'rxjs';
import { ToastService } from '../common/toast/toast.service';
import { HttpService } from '../common/http/http.service';
import { SpinnerIconComponent } from "../common/icons/spinner.icon";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  standalone: true,
  imports: [FormsModule, RouterLink, SpinnerIconComponent]
})
export class SigninComponent {
  public email: string;
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
    this.password = '';
    this.loading = false;
  }

  onSubmit() {
    this.loading = true;

    this.httpService.post("/auth/signin", {
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