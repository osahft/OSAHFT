export namespace Types {

  export interface CreateMailTransferRequest {
    mailSender: string;
    mailReceivers: Array<string>;
    title: string;
    message: string;
  }

  export interface CreateMailTransferResponse {
    mailTransferId: string;
  }

  export interface UploadFileRequest {
    files: FormData;
  }

}
