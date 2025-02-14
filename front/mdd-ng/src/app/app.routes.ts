import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { AuthGardService } from './core/auth-gard.service';
import { LoginComponent } from './pages/login/login.component';
import { PostDetailComponent } from './pages/post-detail/post-detail.component';
import { TopicsComponent } from './pages/topics/topics.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'feed', component: FeedComponent, canActivate: [AuthGardService] },
    { path: 'posts/:id', component: PostDetailComponent, canActivate: [AuthGardService] },
    { path: 'topics', component: TopicsComponent, canActivate: [AuthGardService] },
    { path: 'create-post', component: CreatePostComponent, canActivate: [AuthGardService] },
];
