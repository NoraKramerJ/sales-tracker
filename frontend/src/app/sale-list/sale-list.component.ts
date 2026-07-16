import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Sale } from '../sale.model';
import { SaleService } from '../sale.service';

@Component({
  selector: 'app-sale-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <h2>Sales</h2>
    <div *ngIf="sales.length === 0" style="margin-top:16px;color:#666;">No sales found. Add one!</div>
    <table *ngIf="sales.length > 0" style="margin-top:16px;">
      <thead>
        <tr>
          <th>Customer</th>
          <th>Product</th>
          <th>Amount</th>
          <th>Date</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let sale of sales">
          <td>{{ sale.customerName }}</td>
          <td>{{ sale.product }}</td>
          <td>{{ sale.amount | currency }}</td>
          <td>{{ sale.saleDate }}</td>
          <td>{{ sale.status }}</td>
          <td style="display:flex;gap:8px;">
            <button class="primary" [routerLink]="['/edit', sale.id]">Edit</button>
            <button class="danger" (click)="delete(sale.id!)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  `
})
export class SaleListComponent implements OnInit {
  sales: Sale[] = [];

  constructor(private saleService: SaleService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.saleService.getAll().subscribe(data => this.sales = data);
  }

  delete(id: number): void {
    if (confirm('Delete this sale?')) {
      this.saleService.delete(id).subscribe(() => this.load());
    }
  }
}
