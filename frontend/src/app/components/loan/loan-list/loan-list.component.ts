import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoanService } from '../../../services/loan.service';
import { LoanResponse } from '../../../models/api.models';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="row mb-4">
      <div class="col-md-8">
        <h2>Loans</h2>
      </div>
      <div class="col-md-4">
        <a routerLink="/loans/create" class="btn btn-success float-end">
          Create New Loan
        </a>
      </div>
    </div>

    <div class="alert alert-info" *ngIf="loading">
      Loading loans...
    </div>

    <div class="alert alert-danger" *ngIf="error">
      <strong>Error:</strong> {{ error }}
    </div>

    <div class="card" *ngIf="!loading && !error">
      <div class="table-responsive">
        <table class="table table-striped table-hover mb-0">
          <thead class="table-light">
            <tr>
              <th>ID</th>
              <th>Customer</th>
              <th>Amount</th>
              <th>Type</th>
              <th>Term</th>
              <th>Status</th>
              <th>Created</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let loan of loans">
              <td>{{ loan.id }}</td>
              <td>{{ loan.customerFullName }} ({{ loan.customerId }})</td>
              <td>{{ loan.amount | number: '1.2-2' }}</td>
              <td>{{ loan.loanType }}</td>
              <td>{{ loan.termMonths }} mo</td>
              <td>
                <span class="badge" [ngClass]="{
                  'bg-success': loan.status === 'APPROVED',
                  'bg-warning': loan.status === 'PENDING',
                  'bg-danger': loan.status === 'REJECTED'
                }">
                  {{ loan.status }}
                </span>
              </td>
              <td>{{ loan.createdAt | date: 'short' }}</td>
              <td>
                <a [routerLink]="['/loans', loan.id, 'edit']" 
                   class="btn btn-sm btn-warning btn-action">
                  Edit
                </a>
                <button (click)="deleteLoan(loan.id!)" class="btn btn-sm btn-danger">
                  Delete
                </button>
              </td>
            </tr>
            <tr *ngIf="!loading && loans.length === 0">
              <td colspan="8" class="text-center text-muted">
                No loans found.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class LoanListComponent implements OnInit {
  loans: LoanResponse[] = [];
  loading = false;
  error: string | null = null;

  constructor(private loanService: LoanService) { }

  ngOnInit(): void {
    this.loadLoans();
  }

  loadLoans(): void {
    this.loading = true;
    this.error = null;
    this.loanService.getAllLoans().subscribe({
      next: (data) => {
        this.loans = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load loans';
        this.loading = false;
        console.error(err);
      }
    });
  }

  deleteLoan(id: number): void {
    if (confirm('Are you sure you want to delete this loan?')) {
      this.loanService.deleteLoan(id).subscribe({
        next: () => {
          this.loans = this.loans.filter(l => l.id !== id);
        },
        error: (err) => {
          this.error = 'Failed to delete loan';
          console.error(err);
        }
      });
    }
  }
}

