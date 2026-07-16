import { Routes } from '@angular/router';
import { SaleListComponent } from './sale-list/sale-list.component';
import { SaleFormComponent } from './sale-form/sale-form.component';

export const routes: Routes = [
  { path: '', component: SaleListComponent },
  { path: 'new', component: SaleFormComponent },
  { path: 'edit/:id', component: SaleFormComponent }
];
