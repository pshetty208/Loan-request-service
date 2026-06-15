import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { CustomerResponse } from '../../../models/api.models';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="row mb-4">
      <div class="col-md-8">
        <h2>Customers</h2>
      </div>
      <div class="col-md-4">
        <a routerLink="/customers/create" class="btn btn-primary float-end">
          Add New Customer
        </a>
      </div>
    </div>

    <div class="alert alert-info" *ngIf="loading">
      Loading customers...
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
              <th>Customer ID</th>
              <th>Full Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Address</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let customer of customers">
              <td>{{ customer.customerId }}</td>
              <td>{{ customer.customerFullName }}</td>
              <td>{{ customer.email }}</td>
              <td>{{ customer.phoneNumber }}</td>
              <td>{{ customer.address }}</td>
              <td>
                <a [routerLink]="['/customers', customer.customerId, 'edit']" 
                   class="btn btn-sm btn-warning btn-action">
                  Edit
                </a>
                <a [routerLink]="['/customers', customer.customerId, 'loans']" 
                   class="btn btn-sm btn-info btn-action">
                  Loans
                </a>
                <button (click)="deleteCustomer(customer.customerId)" 
                        class="btn btn-sm btn-danger">
                  Delete
                </button>
              </td>
            </tr>
            <tr *ngIf="!loading && customers.length === 0">
              <td colspan="7" class="text-center text-muted">
                No customers found.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class CustomerListComponent implements OnInit {
  customers: CustomerResponse[] = [];
  loading = false;
  error: string | null = null;

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading = true;
    this.error = null;
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load customers';
        this.loading = false;
        console.error(err);
      }
    });
  }

  deleteCustomer(id: number): void {
    if (confirm('Are you sure you want to delete this customer?')) {
      this.customerService.deleteCustomer(id).subscribe({
        next: () => {
          this.customers = this.customers.filter(c => c.id !== id);
        },
        error: (err) => {
          this.error = 'Failed to delete customer';
          console.error(err);
        }
      });
    }
  }
}

