import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
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
  breakpoint!: number

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

    this.breakpoint = (window.innerWidth <= 500) ? 1 : 2
  }

  create() {
    this.router.navigate(['/posts/create'])
  }

  onResize(event: any) {
    this.breakpoint = (event.target.innerWidth <= 500) ? 1 : 2
  }
}
