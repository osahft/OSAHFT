import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TagInputModule} from 'ngx-chips';
import {AngularEditorModule} from '@kolkov/angular-editor';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
// components
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NgxDropzoneModule} from 'ngx-dropzone';
import {TransfersFormComponent} from './components/transfers-form/transfers-form.component';
import {OsahftContentComponent} from './components/osahft-content/osahft-content.component';
import {ToastComponent} from "./components/toast/toast.component";


@NgModule({
  declarations: [
    AppComponent,
    TransfersFormComponent,
    OsahftContentComponent,
    ToastComponent,
  ],
  imports: [
    BrowserModule,
    NgbModule,
    NgxDropzoneModule,
    FormsModule,
    ReactiveFormsModule,
    TagInputModule,
    AngularEditorModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
