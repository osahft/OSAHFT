import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransfersFormComponent } from './transfers-form.component';

describe('TransfersFormComponent', () => {
  let component: TransfersFormComponent;
  let fixture: ComponentFixture<TransfersFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransfersFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransfersFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
