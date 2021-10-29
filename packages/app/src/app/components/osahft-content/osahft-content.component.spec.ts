import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OsahftContentComponent } from './osahft-content.component';

describe('OsahftContentComponent', () => {
  let component: OsahftContentComponent;
  let fixture: ComponentFixture<OsahftContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OsahftContentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OsahftContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
