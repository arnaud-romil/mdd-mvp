import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { FeedComponent } from './pages/feed/feed.component';
import { AuthGardService } from './core/auth-gard.service';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'feed', component: FeedComponent, canActivate: [AuthGardService] },
    { path: 'login', loadComponent: () => import('./features/auth/components/login/login.component').then(m => m.LoginComponent) },

];
