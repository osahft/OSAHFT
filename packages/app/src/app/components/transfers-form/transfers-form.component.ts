import {Component, EventEmitter, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, Validators} from "@angular/forms";
import {TransfersService} from "../../services/transfers/transfers.service";
import {Constants} from "../../shared/constants";
import {Types} from "../../shared/types";
import {ToastService} from "../../services/toast/toast.service";
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-transfers-form',
  templateUrl: './transfers-form.component.html',
  styleUrls: ['./transfers-form.component.scss'],
  exportAs: "transfersForm",
})

export class TransfersFormComponent implements OnInit {
  @Output() hideFormEvent = new EventEmitter<boolean>();
  @Output() receiversEvent = new EventEmitter<string[]>();
  modalReference: any;
  modalCloseResult: any;
  mailTransferId: string = '';

  mailError: string = 'Please provide a valid email address of format abc@domain.com';
  titleError: string = 'Please provide a title';
  tokenError: string = 'Please provide a valid token';
  receiverError = {
    'mailError': this.mailError
  };
  emailPattern = new RegExp(/(?:[a-z0-9!#$%&'*+\/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+\/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/);//NOSONAR
  numericPattern = new RegExp(/^[0-9]{6}$/);
  receiversValidators = [Validators.pattern(this.emailPattern)];

  transfersForm;
  modalForm;
  // @ts-ignore
  @ViewChild('tokenPopup') private tokenPopup: TemplateRef<NgbModal>;

  constructor(private transferService: TransfersService, private toastService: ToastService, private modalService: NgbModal,
              private formBuilder: FormBuilder) {

    this.transfersForm = this.formBuilder.group({
      messageTitle: ['', [Validators.required]],
      senderEmail: ['', [Validators.required, Validators.pattern(this.emailPattern)]],
      messageBody: [''],
      receiverMails: [[] as { label: string }[], Validators.required],
      transferFiles: [[] as File[], Validators.required]
    });

    this.modalForm = this.formBuilder.group({
      token: ['', [Validators.required, Validators.pattern(this.numericPattern)]]
    });
  }

  ngOnInit(): void {
    // intentional empty function body
  }


  /**
   * Initiates file transfer by creating a mail transfer.
   * Upon successful mail transfer creation opens modal for user verification.
   */
  async initTransfer() {
    if (this.transfersForm.invalid) {
      const error = new Error("Cannot start the transfer due to missing input");
      this.showToast(error.message, Constants.ToastTypes.ERROR);
      return;
    }

    const receivers: { label: string }[] = this.receiverMails?.value;
    const requestBody: Types.CreateMailTransferRequest = {
      "mailSender": this.senderEmail?.value,
      "mailReceivers": receivers.map(r => r.label),
      "title": this.messageTitle?.value,
      "message": this.messageBody?.value
    };
    const transferResponse: Types.CreateMailTransferResponse = await this.transferService.createMailTransfer(requestBody);
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
    console.log("Token:", this.token?.value);

    const auth = await this.transferService.authenticateUser(this.mailTransferId, this.token?.value);
    if (!!auth) console.log("Authenticated!", auth);

    const formData = new FormData();
    for (const f of this.transferFiles?.value) {
      formData.append("files", f, f.name);
    }

    let success = await this.transferService.uploadFiles(this.mailTransferId, formData);

    if (!!success) {
      console.log("Files uploaded", success);
      success = await this.transferService.completeMailTransfer(this.mailTransferId);
    }

    if (!!this.modalReference && !!success) {
      this.modalReference.close();
      // clear token input on modal close
      this.token?.reset();
      this.hideFormEmitter(false);
      this.receiversEmitter(this.receiverMails?.value);
    }

  }

  /**
   * Checks whether given control's value is a valid email address).
   * @param control
   */
  private checkPattern(control: AbstractControl) {
    const patternRegex = this.emailPattern;
    if (patternRegex.test(control.value)) {
      console.log("Match exists.");
    } else {
      return {'pattern': true};
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
  public openModal(content: any) {
    this.modalReference = this.modalService.open(content);
    this.modalReference.result.then((result: any) => {
      this.modalCloseResult = `Closed with: ${result}`;
    }, (reason: any) => {
      this.modalCloseResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  /**
   * Function to externally close ng-bootstrap modal.
   */
  public closeModal() {
    this.modalReference.close();
  }

  /**
   * Gets ng-bootstrap modal's dismiss reason to properly handle the modal's promise on close.
   * @param reason
   */
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  /**
   * Emits event to parent component to hide the transfer form.
   * @param showForm
   */
  private hideFormEmitter(showForm: boolean) {
    this.transfersForm.reset();
    this.transferFiles?.setValue([]);
    this.hideFormEvent.emit(showForm);
  }

  /**
   * Emits receivers to parent component to show in success page.
   * @param receivers
   */
  private receiversEmitter(receivers: { label: string }[]) {
    const formattedReceivers = receivers.map(receiver => receiver.label);
    this.receiversEvent.emit(formattedReceivers);
  }

  get token() {
    return this.modalForm.get('token');
  }

  get receiverMails() {
    return this.transfersForm.get('receiverMails');
  }

  get transferFiles() {
    return this.transfersForm.get('transferFiles');
  }

  get messageTitle() {
    return this.transfersForm.get('messageTitle');
  }

  get senderEmail() {
    return this.transfersForm.get('senderEmail');
  }

  get messageBody() {
    return this.transfersForm.get('messageBody');
  }
}
