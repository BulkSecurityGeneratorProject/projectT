import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'board',
                loadChildren: './board/board.module#ProjectTBoardModule'
            },
            {
                path: 'mark',
                loadChildren: './mark/mark.module#ProjectTMarkModule'
            },
            {
                path: 'player',
                loadChildren: './player/player.module#ProjectTPlayerModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ProjectTEntityModule {}
