import { NgModule } from '@angular/core';

import { ProjectTSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ProjectTSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ProjectTSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ProjectTSharedCommonModule {}
