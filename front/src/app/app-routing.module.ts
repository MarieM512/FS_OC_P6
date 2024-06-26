import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ConnectionComponent } from './pages/connection/connection.component';
import { PostListComponent } from './pages/post-list/post-list.component';
import { TopicListComponent } from './pages/topic-list/topic-list.component';
import { AccountComponent } from './pages/account/account.component';
import { PostFormComponent } from './pages/post-form/post-form.component';
import { PostDetailComponent } from './pages/post-detail/post-detail.component';
import { AuthGuard } from './configuration/guard/auth.guard';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'log/:type', component: ConnectionComponent },
  { path: 'posts', component: PostListComponent, canActivate: [AuthGuard] },
  { path: 'topics', component: TopicListComponent, canActivate: [AuthGuard] },
  { path: 'account', component: AccountComponent, canActivate: [AuthGuard] },
  { path: 'posts/create', component: PostFormComponent, canActivate: [AuthGuard] },
  { path: 'posts/:id', component: PostDetailComponent, canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
