import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForbiddenComponent } from './forbidden.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('ForbiddenComponent', () => {
  let component: ForbiddenComponent;
  let fixture: ComponentFixture<ForbiddenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForbiddenComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({}), // Mock route parameters
            snapshot: {
              paramMap: {
                get: (key: string) => null // Mock snapshot paramMap
              }
            }
          }
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ForbiddenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the error message', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('h2').textContent).toEqual('Accès refusé');
    expect(compiled.querySelector('p').textContent).toEqual("Vous n'avez pas accès à cette ressource.");
  });
});
