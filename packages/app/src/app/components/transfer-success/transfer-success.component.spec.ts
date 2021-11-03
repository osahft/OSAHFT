import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferSuccessComponent } from './transfer-success.component';

describe('TransferSuccessComponent', () => {
  let component: TransferSuccessComponent;
  let fixture: ComponentFixture<TransferSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransferSuccessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransferSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
