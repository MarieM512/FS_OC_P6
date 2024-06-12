import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EmailValidator, FormBuilder, Validators } from '@angular/forms';
import { LoginRequest } from 'src/app/interfaces/request/loginRequest.interface';
import { SignupRequest } from 'src/app/interfaces/request/signupRequest.interface';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.scss']
})
export class ConnectionComponent implements OnInit {
  type!: String
  title!: String
  buttonTitle!: String
  hide = true
  onError = ''

  loginForm = this.formBuilder.group({
    email: ['', [Validators.required]],
    password: ['', [Validators.required]]
  })

  signupForm = this.formBuilder.group({
    username: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  })

  get signupEmail() { return this.signupForm.get('email') }
  get signupPassword() { return this.signupForm.get('password') }

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.type = this.route.snapshot.params["type"]
    if (this.type == "login") {
      this.title = "Se connecter"
      this.buttonTitle = "Se connecter"
    } else {
      this.title = "Inscription"
      this.buttonTitle = "S'inscrire"
    }
  }

  submit() {
    if (this.type == "login") {
      const loginRequest = this.loginForm.value as LoginRequest
      this.authService.login(loginRequest).subscribe({
        next: (response) => {
          localStorage.setItem('token', response.token)
          this.router.navigate(['/posts'])
        },
        error: error => this.onError = error.error
      });
    } else {
      const signupRequest = this.signupForm.value as SignupRequest
      this.authService.register(signupRequest).subscribe({
        next: (response) => {
          localStorage.setItem('token', response.token)
          this.router.navigate(['/posts'])
        },
        error: error => this.onError = error.error
      });
    }
  }
}
