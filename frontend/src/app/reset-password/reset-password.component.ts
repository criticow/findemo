import { Component, inject, OnInit } from "@angular/core";
import { FormsModule, NgForm } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { HttpService } from "../common/http/http.service";
import { ToastService } from "../common/toast/toast.service";
import { catchError, of, tap } from "rxjs";
import { MatchPasswordDirective } from "../common/match-password/match-password.directive";
import { SpinnerIconComponent } from "../common/icons/spinner.icon";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  standalone: true,
  imports: [
    FormsModule,
    MatchPasswordDirective,
    SpinnerIconComponent,
    RouterLink
  ]
})
export class ResetPasswordComponent implements OnInit {
  private httpService: HttpService;
  private toastService: ToastService;

  public token: string;

  public password: string;
  public confirmPassword: string;

  public loading: boolean;
  private route: ActivatedRoute;
  private router: Router;

  constructor() {
    this.httpService = inject(HttpService);
    this.toastService = inject(ToastService);
    this.token = '';
    this.password = '';
    this.confirmPassword = '';
    this.loading = false;
    this.route = inject(ActivatedRoute);
    this.router = inject(Router);
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if(!params["token"]) {
        this.router.navigate(['signin']);
        return;
      }

      this.token = params["token"];
    });
  }

  public validateMatchPasswords(form: NgForm) {
    form.controls['confirmPassword'].updateValueAndValidity();
  }

  onSubmit() {
    if(!this.token) return;

    this.loading = true;

    this.httpService.post("/auth/reset-password", {
      token: this.token,
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