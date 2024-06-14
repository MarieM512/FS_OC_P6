import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserUpdate } from 'src/app/interfaces/response/userUpdate.interface';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

  user$ = this.authService.getUser()
  hide = true
  onError = ''

  accountForm = this.formBuilder.group({
    username: ['', [Validators.minLength(3)]],
    email: ['', [Validators.email]],
    password: ['', [Validators.nullValidator, Validators.minLength(8)]]
  })

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  save() {
    const account = this.accountForm.value as UserUpdate
    this.authService.updateUser(account).subscribe({
      next: (value) => {
        if (account.email.length == 0) {
          window.location.reload()
        } else {
          this.logout()
        }
      },
      error: error => this.onError = error.error
    })
  }

  logout() {
    this.authService.logout()
    this.router.navigate([''])
  }
}
