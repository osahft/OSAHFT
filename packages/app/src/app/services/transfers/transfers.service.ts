import {Injectable, Injector} from '@angular/core';
import {Constants} from "../../shared/constants";
import {Types} from "../../shared/types";
import {HttpClient, HttpErrorResponse, HttpHeaders,} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ToastService} from "../toast/toast.service";

@Injectable()
export class TransfersService {

  constructor(private http: HttpClient, private injector: Injector) {

  }

  /**
   * Calls endpoint to create a pending MailTransfer by passing form data.
   * @param mailTransferRequest
   */
  createMailTransfer(mailTransferRequest: Types.CreateMailTransferRequest): Observable<Types.CreateMailTransferResponse> {
    return this.http.post<Types.CreateMailTransferResponse>(`${Constants.Paths.API_URL}${Constants.Paths.TRANSFERS}`, mailTransferRequest, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    })
      .pipe(
        catchError(this.handleError)
      )
  }

  /**
   * Calls endpoint to complete a pending MailTransfer thereby closing it by passing a valid `mailTransferId`.
   * @param mailTransferId
   */
  completeMailTransfer(mailTransferId: string): Observable<any> {
    const idParam = `/${mailTransferId}`;
    return this.http.put<any>(`${Constants.Paths.API_URL}${Constants.Paths.TRANSFERS}${idParam}`, {},
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response'
      }
    )
      .pipe(
        catchError(this.handleError)
      )
  }

  /**
   * Calls endpoint to upload files for a given pending MailTransfer.
   * @param mailTransferId
   * @param files
   */
  uploadFiles(mailTransferId: string, files: FormData): Observable<any> {
    const idParam = `/${mailTransferId}`;
    return this.http.post<any>(`${Constants.Paths.API_URL}${Constants.Paths.TRANSFERS}${idParam}${Constants.Paths.UPLOADS}`, files, {observe: 'response'})
      .pipe(
        catchError(this.handleError)
      )
  }

  /**
   * Calls endpoint to authenticate sender by checking if a given MailTransfer (via `mailTransferId`) has a valid `authToken`.
   * @param mailTransferId
   * @param authToken
   */
  authenticateUser(mailTransferId: string, authToken: string): Observable<any> {
    const path = `/${mailTransferId}${Constants.Paths.AUTH}/${authToken}`;
    return this.http.post<any>(`${Constants.Paths.API_URL}${Constants.Paths.TRANSFERS}${path}`, {}, {observe: 'response'})
      .pipe(
        catchError(this.handleError)
      )
  }

  /**
   * Handles client and server side errors of HttpClient requests.
   * @param error
   */
  private handleError(error: HttpErrorResponse) {
    const toastService = this.injector.get(ToastService);
    console.log(toastService);

    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    //@TODO: pass error from here to toast service
    console.log(errorMessage);

    toastService.show(errorMessage, {
      classname: 'bg-danger text-light',
      delay: 7500,
      autohide: true
    });
    return throwError(errorMessage);
  }

}
