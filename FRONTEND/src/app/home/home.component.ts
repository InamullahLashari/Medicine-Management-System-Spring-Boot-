import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MedicineService, Medicine } from '../services/medicine.service';
import { PurchaseService } from '../services/purchase.service';
import Swal from 'sweetalert2';

interface PurchaseRequest {
  customerName: string;
  phone: string;
  medicineName: string;
  quantity: number;
  price?: number;
}

interface PurchaseResponse {
  message: string;
  todayCustomerCount: number;
  updatedStock: number;
  outOfStockCount: number;
}

interface Purchase {
  id?: number;
  quantity: number;
  totalPrice?: number;
  purchaseDateTime: string;
  customer: {
    name: string;
    phone: string;
  };
  medicine: {
    name: string;
    price?: number;
  };
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  customers: Purchase[] = [];
  customer = { name: '', phone: '', medicine: '', quantity: 1, price: 0 };
  searchQuery = '';

  suggestions: { name: string; quantity: number; description?: string; price?: number }[] = [];
  highlightedIndex = -1;
  selectedMedicine: { name: string; quantity: number; description?: string; price?: number } | null = null;

  todayCustomers = 0;
  outOfStock = 0;
  todayRevenue = 0;

  themes: Array<'light' | 'dark' | 'glass'> = ['dark', 'light', 'glass'];
  modeIndex = 0;
  currentTheme: 'dark' | 'light' | 'glass' = this.themes[this.modeIndex];

  isSidebarCollapsed = false;

  // 🔹 Out-of-stock modal
  showOutOfStockModal = false;
  outOfStockMedicines: { name: string; quantity: number }[] = [];

  // 🔹 Add medicine modal
  showAddMedicineModal = false;
  newMedicine: Partial<Medicine> = { name: '', quantity: 0, price: 0, description: '' };

  constructor(
    private purchaseService: PurchaseService,
    private medicineService: MedicineService
  ) {}

  ngOnInit() {
    this.loadTodayPurchases();
    this.loadMedicineStats();
  }

  toggleSidebar() { this.isSidebarCollapsed = !this.isSidebarCollapsed; }

  toggleTheme() {
    this.modeIndex = (this.modeIndex + 1) % this.themes.length;
    this.currentTheme = this.themes[this.modeIndex];
    document.body.classList.remove('light', 'dark');
    if (this.currentTheme === 'light') document.body.classList.add('light');
    else if (this.currentTheme === 'dark') document.body.classList.add('dark');
  }

  loadTodayPurchases() {
    this.purchaseService.getTodayPurchases().subscribe({
      next: (data: Purchase[]) => {
        this.customers = data;
        this.todayCustomers = data.length;
        this.todayRevenue = data.reduce((sum, c) => sum + ((c.medicine.price || 0) * c.quantity), 0);
      },
      error: (err) => console.error('Error fetching today\'s purchases:', err)
    });
  }

  loadMedicineStats() {
    this.medicineService.getOutOfStockCount().subscribe({
      next: count => this.outOfStock = count,
      error: err => console.error('Error fetching out-of-stock count:', err)
    });
  }

  addCustomer() {
    if (!this.customer.name || !this.customer.phone || !this.customer.medicine) return;

    const request: PurchaseRequest = {
      customerName: this.customer.name,
      phone: this.customer.phone,
      medicineName: this.customer.medicine,
      quantity: this.customer.quantity,
      price: this.customer.price
    };

    this.purchaseService.createPurchase(request).subscribe({
      next: (res: PurchaseResponse) => {
        const totalPrice = this.customer.price * this.customer.quantity;
        this.loadTodayPurchases();
        Swal.fire({
          icon: 'success',
          title: 'Customer Added 🎉',
          html: `<p>${res.message}</p><p><b>Total Price:</b> $${totalPrice.toFixed(2)}</p>`,
          confirmButtonColor: '#3085d6'
        });
        this.outOfStock = res.outOfStockCount;
        this.customer = { name: '', phone: '', medicine: '', quantity: 1, price: 0 };
      },
      error: (err) => {
        console.error('Error creating purchase:', err);
        Swal.fire({
          icon: 'error',
          title: 'Failed!',
          text: 'Could not add customer record.',
          confirmButtonColor: '#d33'
        });
      }
    });
  }

