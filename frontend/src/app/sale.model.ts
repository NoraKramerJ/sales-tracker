export interface Sale {
  id?: number;
  customerName: string;
  product: string;
  amount: number;
  saleDate: string;
  status: string;
  saleType?: 'BULK' | 'INDIVIDUAL';
  phoneNumber?: string;  // contact number entered per sale
}
