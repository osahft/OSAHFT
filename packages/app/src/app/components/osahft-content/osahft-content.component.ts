import { Component, OnInit } from '@angular/core';

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
   * Hides success page and shows form
   */
  toggleForm() {
    this.show = !this.show;
  }

  /**
   * Gets event from transfer form component whether to show/hide success component.
   * @param flag
   */
  getToggleFormEvent(flag: boolean) {
    this.show = flag;
  }

  /**
   * Gets receivers from transfer component to show in success page.
   * @param receivers
   */
  getReceivers(receivers: string[]) {
    this.receivers = receivers;
  }
}
