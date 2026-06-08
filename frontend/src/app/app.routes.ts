import { Routes } from '@angular/router';
import { BlogComponent } from './components/blog/blog.component';
import { BioComponent } from './components/bio/bio.component';


export const routes: Routes = [
  {
    path: 'blog',
    component: BlogComponent
  },
  { path: 'bio', 
    component: BioComponent }


];