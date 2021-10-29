import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {AbstractControl} from "@angular/forms";
import {TransfersService} from "../../services/transfers/transfers.service";
import {Constants} from "../../shared/constants";
import {Types} from "../../shared/types";
import {ToastService} from "../../services/toast/toast.service";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-transfers-form',
  templateUrl: './transfers-form.component.html',
  styleUrls: ['./transfers-form.component.scss'],
  exportAs: "transfersForm",
})

export class TransfersFormComponent implements OnInit {
  mailTitle: string = '';
  messageBody: string = '';
  senderAddress: string = '';
  receiverAddresses: { label: string }[] = [];
  files: File[] = [];
  modalReference: any;
  token: string = '';
  mailTransferId: string = '';

  isTitleValid: boolean = false;

  errorMessages = {
    'pattern': 'Please provide a valid email address of format abc@domain.com',
  };
  validators = [this.checkPattern];

  emailPattern: string = "^[A-Za-z0–9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$";


  // @ts-ignore
  @ViewChild('tokenPopup') private tokenPopup: TemplateRef<NgbModal>;

  constructor(
    private transferService: TransfersService,
    private toastService: ToastService,
    private modalService: NgbModal
  ) {
    // intentional empty function body
  }

  ngOnInit(): void {
    // intentional empty function body
  }


  /**
   * Initiates file transfer by creating a mail transfer.
   * Upon successful mail transfer creation opens modal for user verification.
   */
  async initTransfer() {
    if (!this.isInputValid()) {
      const error = new Error("Cannot start the transfer due to missing input")
      this.showToast(error.message, Constants.ToastTypes.ERROR)
      return;
    }

    // console.log(this.files)
    // console.log(this.mailTitle)
    // console.log(this.messageBody)
    // console.log(this.senderAddress)
    // console.log(this.receiverAddresses)

    const requestBody: Types.CreateMailTransferRequest = {
      "mailSender": this.senderAddress,
      "mailReceivers": this.receiverAddresses.map(receivers => receivers.label),
      "title": this.mailTitle,
      "message": this.messageBody
    }

    const transferResponse: Types.CreateMailTransferResponse = await this.transferService.createMailTransfer(requestBody).toPromise();
    if (!!transferResponse) {
      console.log("Mail Transfer created", transferResponse);
      this.mailTransferId = transferResponse.mailTransferId;
    }

    this.openModal(this.tokenPopup);
  }

  /**
   * Verifies sender address via token.
   * Upon successful verification, initiates actual file transfer to API.
   * If file transfer is successful, completes mail transfer.
   */
  async verifyAndExecuteTransfer() {
    console.log("Token:", this.token);

    const auth = await this.transferService.authenticateUser(this.mailTransferId, this.token).toPromise();
    if (!!auth) console.log("Authenticated!", auth);

    const formData = new FormData();
    for (const f of this.files) {
      formData.append("files", f, f.name);
    }

    let success = await this.transferService.uploadFiles(this.mailTransferId, formData).toPromise();

    if (!!success) {
      console.log("Files uploaded", success);
      success = await this.transferService.completeMailTransfer(this.mailTransferId).toPromise();
    }

    if (!!success) {
      console.log("Mail Transfer completed", success);
      this.showToast("Files sent successfully!", Constants.ToastTypes.SUCCESS);
    }

    if (!!this.modalReference && !!success) {
      this.modalReference.close();
      // clear token input on modal close
      this.token = '';
    }

  }

  /**
   * Checks whether form inputs title, receiver address, sender address and files are valid = given.
   */
  private isInputValid() {
    return this.isTitleValid && this.receiverAddresses.length > 0 && this.senderAddress.length > 0 && this.files.length > 0;
  }

  /**
   * Checks wether given control's value is a valid email address).
   * @param control
   */
  private checkPattern(control: AbstractControl) {
    const patternRegex = /^[A-Za-z0–9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$/;
    if (patternRegex.test(control.value)) {
      console.log("Match exists.");
    } else {
      return {'pattern': true}
    }
    return null;
  }

  /**
   * Shows error message toast with given message.
   * @param message
   * @param type
   */
  private showToast(message: string, type: string) {
    this.toastService.show(message, {
      classname: `${type} text-light`,
      delay: 7500,
      autohide: true
    });
  }

  /**
   * Opens modal by given modal reference.
   * @param content
   */
  private openModal(content: any) {
    this.modalReference = this.modalService.open(content);
    this.modalReference.result.then((result: any) => {
      console.log(`Closed with: ${result}`)
    })
  }

}
