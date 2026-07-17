import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sale } from '../sale.model';
import { SaleService } from '../sale.service';

const MIN_AMOUNT = 30;
const BULK_THRESHOLD = 450;
// Accepts: 5551234567 | 555-123-4567 | (555) 123-4567 | (555)123-4567
const US_PHONE_REGEX = /^\(?[2-9]\d{2}\)?[\s\-]?\d{3}[\s\-]?\d{4}$/;
// Accepts http:// or https:// URLs with a valid domain
const URL_REGEX = /^https?:\/\/[\w\-]+(\.[\w\-]+)+([\w\-._~:/?#\[\]@!$&'()*+,;=%]*)?$/;

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
        <input [(ngModel)]="sale.amount" name="amount" type="number" step="0.01" min="30" required (ngModelChange)="onAmountChange()" />
        <div *ngIf="amountTooLow" style="color:#c62828;font-size:0.85em;margin-top:4px;">Minimum sale amount is $30.</div>
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
      <div>
        <div style="font-size:14px;margin-bottom:4px;">Sale Type</div>
        <span [ngStyle]="{
          'padding': '4px 12px',
          'border-radius': '12px',
          'font-weight': '600',
          'font-size': '13px',
          'background': derivedSaleType === 'INDIVIDUAL' ? '#e8f5e9' : '#e3f2fd',
          'color':      derivedSaleType === 'INDIVIDUAL' ? '#2e7d32' : '#1565c0'
        }">{{ derivedSaleType === 'INDIVIDUAL' ? 'Individual' : 'Bulk' }}</span>
        <div style="color:#888;font-size:0.82em;margin-top:4px;">
          {{ derivedSaleType === 'INDIVIDUAL' ? 'Amount \u2264 $450 \u2014 automatically Individual' : 'Amount > $450 \u2014 automatically Bulk' }}
        </div>
      </div>
      <label>
        Phone Number *
        <input [(ngModel)]="sale.phoneNumber" name="phoneNumber" required
               placeholder="555-123-4567 or (555) 123-4567"
               (ngModelChange)="onPhoneChange()" />
        <div *ngIf="phoneInvalid" style="color:#c62828;font-size:0.85em;margin-top:4px;">
          Enter a valid US phone number, e.g. 555-123-4567 or (555) 123-4567.
        </div>
      </label>

      <!-- Bulk-only fields — only shown when amount > $450 -->
      <ng-container *ngIf="derivedSaleType === 'BULK'">
        <div style="border-top:1px solid #e0e0e0;padding-top:12px;">
          <div style="font-size:13px;font-weight:600;color:#1565c0;margin-bottom:8px;">Bulk Sale Details</div>
          <label>
            Company Website *
            <input [(ngModel)]="sale.companyWebsite" name="companyWebsite"
                   placeholder="https://company.com"
                   (ngModelChange)="onBulkFieldChange()" />
            <div *ngIf="websiteInvalid" style="color:#c62828;font-size:0.85em;margin-top:4px;">
              Enter a valid URL, e.g. https://company.com.
            </div>
          </label>
          <label style="margin-top:8px;display:block;">
            Tax ID *
            <input [(ngModel)]="sale.taxId" name="taxId"
                   placeholder="12-3456789"
                   (ngModelChange)="onBulkFieldChange()" />
            <div *ngIf="taxIdInvalid" style="color:#c62828;font-size:0.85em;margin-top:4px;">
              Tax ID is required for Bulk sales.
            </div>
          </label>
        </div>
      </ng-container>

      <div style="display:flex;gap:8px;margin-top:8px;">
        <button class="primary" type="submit" [disabled]="amountTooLow || phoneInvalid || websiteInvalid || taxIdInvalid">{{ isEdit ? 'Update' : 'Create' }}</button>
        <button class="secondary" type="button" (click)="cancel()">Cancel</button>
      </div>
    </form>
  `
})
export class SaleFormComponent implements OnInit {
  sale: Sale = { customerName: '', product: '', amount: 0, saleDate: '', status: 'Open', phoneNumber: '' };
  derivedSaleType: 'INDIVIDUAL' | 'BULK' = 'INDIVIDUAL';
  amountTooLow = true;
  phoneInvalid = true;
  websiteInvalid = false; // only required when BULK
  taxIdInvalid = false;   // only required when BULK
  isEdit = false;
  private id?: number;

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
      this.saleService.getById(this.id).subscribe(data => {
        this.sale = data;
        this.onAmountChange();
        this.onPhoneChange();
      });
    }
  }

  onAmountChange(): void {
    this.amountTooLow = this.sale.amount < MIN_AMOUNT;
    this.derivedSaleType = this.sale.amount <= BULK_THRESHOLD ? 'INDIVIDUAL' : 'BULK';
    if (this.derivedSaleType === 'INDIVIDUAL') {
      // clear bulk fields and validation when switching back to individual
      this.sale.companyWebsite = undefined;
      this.sale.taxId = undefined;
      this.websiteInvalid = false;
      this.taxIdInvalid = false;
    } else {
      this.onBulkFieldChange();
    }
  }

  onPhoneChange(): void {
    const val = (this.sale.phoneNumber ?? '').trim();
    this.phoneInvalid = !val || !US_PHONE_REGEX.test(val);
  }

  onBulkFieldChange(): void {
    const url = (this.sale.companyWebsite ?? '').trim();
    this.websiteInvalid = !url || !URL_REGEX.test(url);
    this.taxIdInvalid   = !(this.sale.taxId ?? '').trim();
  }

  save(): void {
    if (this.amountTooLow || this.phoneInvalid || this.websiteInvalid || this.taxIdInvalid) return;
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
