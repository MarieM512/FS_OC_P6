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

  constructor(private topicService: TopicService, private authService: AuthService) { }

  ngOnInit(): void {
  }

  subscribe(topic: Topic) {
    console.log("submit")
    this.topicService.subscribe(topic).subscribe({
      next: (response) => {
        console.log("ok")
      },
      error: error => console.log(error)
    });
  }
}
