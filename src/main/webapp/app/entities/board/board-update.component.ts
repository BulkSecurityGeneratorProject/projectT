import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IBoard } from 'app/shared/model/board.model';
import { BoardService } from './board.service';

@Component({
    selector: 'jhi-board-update',
    templateUrl: './board-update.component.html'
})
export class BoardUpdateComponent implements OnInit {
    board: IBoard;
    isSaving: boolean;

    constructor(protected boardService: BoardService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ board }) => {
            this.board = board;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.board.id !== undefined) {
            this.subscribeToSaveResponse(this.boardService.update(this.board));
        } else {
            this.subscribeToSaveResponse(this.boardService.create(this.board));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoard>>) {
        result.subscribe((res: HttpResponse<IBoard>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
