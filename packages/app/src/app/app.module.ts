import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TagInputModule} from 'ngx-chips';
import {AngularEditorModule} from '@kolkov/angular-editor';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import { FileUploadModule } from '@iplab/ngx-file-upload';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
// components
import {AppComponent} from './app.component';
import {TransfersFormComponent} from './components/transfers-form/transfers-form.component';
import {OsahftContentComponent} from './components/osahft-content/osahft-content.component';
import {ToastComponent} from "./components/toast/toast.component";
import {ToastService} from "./services/toast/toast.service";
import {TransfersService} from "./services/transfers/transfers.service";
import { TransferSuccessComponent } from './components/transfer-success/transfer-success.component';

@NgModule({
  declarations: [
    AppComponent,
    TransfersFormComponent,
    OsahftContentComponent,
    ToastComponent,
    TransferSuccessComponent,
  ],
  imports: [
    BrowserModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    TagInputModule,
    AngularEditorModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FileUploadModule,
  ],
  providers: [ToastService, TransfersService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
