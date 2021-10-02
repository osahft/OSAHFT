import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  isPrivacySelected: boolean = false;
  files: File[] = [];

  onSelect(event: any) {
    console.log(event);
    this.files.push(...event.addedFiles);
  }

  onRemove(event: any) {
    console.log(event);
    this.files.splice(this.files.indexOf(event), 1);
  }

  isInputValid() {
    return !this.isPrivacySelected;
  }

  emailPattern: string = "^[a-zA-Z0-9._% -]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

  mailTitle: string = '';
  messageBody: string = '';
  senderAddress: string = '';
  receiverAddress: string = '';
}
