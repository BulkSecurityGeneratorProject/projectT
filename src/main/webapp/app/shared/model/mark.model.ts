import { IBoard } from 'app/shared/model/board.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IMark {
    id?: number;
    x?: number;
    y?: number;
    board?: IBoard;
    player?: IPlayer;
}

export class Mark implements IMark {
    constructor(public id?: number, public x?: number, public y?: number, public board?: IBoard, public player?: IPlayer) {}
}
