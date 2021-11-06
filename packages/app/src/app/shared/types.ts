export namespace Types {

  export interface ICreateMailTransferRequest {
    mailSender: string;
    mailReceivers: Array<string>;
    title: string;
    message: string;
  }

  export interface ICreateMailTransferResponse {
    mailTransferId: string;
  }

  export interface IUploadFileRequest {
    files: FormData;
  }

  export interface IFormToggleEvent {
    flag: boolean,
    receivers: string[]
  }

  export interface IApiError {
    code: string
    message: string
  }
}
