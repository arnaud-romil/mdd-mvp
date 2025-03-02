import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { UserService } from "./user.service";
import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { firstValueFrom } from "rxjs";
import { User } from "../models/user.interface";


describe('UserService', () => {

    let userService: UserService;
    let httpTesting: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]
        });
        userService = TestBed.inject(UserService);
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should be created', () => {
        expect(userService).toBeTruthy();
    });

    it('should add subscription', async () => {

        const topicId = 1;
        const user$ = userService.addSubscription(topicId);
        const userPromise = firstValueFrom(user$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: `api/users/me/topics/${topicId}`
        });

        const user: User = {
            username: 'Alice',
            email: 'alice@test.com',
            topics: [
                {
                    id: 1,
                    title: 'Java',
                    description: 'Java est un langage de programmation de haut niveau, basé sur les classes et orienté objet.'
                }
            ]
        };

        req.flush(user);

        expect(await userPromise).toEqual(user);
    });

    it('should remove subscription', async () => {

        const topicId = 1;
        const user$ = userService.removeSubscription(topicId);
        const userPromise = firstValueFrom(user$);

        const req = httpTesting.expectOne({
            method: 'DELETE',
            url: `api/users/me/topics/${topicId}`
        });

        const user: User = {
            username: 'Alice',
            email: 'alice@test.com',
            topics: []
        };

        req.flush(user);

        expect(await userPromise).toEqual(user);
    });
});