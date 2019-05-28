import { IMark } from 'app/shared/model/mark.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IBoard {
    id?: number;
    width?: number;
    height?: number;
    marks?: IMark[];
    players?: IPlayer[];
}

export class Board implements IBoard {
    constructor(public id?: number, public width?: number, public height?: number, public marks?: IMark[], public players?: IPlayer[]) {}
}
