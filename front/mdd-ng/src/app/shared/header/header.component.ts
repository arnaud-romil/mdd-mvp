import { CommonModule } from '@angular/common';
import { Component, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Subscription } from 'rxjs';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MatSidenavModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit, OnDestroy {

  @ViewChild('sidenav') sidenav!: MatSidenav;

  showHeader: boolean = false;
  showLinks: boolean = false;
  isMobile: boolean = false;
  private routeSub!: Subscription;

  constructor(
    private readonly router: Router,
  ) { }

  ngOnInit(): void {

    this.updateIsMobile();
    this.updateHeader(this.router.url);

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
    const specialRoutes = ['/login', '/register'];

    console.log(`isMobile: ${this.isMobile}`);

    this.showHeader = !this.isHomePage(currentRoute) && !(this.isMobile &&
      specialRoutes.includes(currentRoute));

    this.showLinks = this.showHeader && !specialRoutes.includes(currentRoute) &&
      !this.isMobile;
  }

  @HostListener('window:resize', [])
  updateIsMobile(): void {
    this.isMobile = window.innerWidth < 768;
    this.updateHeader(this.router.url);
  }

  toggleSidenav() {
    this.sidenav.toggle();
  }

  private isHomePage(currentRoute: string): boolean {
    return currentRoute === '/';
  }
}
