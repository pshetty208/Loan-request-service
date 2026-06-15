import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { LoanService } from '../../../services/loan.service';
import { CustomerService } from '../../../services/customer.service';
import { LoanRequest, CustomerResponse } from '../../../models/api.models';

@Component({
  selector: 'app-loan-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="row">
      <div class="col-md-8 offset-md-2">
        <h2 class="mb-4">{{ isEdit ? 'Edit Loan' : 'Create New Loan' }}</h2>

        <div class="alert alert-danger" *ngIf="error">
          <strong>Error:</strong> {{ error }}
        </div>

        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="mb-3">
            <label for="customerId" class="form-label">Customer *</label>
            <select class="form-select" id="customerId" 
                    formControlName="customerId" required>
              <option value="">Select a customer</option>
              <option *ngFor="let customer of customers" [value]="customer.customerId">
                {{ customer.customerFullName }} ({{ customer.customerId }})
              </option>
            </select>
            <div class="text-danger small" 
                 *ngIf="form.get('customerId')?.invalid && form.get('customerId')?.touched">
              Customer is required
            </div>
          </div>

          <div class="mb-3">
            <label for="amount" class="form-label">Amount *</label>
            <input type="number" step="0.01" class="form-control" id="amount" 
                   formControlName="amount" required min="500" max="12000.50">
            <small class="text-muted">Min: 500, Max: 12000.50</small>
            <div class="text-danger small" 
                 *ngIf="form.get('amount')?.invalid && form.get('amount')?.touched">
              Amount is required (500 - 12000.50)
            </div>
          </div>

          <div class="mb-3">
            <label for="loanType" class="form-label">Loan Type</label>
            <select class="form-select" id="loanType" formControlName="loanType">
              <option value="">Select type</option>
              <option value="PERSONAL">Personal Loan</option>
              <option value="HOME">Home Loan</option>
              <option value="AUTO">Auto Loan</option>
              <option value="BUSINESS">Business Loan</option>
              <option value="EDUCATION">Education Loan</option>
            </select>
          </div>

          <div class="mb-3">
            <label for="termMonths" class="form-label">Term (Months)</label>
            <input type="number" class="form-control" id="termMonths" 
                   formControlName="termMonths" min="1" step="1">
          </div>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-success" [disabled]="!form.valid || loading">
              {{ isEdit ? 'Update' : 'Create' }}
            </button>
            <a routerLink="/loans" class="btn btn-secondary">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  `
})
export class LoanFormComponent implements OnInit {
  form: FormGroup;
  isEdit = false;
  loading = false;
  error: string | null = null;
  loanId: number | null = null;
  customers: CustomerResponse[] = [];

  constructor(
    private fb: FormBuilder,
    private loanService: LoanService,
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      customerId: [null, Validators.required],
      amount: [null, [Validators.required, Validators.min(500), Validators.max(12000.50)]],
      loanType: [''],
      termMonths: [null]
    });
  }

  ngOnInit(): void {
    this.loadCustomers();

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.loanId = params['id'];
        this.isEdit = true;
        this.loadLoan();
      }
    });

    this.route.queryParams.subscribe(params => {
      if (params['customerId'] && !this.isEdit) {
        this.form.patchValue({ customerId: parseInt(params['customerId'], 10) });
      }
    });

  }

  loadCustomers(): void {
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers = data;
      },
      error: (err) => {
        console.error('Failed to load customers:', err);
      }
    });
  }

  loadLoan(): void {
    if (!this.loanId) return;

    this.loanService.getLoanById(this.loanId).subscribe({
      next: (loan) => {
        this.form.patchValue({
          customerId: loan.customerId,
          customerFullName: loan.customerFullName,
          amount: loan.amount,
          loanType: loan.loanType,
          termMonths: loan.termMonths
        });
      },
      error: (err) => {
        this.error = 'Failed to load loan';
        console.error(err);
      }
    });
  }

  onSubmit(): void {
    if (!this.form.valid) return;

    this.loading = true;
    this.error = null;

    const data: LoanRequest = {
      amount: this.form.value.amount,
      customerFullName: this.form.value.customerFullName,
      customerId: this.form.value.customerId,
      loanType: this.form.value.loanType,
      termMonths: this.form.value.termMonths
    };

    const request = this.isEdit && this.loanId
      ? this.loanService.updateLoan(this.loanId, data)
      : this.loanService.createLoan(data);

    request.subscribe({
      next: () => {
        this.router.navigate(['/loans']);
      },
      error: (err) => {
        this.error = 'Failed to save loan';
        this.loading = false;
        console.error(err);
      }
    });
  }
}

