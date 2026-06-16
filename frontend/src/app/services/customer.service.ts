import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerRequest, CustomerResponse } from '../models/api.models';
import { environment } from 'src/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = environment.customerServiceUrl;

  constructor(private http: HttpClient) { }

  createCustomer(customer: CustomerRequest): Observable<CustomerResponse> {
    return this.http.post<CustomerResponse>(this.apiUrl, customer);
  }

  getAllCustomers(): Observable<CustomerResponse[]> {
    return this.http.get<CustomerResponse[]>(this.apiUrl);
  }

  getCustomerById(id: number): Observable<CustomerResponse> {
    return this.http.get<CustomerResponse>(`${this.apiUrl}/customerId/${id}`);
  }

  updateCustomer(id: number, customer: CustomerRequest): Observable<CustomerResponse> {
    return this.http.put<CustomerResponse>(`${this.apiUrl}/${id}`, customer);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

