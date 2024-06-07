import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Topic } from 'src/app/interfaces/response/topic.interface';
import { UserUpdate } from 'src/app/interfaces/response/userUpdate.interface';
import { AuthService } from 'src/app/services/auth.service';
import { TopicService } from 'src/app/services/topic.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

  user$ = this.authService.getUser()
  topics$!: Observable<Topic[]>
  hide = true

  accountForm = this.formBuilder.group({
    username: ['', [Validators.minLength(3)]],
    email: ['', [Validators.email]],
    password: ['', [Validators.nullValidator, Validators.minLength(8)]]
  })

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private topicService: TopicService) { }

  ngOnInit(): void {
    this.topics$ = this.user$.pipe(map(user => user.topics))
  }

  save() {
    const account = this.accountForm.value as UserUpdate
    this.authService.updateUser(account).subscribe({
      next(value) {
          console.log(value)
      },
      error(err) {
          console.log(err)
      },
    })
  }

  logout() {
    this.authService.logout()
    this.router.navigate([''])
  }

  unsubscribe(topic: Topic) {
    this.topicService.unsubscribe(topic).subscribe({
      next(value) {
        window.location.reload()
      },
      error(err) {
          console.log(err)
      },
    })
  }
}
