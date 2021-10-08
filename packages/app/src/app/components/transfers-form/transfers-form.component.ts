import { Component, OnInit } from '@angular/core';
import {AbstractControl} from "@angular/forms";
import {TransfersService} from "../../service/transfers.service";
import {Types} from "../../shared/types";

@Component({
  selector: 'app-transfers-form',
  templateUrl: './transfers-form.component.html',
  styleUrls: ['./transfers-form.component.css'],
  exportAs: "transfersForm",
})

export class TransfersFormComponent implements OnInit {
  mailTitle: string = '';
  messageBody: string = '';
  senderAddress: string = '';
  receiverAddresses: string[] = [];
  files: File[] = [];

  isPrivacySelected: boolean = false;
  isTitleValid: boolean = false;

  errorMessages = {
    'pattern': 'Email must be in format abc@abc.com',
  };
  validators = [this.checkPattern];

  emailPattern: string = "^[A-Za-z0–9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$";

  constructor(public transferService: TransfersService) { }

  ngOnInit(): void {
  }


  onSelect(event: any) {
    console.log(event);
    this.files.push(...event.addedFiles);
  }

  onRemove(event: any) {
    console.log(event);
    this.files.splice(this.files.indexOf(event), 1);
  }

  isInputValid() {
    return !(this.isPrivacySelected && this.isTitleValid && this.receiverAddresses.length > 0 && this.senderAddress.length > 0 && this.files.length > 0);
  }

  checkPattern(control: AbstractControl) {
    const patternRegex = /^[A-Za-z0–9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$/;
    if (patternRegex.test(control.value)) {
      console.log("Match exists.");
    } else {
      return {'pattern': true}
    }
    return null;
  }

  async transfer() {
    console.log(this.files)
    console.log(this.mailTitle)
    console.log(this.messageBody)
    console.log(this.senderAddress)
    console.log(this.receiverAddresses)

    const requestBody: Types.CreateMailTransferRequest = {
      "mailSender": this.senderAddress,
      "mailReceivers": this.receiverAddresses,
      "title": this.mailTitle,
      "message": this.messageBody
    }

    const fileUpload: Types.UploadFileRequest = {
      files: this.files
    }

    const transferResponse: Types.CreateMailTransferResponse = await this.transferService.createMailTransfer(requestBody).toPromise();
    let success = await this.transferService.uploadFiles(transferResponse.mailTransferId, fileUpload).toPromise();

    if(!!success) {
      success = this.transferService.completeMailTransfer(transferResponse.mailTransferId).toPromise();
    }

    // @TODO: do some fancy stuff on success, for now just log
    console.log(success);
  }

}
