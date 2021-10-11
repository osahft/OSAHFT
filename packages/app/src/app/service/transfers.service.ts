import {Injectable} from '@angular/core';
import {Types} from "../shared/types";
import {HttpClient, HttpErrorResponse, HttpHeaders, } from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TransfersService {

  // @TODO: dynamic api url; move this to a constants.ts file in /app/shared
  API_URL = 'http://localhost:8080/api';
  TRANSFERS = '/v1/transfers/mails';
  UPLOADS = '/uploads';

  constructor(private http: HttpClient) {
  }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  }

  createMailTransfer(mailTransferRequest: Types.CreateMailTransferRequest): Observable<Types.CreateMailTransferResponse> {
    return this.http.post<Types.CreateMailTransferResponse>(`${this.API_URL}${this.TRANSFERS}`, mailTransferRequest, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      )
  }

  completeMailTransfer(mailTransferId: string): Observable<boolean> {
    const idParam = `/${mailTransferId}`
    return this.http.put<any>(`${this.API_URL}${this.TRANSFERS}${idParam}`, {})
      .pipe(
        catchError(this.handleError)
      )
  }

  uploadFiles(mailTransferId: string, files: FormData): Observable<boolean> {
    const idParam = `/${mailTransferId}`
    return this.http.post<any>(`${this.API_URL}${this.TRANSFERS}${idParam}${this.UPLOADS}`, files)
  }

  handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    window.alert(errorMessage);
    return throwError(errorMessage);
  }

}
