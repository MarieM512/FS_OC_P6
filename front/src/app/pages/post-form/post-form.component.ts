import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostRequest } from 'src/app/interfaces/request/postRequest.interface';
import { Topic } from 'src/app/interfaces/response/topic.interface';
import { PostService } from 'src/app/services/post.service';
import { TopicService } from 'src/app/services/topic.service';

@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.scss']
})
export class PostFormComponent implements OnInit {

  topics$ = this.topicService.getTopics()

  postForm = this.formBuilder.group({
    topicId: [0, [Validators.required]],
    subject: ['', [Validators.required]],
    content: ['', [Validators.required]]
  })

  constructor(private formBuilder: FormBuilder, private topicService: TopicService, private postService: PostService, private router: Router) { }

  ngOnInit(): void {
  }

  submit() {
    console.log('submit');
    const postRequest = this.postForm.value as PostRequest
    console.log(postRequest)
    this.postService.create(postRequest).subscribe({
      next: (response) => {
        this.router.navigate(['/posts'])
      },
      error: error => console.log(error)
    });
  }
}
