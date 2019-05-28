import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMark } from 'app/shared/model/mark.model';
import { MarkService } from './mark.service';
import { IBoard } from 'app/shared/model/board.model';
import { BoardService } from 'app/entities/board';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player';

@Component({
    selector: 'jhi-mark-update',
    templateUrl: './mark-update.component.html'
})
export class MarkUpdateComponent implements OnInit {
    mark: IMark;
    isSaving: boolean;

    boards: IBoard[];

    players: IPlayer[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected markService: MarkService,
        protected boardService: BoardService,
        protected playerService: PlayerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mark }) => {
            this.mark = mark;
        });
        this.boardService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IBoard[]>) => mayBeOk.ok),
                map((response: HttpResponse<IBoard[]>) => response.body)
            )
            .subscribe((res: IBoard[]) => (this.boards = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.playerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPlayer[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPlayer[]>) => response.body)
            )
            .subscribe((res: IPlayer[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.mark.id !== undefined) {
            this.subscribeToSaveResponse(this.markService.update(this.mark));
        } else {
            this.subscribeToSaveResponse(this.markService.create(this.mark));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IMark>>) {
        result.subscribe((res: HttpResponse<IMark>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBoardById(index: number, item: IBoard) {
        return item.id;
    }

    trackPlayerById(index: number, item: IPlayer) {
        return item.id;
    }
}
