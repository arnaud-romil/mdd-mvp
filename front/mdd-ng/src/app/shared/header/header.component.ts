import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatButtonModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {

  showHeader: boolean = false;
  showLinks: boolean = false;
  isMobile: boolean = false;
  private routeSub!: Subscription;

  constructor(
    private readonly router: Router,
  ) { }

  ngOnInit(): void {

    this.updateHeader(this.router.url);
    this.updateIsMobile();

    this.routeSub = this.router.events.subscribe(
      event => {
        if (event instanceof NavigationEnd) {
          this.updateHeader(event.urlAfterRedirects);
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }

  updateHeader(currentRoute: string) {
    const hiddenRoutes = ['/'];
    const noLinksRoutes = ['/login', '/register'];

    this.showHeader = !hiddenRoutes.includes(currentRoute);
    this.showLinks = this.showHeader && (!noLinksRoutes.includes(currentRoute));

  }

  @HostListener('window:resize', [])
  updateIsMobile(): void {
    this.isMobile = window.innerWidth < 768;
    this.updateHeader(this.router.url);
  }

}
