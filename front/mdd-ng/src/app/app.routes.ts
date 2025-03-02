import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { AuthGuardService } from './core/auth-guard.service';
import { LoginComponent } from './pages/login/login.component';
import { PostDetailComponent } from './pages/post-detail/post-detail.component';
import { TopicsComponent } from './pages/topics/topics.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { RegisterComponent } from './pages/register/register.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ForbiddenComponent } from './pages/forbidden/forbidden.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'feed', component: FeedComponent, canActivate: [AuthGuardService] },
    { path: 'posts/:id', component: PostDetailComponent, canActivate: [AuthGuardService] },
    { path: 'topics', component: TopicsComponent, canActivate: [AuthGuardService] },
    { path: 'create-post', component: CreatePostComponent, canActivate: [AuthGuardService] },
    { path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService] },
    { path: 'forbidden', component: ForbiddenComponent, canActivate: [AuthGuardService] },
    { path: 'not-found', component: NotFoundComponent },
    { path: '**', redirectTo: '/not-found' }

];
