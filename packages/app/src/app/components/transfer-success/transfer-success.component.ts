import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-transfer-success',
  templateUrl: './transfer-success.component.html',
  styleUrls: ['./transfer-success.component.scss']
})
export class TransferSuccessComponent implements OnInit {
  @Input() receivers: string[] = [];
  @Output() showFormEvent = new EventEmitter<boolean>();

  constructor() {
    // intentional empty function body
  }

  ngOnInit(): void {
    // intentional empty function body
  }

  /**
   * Hides success page and shows form
   */
  toggleForm() {
    this.showFormEmitter(true)
  }

  /**
   * Emits event to parent component to hide the transfer form.
   * @param showForm
   */
  private showFormEmitter(showForm: boolean) {
    this.showFormEvent.emit(showForm);
  }
}
