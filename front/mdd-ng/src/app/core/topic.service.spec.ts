import { TestBed } from '@angular/core/testing';
import { TopicService } from './topic.service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
import { Topic } from '../models/topic.interface';


describe('TopicService', () => {

    let topicService: TopicService;
    let httpTesting: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]
        });
        topicService = TestBed.inject(TopicService);
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should be created', () => {
        expect(topicService).toBeTruthy();
    });

    it('should return topics', async () => {

        const topics$ = topicService.getTopics();
        const topicsPromise = firstValueFrom(topics$);

        const req = httpTesting.expectOne({
            method: 'GET',
            url: 'api/topics'

        });

        const expectedTopics: Topic[] = [
            { id: 1, title: 'Java', description: 'Java est un langage de programmation de haut niveau, basé sur les classes et orienté objet.' },
            { id: 2, title: 'Spring', description: 'Spring est un framework puissant et riche en fonctionnalités pour développer des applications Java.' },
            { id: 3, title: 'Python', description: 'Python est un langage de programmation polyvalent et de haut niveau, connu pour sa simplicité et sa lisibilité.' }
        ];

        req.flush(expectedTopics);

        expect(await topicsPromise).toEqual(expectedTopics);
    });


});