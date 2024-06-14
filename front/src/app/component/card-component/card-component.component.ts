import { Component, Input, OnInit } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Topic } from 'src/app/interfaces/response/topic.interface';
import { AuthService } from 'src/app/services/auth.service';
import { TopicService } from 'src/app/services/topic.service';

@Component({
  selector: 'app-card-component',
  templateUrl: './card-component.component.html',
  styleUrls: ['./card-component.component.scss']
})
export class CardComponentComponent implements OnInit {

  @Input() toSubscribe: Boolean = false

  breakpoint!: number
  topics$!: Observable<Topic[]>
  user$ = this.authService.getUser()
  userTopic: Topic[] = []

  constructor(private topicService: TopicService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user$.subscribe({
      next: (user) => {
        this.userTopic = user.topics
      }
    })
    if (this.toSubscribe) {
      this.topics$ = this.topicService.getTopics()
    } else {
      this.topics$ = this.user$.pipe(map(user => user.topics))
    }

    this.breakpoint = (window.innerWidth <= 500) ? 1 : 2
  }

  subscribe(topic: Topic) {
    this.topicService.subscribe(topic).subscribe({
      next(value) {
          window.location.reload()
      },
      error(err) {
          console.log(err)
      },
    });
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

  isSubscribed(topicId: number) {
    return this.userTopic.find((topic) => topic.id === topicId)
  }

  onResize(event: any) {
    this.breakpoint = (event.target.innerWidth <= 500) ? 1 : 2
  }

}
