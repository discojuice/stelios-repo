import { RouterModule, Routes } from '@angular/router';
import { BlogComponent } from './components/blog/blog.component';
import { BioComponent } from './components/bio/bio.component';
import { CoverageComponent } from './components/coverage/coverage.component';
import { NgModule } from '@angular/core';


export const routes: Routes = [
  {
    path: 'blog',
    component: BlogComponent
  },
  { path: 'bio', 
    component: BioComponent 
  },
  {
    path: 'coverage',
    component: CoverageComponent,
    data: { title: 'Coverage Report' }
  }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }