import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PurchaseService {
  private apiUrl = 'http://localhost:8080/api/purchases';
  getPurchases: any;

  constructor(private http: HttpClient) {}

  // Get today's purchases
  getTodayPurchases(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/today`);
  }

  // Create new purchase
  createPurchase(request: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, request);
  }

  // Get today’s unique customer count
  getTodayCustomerCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/today-customers`);
  }

  // Optional: Get purchases by customer phone
  getPurchasesByCustomer(phone: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/customer/${phone}`);
  }
}
