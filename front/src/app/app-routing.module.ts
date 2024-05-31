import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ConnectionComponent } from './pages/connection/connection.component';
import { PostListComponent } from './pages/post-list/post-list.component';
import { TopicListComponent } from './pages/topic-list/topic-list.component';
import { AccountComponent } from './pages/account/account.component';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'log/:type', component: ConnectionComponent },
  { path: 'posts', component: PostListComponent },
  { path: 'topics', component: TopicListComponent },
  { path: 'account', component: AccountComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
