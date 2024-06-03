import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.scss']
})
export class PostFormComponent implements OnInit {

  postForm = this.formBuilder.group({
    topic: ['', [Validators.required]],
    subject: ['', [Validators.required]],
    content: ['', [Validators.required]]
  })

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
  }

  submit() {
    console.log('submit');
  }

}
