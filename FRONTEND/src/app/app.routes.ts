import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' }, // default route
  {
    path: 'add',
    loadComponent: () =>
      import('./add-medicine/add-medicine.component').then(
        (m) => m.AddMedicineComponent
      ),
  },
  {
    path: 'search',
    loadComponent: () =>
      import('./search-medicine/search-medicine.component').then(
        (m) => m.SearchMedicineComponent
      ),
  },
  {
    path: 'record-sale',
    loadComponent: () =>
      import('./record-sale/record-sale.component').then(
        (m) => m.RecordSaleComponent
      ),
  },
 
  { path: '**', redirectTo: '' }, // fallback for unknown routes
];
