import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { DigitalmarketingComponent } from './components/digitalmarketing/digitalmarketing.component';
import { UspComponent } from './components/usp/usp.component';
import { PricingComponent } from './components/pricing/pricing.component';
import { WebsiteComponent } from './components/website/website.component';
import { ContactComponent } from './components/contact/contact.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    DigitalmarketingComponent,
    UspComponent,
    PricingComponent,
    WebsiteComponent,
    ContactComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
