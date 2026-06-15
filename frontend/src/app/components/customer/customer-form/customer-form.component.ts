import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { CustomerRequest } from '../../../models/api.models';

// @ts-ignore
@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="row">
      <div class="col-md-8 offset-md-2">
        <h2 class="mb-4">{{ isEdit ? 'Edit Customer' : 'Create New Customer' }}</h2>

        <div class="alert alert-danger" *ngIf="error">
          <strong>Error:</strong> {{ error }}
        </div>

        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="mb-3">
            <label for="customerId" class="form-label">Customer ID *</label>
            <input type="number" class="form-control" id="customerId" 
                   formControlName="customerId" required>
            <div class="text-danger small" 
                 *ngIf="form.get('customerId')?.invalid && form.get('customerId')?.touched">
              Customer ID is required
            </div>
          </div>

          <div class="mb-3">
            <label for="customerFullName" class="form-label">Full Name *</label>
            <input type="text" class="form-control" id="customerFullName" 
                   formControlName="customerFullName" required>
            <div class="text-danger small" 
                 *ngIf="form.get('customerFullName')?.invalid && form.get('customerFullName')?.touched">
              Full name is required
            </div>
          </div>

          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" 
                   formControlName="email">
            <div class="text-danger small" 
                 *ngIf="form.get('email')?.invalid && form.get('email')?.touched">
              Invalid email format
            </div>
          </div>

          <div class="mb-3">
            <label for="phoneNumber" class="form-label">Phone Number</label>
            <input type="tel" class="form-control" id="phoneNumber" 
                   formControlName="phoneNumber" placeholder="+1234567890">
          </div>

          <div class="mb-3">
            <label for="address" class="form-label">Address</label>
            <input type="text" class="form-control" id="address" 
                   formControlName="address">
          </div>

          <div class="mb-3">
            <label for="dateOfBirth" class="form-label">Date of Birth</label>
            <input type="date" class="form-control" id="dateOfBirth" 
                   formControlName="dateOfBirth">
          </div>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary" [disabled]="!form.valid || loading">
              {{ isEdit ? 'Update' : 'Create' }}
            </button>
            <a routerLink="/customers" class="btn btn-secondary">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  `
})
export class CustomerFormComponent implements OnInit {
  form: FormGroup;
  isEdit = false;
  loading = false;
  error: string | null = null;
  customerId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      customerId: [null, Validators.required],
      customerFullName: ['', Validators.required],
      email: ['', Validators.email],
      phoneNumber: [''],
      address: [''],
      dateOfBirth: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.customerId = params['id'];
        this.isEdit = true;
        this.loadCustomer();
      }
    });
  }

  loadCustomer(): void {
    if (!this.customerId) return;
    
    this.customerService.getCustomerById(this.customerId).subscribe({
      next: (customer) => {
        this.form.patchValue({
          customerId: customer.customerId,
          customerFullName: customer.customerFullName,
          email: customer.email,
          phoneNumber: customer.phoneNumber,
          address: customer.address,
          dateOfBirth: customer.dateOfBirth
        });
      },
      error: (err) => {
        this.error = 'Failed to load customer';
        console.error(err);
      }
    });
  }

  onSubmit(): void {
    if (!this.form.valid) return;

    this.loading = true;
    this.error = null;

    const data: CustomerRequest = this.form.value;

    const request = this.isEdit && this.customerId
      ? this.customerService.updateCustomer(this.customerId, data)
      : this.customerService.createCustomer(data);

    request.subscribe({
      next: () => {
        this.router.navigate(['/customers']);
      },
      error: (err) => {
        this.error = 'Failed to save customer';
        this.loading = false;
        console.error(err);
      }
    });
  }
}

