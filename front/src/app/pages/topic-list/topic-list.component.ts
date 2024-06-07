import { Component, OnInit } from '@angular/core';
import { Topic } from 'src/app/interfaces/response/topic.interface';
import { AuthService } from 'src/app/services/auth.service';
import { TopicService } from 'src/app/services/topic.service';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrls: ['./topic-list.component.scss']
})
export class TopicListComponent implements OnInit {

  topics$ = this.topicService.getTopics()
  user$ = this.authService.getUser()
  userTopic: Topic[] = []

  constructor(private topicService: TopicService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user$.subscribe({
      next: (user) => {
        this.userTopic = user.topics
      }
    })
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

  isSubscribed(topicId: number) {
    return this.userTopic.find((topic) => topic.id === topicId)
  }
}
