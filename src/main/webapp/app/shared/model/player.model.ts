import { IMark } from 'app/shared/model/mark.model';
import { IBoard } from 'app/shared/model/board.model';

export const enum PlayerType {
    CROSS = 'CROSS',
    CIRCLE = 'CIRCLE'
}

export interface IPlayer {
    id?: number;
    name?: string;
    type?: PlayerType;
    marks?: IMark[];
    board?: IBoard;
}

export class Player implements IPlayer {
    constructor(public id?: number, public name?: string, public type?: PlayerType, public marks?: IMark[], public board?: IBoard) {}
}
