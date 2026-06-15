import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="row">
      <div class="col-md-12">
        <h2 class="mb-4">Dashboard</h2>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6 mb-4">
        <div class="card h-100">
          <div class="card-body">
            <h5 class="card-title">Customers</h5>
            <a routerLink="/customers" class="btn btn-sm btn-primary">View All</a>
            <br><br>
            <a routerLink="/customers/create" class="btn btn-sm btn-primary">Add New Customer</a>
          </div>
        </div>
      </div>

      <div class="col-md-6 mb-4">
        <div class="card h-100">
          <div class="card-body">
            <h5 class="card-title">Loans</h5>
            <a routerLink="/loans" class="btn btn-sm btn-success">View All</a>
            <br><br>
            <a routerLink="/loans/create" class="btn btn-sm btn-success">Add new Loans</a>
          </div>
        </div>
      </div>
    </div>
    
  `
})
export class DashboardComponent {
}

