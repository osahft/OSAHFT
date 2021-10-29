import {Injectable, TemplateRef} from '@angular/core';

@Injectable()
export class ToastService {
  toasts: any[] = [];

  /**
   * Shows message toast with given text.
   * @param textOrTpl
   * @param options
   */
  show(textOrTpl: string | TemplateRef<any>, options: any = {}) {
    this.toasts.push({textOrTpl, ...options});
  }

  /**
   * Removes passed message toast from toast instance.
   * @param toast
   */
  remove(toast: any) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }
}
