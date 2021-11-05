import { Component, OnInit } from '@angular/core';
import {Types} from "../../shared/types";

@Component({
  selector: 'app-osahft-content',
  templateUrl: './osahft-content.component.html',
  styleUrls: ['./osahft-content.component.scss']
})
export class OsahftContentComponent implements OnInit {
  show: boolean = true;
  receivers: string[] = [];

  constructor() {
    // intentional empty function body
  }

  ngOnInit(): void {
    // intentional empty function body
  }

  /**
   * Gets event from transfer form component whether to show/hide success component.
   * @param toggleEvent
   */
  getToggleFormEvent(toggleEvent: Types.FormToggleEvent) {
    this.show = toggleEvent.flag;
    this.receivers = toggleEvent.receivers;
  }
}
