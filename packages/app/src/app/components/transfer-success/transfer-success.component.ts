import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Types} from "../../shared/types";

@Component({
  selector: 'app-transfer-success',
  templateUrl: './transfer-success.component.html',
  styleUrls: ['./transfer-success.component.scss']
})
export class TransferSuccessComponent implements OnInit {
  @Input() receivers: string[] = [];
  @Output() showFormEvent = new EventEmitter<Types.IFormToggleEvent>();

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
    const toggleEvent: Types.IFormToggleEvent = {
      flag: showForm,
      receivers: []
    }
    this.showFormEvent.emit(toggleEvent);
  }
}
