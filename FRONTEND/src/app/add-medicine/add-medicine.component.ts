import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MedicineService, Medicine } from '../services/medicine.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-medicine',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-medicine.component.html',
  styleUrls: ['./add-medicine.component.css']
})
export class AddMedicineComponent {
  newMedicine: Medicine = { name: '', quantity: 0, price: 0,};

  constructor(private medicineService: MedicineService) {}

  addMedicine() {
    if (!this.newMedicine.name || this.newMedicine.price <= 0) {
      Swal.fire('Validation Failed', 'Name and price are required!', 'warning');
      return;
    }

    this.medicineService.addMedicine(this.newMedicine).subscribe({
      next: (res) => {
        Swal.fire({
          icon: 'success',
          title: 'Medicine Added 🎉',
          text: `${res.name} added with stock ${res.quantity}`,
          confirmButtonColor: '#3085d6'
        });
        this.clearForm();
      },
      error: (err) => {
        console.error('Error adding medicine:', err);
        Swal.fire('Error', 'Could not add medicine', 'error');
      }
    });
  }

  clearForm() {
    this.newMedicine = { name: '', quantity: 0, price: 0, };
  }
}
