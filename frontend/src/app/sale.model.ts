export interface UserType {
  id: number;
  typeName: string;
  phoneNumber?: string;
}

export interface Sale {
  id?: number;
  customerName: string;
  product: string;
  amount: number;
  saleDate: string;
  status: string;
  userType?: UserType;
}
