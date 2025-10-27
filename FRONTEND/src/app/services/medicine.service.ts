import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Medicine {
  id?: number;   // ✅ now optional
  name: string;
  quantity: number;
  price: number;
  description?: string;
}

@Injectable({ providedIn: 'root' })
export class MedicineService {
  private apiUrl = 'http://localhost:8080/api/medicines';

  constructor(private http: HttpClient) {}

  // 🔹 Search medicines by prefix
  searchByPrefix(prefix: string): Observable<Medicine[]> {
    return this.http.get<Medicine[]>(`${this.apiUrl}/suggest?prefix=${prefix}`);
  }

  // 🔹 Out-of-stock count
  getOutOfStockCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/out-of-stock/count`);
  }

  // 🔹 Optional: Get list of out-of-stock medicines
  getOutOfStockMedicines(): Observable<Medicine[]> {
    return this.http.get<Medicine[]>(`${this.apiUrl}/out-of-stock`);
  }
    // 🔹 Add single medicine
  addMedicine(medicine: Medicine): Observable<Medicine> {
    return this.http.post<Medicine>(`${this.apiUrl}/add`, medicine);
  }
}
