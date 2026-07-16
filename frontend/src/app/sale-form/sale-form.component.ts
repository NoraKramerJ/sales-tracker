import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sale, UserType } from '../sale.model';
import { SaleService } from '../sale.service';

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
        User Type
        <select [(ngModel)]="selectedUserTypeId" name="userTypeId">
          <option [ngValue]="null">-- None --</option>
          <option *ngFor="let ut of userTypes" [ngValue]="ut.id">{{ ut.typeName }}</option>
        </select>
      </label>
      <div style="display:flex;gap:8px;margin-top:8px;">
        <button class="primary" type="submit">{{ isEdit ? 'Update' : 'Create' }}</button>
        <button class="secondary" type="button" (click)="cancel()">Cancel</button>
      </div>
    </form>
  `
})
export class SaleFormComponent implements OnInit {
  sale: Sale = { customerName: '', product: '', amount: 0, saleDate: '', status: 'Open' };
  userTypes: UserType[] = [];
  selectedUserTypeId: number | null = null;
  isEdit = false;
  private id?: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private saleService: SaleService
  ) {}

  ngOnInit(): void {
    // Load user types for the dropdown
    this.saleService.getAllUserTypes().subscribe(data => this.userTypes = data);

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEdit = true;
      this.id = +idParam;
      this.saleService.getById(this.id).subscribe(data => {
        this.sale = data;
        this.selectedUserTypeId = data.userType?.id ?? null;
      });
    }
  }

  save(): void {
    // Attach the selected user type id before saving
    this.sale.userType = this.selectedUserTypeId
      ? { id: this.selectedUserTypeId, typeName: '' }
      : undefined;

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
