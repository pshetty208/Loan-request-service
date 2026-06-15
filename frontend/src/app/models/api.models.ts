export interface CustomerRequest {
  customerId: number;
  customerFullName: string;
  email?: string;
  phoneNumber?: string;
  address?: string;
  dateOfBirth?: string;
}

export interface CustomerResponse extends CustomerRequest {
  id?: number;
}

export interface LoanRequest {
  amount: number;
  customerFullName: string;
  customerId: number;
  loanType?: string;
  termMonths?: number;
}

export interface LoanResponse extends LoanRequest {
  id?: number;
  status?: string;
  createdAt?: string;
}

