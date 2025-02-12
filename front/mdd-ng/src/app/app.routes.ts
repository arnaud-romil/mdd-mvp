import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { AuthGardService } from './core/auth-gard.service';
import { LoginComponent } from './pages/login/login.component';
import { PostDetailComponent } from './pages/post-detail/post-detail.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'feed', component: FeedComponent, canActivate: [AuthGardService] },
    { path: 'posts/:id', component: PostDetailComponent, canActivate: [AuthGardService] },
];
