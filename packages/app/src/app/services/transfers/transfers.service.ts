import {Injectable, isDevMode} from '@angular/core';
import {Constants} from "../../shared/constants";
import {Types} from "../../shared/types";
import {HttpClient, HttpErrorResponse, HttpHeaders,} from '@angular/common/http';
import {throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {ToastService} from "../toast/toast.service";

@Injectable()
export class TransfersService {

  BASE_URL: string = Constants.Paths.BASEURL_LOCAL;

  constructor(private http: HttpClient, private toastService: ToastService) {
    // intentional empty function body
    if(!isDevMode()) {
      this.BASE_URL = Constants.Paths.BASEURL_SERVER
    }
  }

  /**
   * Calls endpoint to create a pending MailTransfer by passing form data.
   * @param mailTransferRequest
   */
  createMailTransfer(mailTransferRequest: Types.ICreateMailTransferRequest): Promise<Types.ICreateMailTransferResponse> {
    console.log(this.BASE_URL);
    return this.http.post<Types.ICreateMailTransferResponse>(`${this.BASE_URL}${Constants.Paths.API}${Constants.Paths.TRANSFERS}`, mailTransferRequest, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    })
      .pipe(
        catchError(this.handleError.bind(this))
      ).toPromise()
  }

  /**
   * Calls endpoint to complete a pending MailTransfer thereby closing it by passing a valid `mailTransferId`.
   * @param mailTransferId
   */
  completeMailTransfer(mailTransferId: string): Promise<any> {
    const idParam = `/${mailTransferId}`;
    return this.http.put<any>(`${this.BASE_URL}${Constants.Paths.API}${Constants.Paths.TRANSFERS}${idParam}`, {},
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response'
      }
    )
      .pipe(
        catchError(this.handleError.bind(this))
      ).toPromise()
  }

  /**
   * Calls endpoint to upload files for a given pending MailTransfer.
   * @param mailTransferId
   * @param files
   */
  uploadFiles(mailTransferId: string, files: FormData): Promise<any> {
    const idParam = `/${mailTransferId}`;
    return this.http.post<any>(`${this.BASE_URL}${Constants.Paths.API}${Constants.Paths.TRANSFERS}${idParam}${Constants.Paths.UPLOADS}`, files, {observe: 'response'})
      .pipe(
        catchError(this.handleError.bind(this))
      ).toPromise()
  }

  /**
   * Calls endpoint to authenticate sender by checking if a given MailTransfer (via `mailTransferId`) has a valid `authToken`.
   * @param mailTransferId
   * @param authToken
   */
  authenticateUser(mailTransferId: string, authToken: string): Promise<any> {
    const path = `/${mailTransferId}${Constants.Paths.AUTH}/${authToken}`;
    return this.http.post<any>(`${this.BASE_URL}${Constants.Paths.API}${Constants.Paths.TRANSFERS}${path}`, {}, {observe: 'response'})
      .pipe(
        catchError(this.handleError.bind(this))
      ).toPromise()
  }

  /**
   * Handles client and server side errors of HttpClient requests.
   * @param error
   */
  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (this.isApiError(error.error)) {
      // Get custom error if present
      errorMessage = `Error ${error.error.code}: ${error.error.message}`;
    } else {
      // Use HttpStatusCode and HttpErrorResponse.message instead
      errorMessage = `Error ${error.status}: ${error.message}`;
    }

    this.toastService.show(errorMessage, {
      classname: 'bg-danger text-light',
      delay: 7500,
      autohide: true
    });
    return throwError(errorMessage);
  }

  /**
   * Checks whether passed object is of type `IApiError`.
   * @param object
   */
  private isApiError(object: any): object is Types.IApiError {
    return object;
  }

}
