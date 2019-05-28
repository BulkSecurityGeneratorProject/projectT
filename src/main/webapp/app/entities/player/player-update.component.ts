import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';
import { IBoard } from 'app/shared/model/board.model';
import { BoardService } from 'app/entities/board';

@Component({
    selector: 'jhi-player-update',
    templateUrl: './player-update.component.html'
})
export class PlayerUpdateComponent implements OnInit {
    player: IPlayer;
    isSaving: boolean;

    boards: IBoard[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected playerService: PlayerService,
        protected boardService: BoardService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ player }) => {
            this.player = player;
        });
        this.boardService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IBoard[]>) => mayBeOk.ok),
                map((response: HttpResponse<IBoard[]>) => response.body)
            )
            .subscribe((res: IBoard[]) => (this.boards = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.player.id !== undefined) {
            this.subscribeToSaveResponse(this.playerService.update(this.player));
        } else {
            this.subscribeToSaveResponse(this.playerService.create(this.player));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>) {
        result.subscribe((res: HttpResponse<IPlayer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
