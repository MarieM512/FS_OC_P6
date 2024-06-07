import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { map } from 'rxjs';
import { Post } from 'src/app/interfaces/response/post.interface';
import { AuthService } from 'src/app/services/auth.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent implements OnInit {

  posts$ = this.postService.getPosts()

  constructor(private router: Router, private postService: PostService, private authService: AuthService) { }

  ngOnInit(): void {
    this.posts$.subscribe({
      next(value) {
        value.forEach((post) => {
          console.log(post)
          console.log(post.created_at)
        })
      },
    })
  }

  create() {
    this.router.navigate(['/posts/create'])
  }
}
