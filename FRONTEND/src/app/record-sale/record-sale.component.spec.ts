import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecordSaleComponent } from './record-sale.component';

describe('RecordSaleComponent', () => {
  let component: RecordSaleComponent;
  let fixture: ComponentFixture<RecordSaleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecordSaleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecordSaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
