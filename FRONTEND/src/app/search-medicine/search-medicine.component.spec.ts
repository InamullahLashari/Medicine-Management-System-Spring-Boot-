import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchMedicineComponent } from './search-medicine.component';

describe('SearchMedicineComponent', () => {
  let component: SearchMedicineComponent;
  let fixture: ComponentFixture<SearchMedicineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchMedicineComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchMedicineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
