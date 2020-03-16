import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ContactComponent } from './components/contact/contact.component';
import { DigitalmarketingComponent } from './components/digitalmarketing/digitalmarketing.component';
import { PricingComponent } from './components/pricing/pricing.component';
import { UspComponent } from './components/usp/usp.component';
import { WebsiteComponent } from './components/website/website.component';


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'digitalmarketing', component: DigitalmarketingComponent},
  {path: 'pricing', component: PricingComponent},
  {path: 'usp', component: UspComponent},
  {path: 'website', component: WebsiteComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