  searchMedicine() {
    const query = this.searchQuery.trim();
    if (!query) { this.suggestions = []; return; }

    this.medicineService.searchByPrefix(query).subscribe({
      next: medicines => {
        this.suggestions = medicines.map(m => ({ name: m.name, quantity: m.quantity, description: m.description, price: m.price }));
        this.highlightedIndex = -1;
      },
      error: () => this.suggestions = []
    });
  }

  selectSuggestion(medicine: { name: string; quantity: number; description?: string; price?: number }) {
    this.customer.medicine = medicine.name;
    this.customer.price = medicine.price || 0;
    this.searchQuery = medicine.name;
    this.suggestions = [];
    this.selectedMedicine = medicine;
  }

  closePopup() { this.selectedMedicine = null; }

  addToCustomer() {
    if (this.selectedMedicine) {
      this.customer.medicine = this.selectedMedicine.name;
      this.customer.price = this.selectedMedicine.price || 0;
      this.closePopup();
    }
  }

  onKeyDown(event: KeyboardEvent) {
    if (!this.suggestions.length) return;
    if (event.key === 'ArrowDown') { this.highlightedIndex = (this.highlightedIndex + 1) % this.suggestions.length; event.preventDefault(); }
    else if (event.key === 'ArrowUp') { this.highlightedIndex = (this.highlightedIndex - 1 + this.suggestions.length) % this.suggestions.length; event.preventDefault(); }
    else if (event.key === 'Enter') { if (this.highlightedIndex >= 0) { this.selectSuggestion(this.suggestions[this.highlightedIndex]); event.preventDefault(); } }
    else if (event.key === 'Escape') { this.suggestions = []; }
  }

  // 🔹 Clear form
  clearForm() {
    this.customer = { name: '', phone: '', medicine: '', quantity: 1, price: 0 };
  }

  // 🔹 Export today's customers as CSV
  exportCSV() {
    if (!this.customers.length) return;
    const headers = ['#', 'Name', 'Phone', 'Medicine', 'Quantity', 'Total Price', 'Date'];
    const rows = this.customers.map((c, i) => [
      i + 1,
      c.customer?.name,
      c.customer?.phone,
      c.medicine?.name,
      c.quantity,
      ((c.medicine.price || 0) * c.quantity).toFixed(2),
      new Date(c.purchaseDateTime).toLocaleString()
    ]);

    let csvContent = headers.join(',') + '\n';
    rows.forEach(r => { csvContent += r.join(',') + '\n'; });

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `today_customers_${new Date().toISOString()}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  // 🔹 Open out-of-stock modal
  openOutOfStockModal() {
    this.medicineService.getOutOfStockMedicines().subscribe({
      next: medicines => {
        this.outOfStockMedicines = medicines.map(m => ({ name: m.name, quantity: m.quantity }));
        this.showOutOfStockModal = true;
      },
      error: err => { console.error('Error fetching out-of-stock medicines:', err); this.showOutOfStockModal = false; }
    });
  }

  closeOutOfStockModal() {
    this.showOutOfStockModal = false;
  }

  // 🔹 Add Medicine Modal Controls
  openAddMedicineModal() {
    this.showAddMedicineModal = true;
  }

  closeAddMedicineModal() {
    this.showAddMedicineModal = false;
    this.newMedicine = { name: '', quantity: 0, price: 0, description: '' };
  }

  // 🔹 Add medicine to backend
  addMedicine() {
    this.medicineService.addMedicine(this.newMedicine as Medicine).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Medicine Added ✅',
          text: `${this.newMedicine.name} has been added.`
        });
        this.closeAddMedicineModal();
        this.loadMedicineStats();
      },
      error: (err) => {
        console.error('Error adding medicine:', err);
        Swal.fire({
          icon: 'error',
          title: 'Failed!',
          text: 'Could not add medicine.'
        });
      }
    });
  }
}
