import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoanRequest, LoanResponse } from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private apiUrl = '/api/v1/loan-service';

  constructor(private http: HttpClient) { }

  createLoan(loan: LoanRequest): Observable<LoanResponse> {
    return this.http.post<LoanResponse>(this.apiUrl, loan);
  }

  getAllLoans(): Observable<LoanResponse[]> {
    return this.http.get<LoanResponse[]>(`${this.apiUrl}/loans`);
  }

  getLoanById(id: number): Observable<LoanResponse> {
    return this.http.get<LoanResponse>(`${this.apiUrl}/loans/${id}`);
  }

  getLoansByCustomerId(customerId: number): Observable<LoanResponse[]> {
    return this.http.get<LoanResponse[]>(`${this.apiUrl}/loans/customers/${customerId}`);
  }

  getTotalLoanByCustomerId(customerId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/loans/customers/${customerId}/total`);
  }

  updateLoan(id: number, loan: LoanRequest): Observable<LoanResponse> {
    return this.http.put<LoanResponse>(`${this.apiUrl}/loans/${id}`, loan);
  }

  deleteLoan(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/loans/${id}`);
  }
}

