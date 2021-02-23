import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TableComponent} from './table/table.component';
import {PaginationComponent} from './pagination/pagination.component';


@NgModule({
  declarations: [TableComponent, PaginationComponent],
  imports: [
    CommonModule
  ],
  exports: [
    TableComponent, PaginationComponent
  ]
})
export class SharedModule {
}
