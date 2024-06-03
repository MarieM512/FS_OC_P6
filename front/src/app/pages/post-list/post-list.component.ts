import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  create() {
    this.router.navigate(['/posts/create'])
  }
}
