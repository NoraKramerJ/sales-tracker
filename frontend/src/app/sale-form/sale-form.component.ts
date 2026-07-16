import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sale } from '../sale.model';
import { SaleService } from '../sale.service';

// Sales over this amount must be recorded as Individual (not Retail).
const AMOUNT_LIMIT = 400;

@Component({
  selector: 'app-sale-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>{{ isEdit ? 'Edit Sale' : 'New Sale' }}</h2>
    <form (ngSubmit)="save()" style="max-width:480px;margin-top:16px;display:flex;flex-direction:column;gap:12px;background:white;padding:24px;border-radius:6px;box-shadow:0 1px 4px rgba(0,0,0,0.1);">
      <label>
        Customer Name
        <input [(ngModel)]="sale.customerName" name="customerName" required />
      </label>
      <label>
        Product
        <input [(ngModel)]="sale.product" name="product" required />
      </label>
      <label>
        Amount
        <input [(ngModel)]="sale.amount" name="amount" type="number" step="0.01" required />
      </label>
      <label>
        Sale Date
        <input [(ngModel)]="sale.saleDate" name="saleDate" type="date" required />
      </label>
      <label>
        Status
        <select [(ngModel)]="sale.status" name="status">
          <option>Open</option>
          <option>Pending</option>
          <option>Closed</option>
        </select>
      </label>
      <label>
        Sale Type
        <select [(ngModel)]="sale.saleType" name="saleType">
          <option value="BULK">Bulk</option>
          <option value="INDIVIDUAL">Individual</option>
        </select>
      </label>
      <div *ngIf="exceedsLimit" style="color:#b45309;font-size:0.9em;margin-top:-4px;">
        ⚠ Amount over {{ AMOUNT_LIMIT }} — this will be saved as an Bulk sale sale.
      </div>
      <label>
        Phone Number
        <input [(ngModel)]="sale.phoneNumber" name="phoneNumber" placeholder="555-100-0000" />
      </label>
      <div style="display:flex;gap:8px;margin-top:8px;">
        <button class="primary" type="submit">{{ isEdit ? 'Update' : 'Create' }}</button>
        <button class="secondary" type="button" (click)="cancel()">Cancel</button>
      </div>
    </form>
  `
})
export class SaleFormComponent implements OnInit {
  sale: Sale = { customerName: '', product: '', amount: 0, saleDate: '', status: 'Open', saleType: 'BULK' };
  isEdit = false;
  private id?: number;
  readonly AMOUNT_LIMIT = AMOUNT_LIMIT;

  // True when the amount exceeds the limit but the type isn't yet Individual.
  get exceedsLimit(): boolean {
    return this.sale.amount > AMOUNT_LIMIT && this.sale.saleType !== 'INDIVIDUAL';
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private saleService: SaleService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEdit = true;
      this.id = +idParam;
      this.saleService.getById(this.id).subscribe(data => this.sale = data);
    }
  }

  save(): void {
    // Rule: amount over the limit forces the sale type to Individual. Warn first.
    if (this.sale.amount > AMOUNT_LIMIT && this.sale.saleType !== 'INDIVIDUAL') {
      const ok = confirm(
        `Amount ${this.sale.amount} exceeds ${AMOUNT_LIMIT}, so this sale must be recorded ` +
        `as "Individual" (not "Bulk"). Save it as Individual?`
      );
      if (!ok) {
        return; // let the user adjust the amount or type instead
      }
      this.sale.saleType = 'INDIVIDUAL';
    }

    if (this.isEdit && this.id) {
      this.saleService.update(this.id, this.sale).subscribe(() => this.router.navigate(['/']));
    } else {
      this.saleService.create(this.sale).subscribe(() => this.router.navigate(['/']));
    }
  }

  cancel(): void {
    this.router.navigate(['/']);
  }
}
