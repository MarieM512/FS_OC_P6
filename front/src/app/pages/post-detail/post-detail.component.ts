import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Comment } from 'src/app/interfaces/response/comment.interface';
import { Post } from 'src/app/interfaces/response/post.interface';
import { CommentService } from 'src/app/services/comment.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss']
})
export class PostDetailComponent implements OnInit {

  post$!: Observable<Post>
  postId!: number

  commentForm = this.formBuilder.group({
    content: ['', [Validators.required]]
  })

  constructor(private route: ActivatedRoute, private postService: PostService, private formBuilder: FormBuilder, private commentService: CommentService) { }

  ngOnInit(): void {
    this.postId = +this.route.snapshot.params["id"]
    this.post$ = this.postService.getPostById(this.postId)
  }

  submit() {
    console.log("submit")
    const comment = this.commentForm.value as Comment
    this.commentService.create(this.postId, comment.content).subscribe({
      next: (response) => {
        console.log("ok")
      },
      error: error => console.log(error)
    });
  }
}
