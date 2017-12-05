import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import {NavbarComponent} from "./navbar/navbar.component";
import {UserComponent} from "./user/user.component";
import {UserService} from "./user-service/user.service";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    UserComponent
  ],
  imports: [
    BrowserModule, HttpClientModule,
    RouterModule.forRoot([
      { path:'users', component: UserComponent }
    ])
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
