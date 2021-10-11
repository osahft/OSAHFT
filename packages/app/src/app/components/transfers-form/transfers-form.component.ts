import { Component, OnInit } from '@angular/core';
import {AbstractControl} from "@angular/forms";
import {TransfersService} from "../../service/transfers.service";
import {Types} from "../../shared/types";

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
  receiverAddresses: {label: string}[] = [];
  files: File[] = [];

  isPrivacySelected: boolean = false;
  isTitleValid: boolean = false;

  errorMessages = {
    'pattern': 'Please provide a valid email address of format abc@domain.com',
  };
  validators = [this.checkPattern];

  emailPattern: string = "^[A-Za-z0–9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$";

  constructor(public transferService: TransfersService) { }

  ngOnInit(): void {
  }


  onSelect(event: any) {
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
      "mailReceivers": this.receiverAddresses.map(receivers => receivers.label),
      "title": this.mailTitle,
      "message": this.messageBody
    }

    const formData = new FormData();
    for (const f of this.files) {
      formData.append("files", f, f.name);
    }

    //@TODO: error handling for service
    const transferResponse: Types.CreateMailTransferResponse = await this.transferService.createMailTransfer(requestBody).toPromise();
    if(!!transferResponse) console.log("Mail Transfer created");
    let success = await this.transferService.uploadFiles(transferResponse.mailTransferId, formData).toPromise();

    if(success) {
      console.log("Files uploaded", success);
      success = await this.transferService.completeMailTransfer(transferResponse.mailTransferId).toPromise();
    }

    // @TODO: do some fancy stuff on success, for now just log
    console.log("Mail Transfer completed", success);
  }

}
