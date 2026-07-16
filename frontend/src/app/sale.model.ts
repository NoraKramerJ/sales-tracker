export interface Sale {
  id?: number;
  customerName: string;
  product: string;
  amount: number;
  saleDate: string;
  status: string;
  saleType?: 'BULK' | 'INDIVIDUAL';
  phoneNumber?: string;
  companyWebsite?: string;  // BULK only
  taxId?: string;           // BULK only
}
