import {Component} from '@angular/core';
import {AbstractControl} from "@angular/forms";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  mailTitle: string = '';
  messageBody: string = '';
  senderAddress: string[] = [];
  receiverAddresses: string[] = [];
  files: File[] = [];

  isPrivacySelected: boolean = false;
  isTitleValid: boolean = false;

  errorMessages = {
    'pattern': 'Email must be in format abc@abc.com',
  };
  validators = [this.checkPattern];

  emailPattern: string = "^[A-Za-z0–9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$";

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

  transfer() {
    console.log(this.files)
    console.log(this.mailTitle)
    console.log(this.messageBody)
    console.log(this.senderAddress)
    console.log(this.receiverAddresses)
  }

}
