import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, map, of, tap, throwError } from "rxjs";
import { environment } from "../../../environments/environment";
import { Router } from "@angular/router";

interface CustomResponse<T> {
  timestamp: string;
  data: T;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  private httpClient: HttpClient;
  private router: Router;

  constructor() {
    this.httpClient = inject(HttpClient);
    this.router = inject(Router);
  }

  public post<T, R>(path: string, body: T, params?: {[key: string]: string}) {
    const url = environment.API_URL + path;

    return this.httpClient.post<CustomResponse<R>>(
      url,
      body,
      {
        headers: this.getHeaders(),
        observe: 'response',
        params
      }
    ).pipe(
      map((response) => {
        if(response.headers.has("token")) {
          localStorage.setItem("token", response.headers.get("token") || '');
        }

        return response.body;
      }),
      catchError((error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['signin']);
        }

        return throwError(() => error);
      })
    );
  }

  public get<T>(path: string, params?: {[key: string]: string}) {
    const url = environment.API_URL + path;

    return this.httpClient.get<CustomResponse<T>>(
      url,
      {
        headers: this.getHeaders(),
        params
      }
    ).pipe(
      catchError((error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['signin']);
        }

        return throwError(() => error);
      })
    );
  }

  private getHeaders() {
    const token = localStorage.getItem("token");

    const headers = token
      ? new HttpHeaders().set("Authorization", `Bearer ${token}`)
      : new HttpHeaders();

    return headers;
  }
}
