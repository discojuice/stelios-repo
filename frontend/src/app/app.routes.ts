import { BlogComponent } from './components/blog/blog.component';
import { BioComponent } from './components/bio/bio.component';
import { CoverageComponent } from './components/coverage/coverage.component';
import { TutorialsComponent } from './components/tutorials/tutorials.component'; import { RequestsComponent } from './components/requests/request.component';
import { ProjectComponent } from './components/project/project.component';
import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { WordlComponent } from './components/wordl/wordl.component';
import { ConverterComponent } from './components/converter/converter.component';


export const routes: Routes = [
  { path: '', redirectTo: 'project', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'blog', component: BlogComponent },
  { path: 'requests', component: RequestsComponent },
  { path: 'games', component: WordlComponent },
  { path: 'tutorials', component: TutorialsComponent },
  { path: 'project', component: ProjectComponent },
  { path: 'bio', component: BioComponent },
  { path: 'coverage', component: CoverageComponent },
  { path: 'converter', component: ConverterComponent },
  { path: '**', redirectTo: 'home' }
];