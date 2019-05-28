import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMark } from 'app/shared/model/mark.model';
import { AccountService } from 'app/core';
import { MarkService } from './mark.service';

@Component({
    selector: 'jhi-mark',
    templateUrl: './mark.component.html'
})
export class MarkComponent implements OnInit, OnDestroy {
    marks: IMark[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected markService: MarkService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.markService
            .query()
            .pipe(
                filter((res: HttpResponse<IMark[]>) => res.ok),
                map((res: HttpResponse<IMark[]>) => res.body)
            )
            .subscribe(
                (res: IMark[]) => {
                    this.marks = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMarks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMark) {
        return item.id;
    }

    registerChangeInMarks() {
        this.eventSubscriber = this.eventManager.subscribe('markListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
