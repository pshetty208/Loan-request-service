import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { CustomerListComponent } from './components/customer/customer-list/customer-list.component';
import { CustomerFormComponent } from './components/customer/customer-form/customer-form.component';
import { LoanListComponent } from './components/loan/loan-list/loan-list.component';
import { LoanFormComponent } from './components/loan/loan-form/loan-form.component';
// import { CustomerLoansComponent } from './components/loan/customer-loans/customer-loans.component';

export const appRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'customers', component: CustomerListComponent },
  { path: 'customers/create', component: CustomerFormComponent },
  { path: 'customers/:id/edit', component: CustomerFormComponent },
  { path: 'loans', component: LoanListComponent },
  { path: 'loans/create', component: LoanFormComponent },
  { path: 'loans/:id/edit', component: LoanFormComponent },
  // { path: 'customers/:customerId/loans', component: CustomerLoansComponent },
  { path: '**', redirectTo: '' }
];

