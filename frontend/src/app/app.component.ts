import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogComponent } from './components/blog/blog.component';
import { RequestsComponent } from './components/requests/request.component';
import { BioComponent } from './components/bio/bio.component';
import { ProjectComponent } from './components/project/project.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, BlogComponent, RequestsComponent, BioComponent, ProjectComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  activePage: 'blog' | 'requests' | 'bio' | 'project' | null = null;

  goToBlog(): void {
    this.activePage = 'blog';
  }

  goToRequests(): void {
    this.activePage = 'requests';
  }

  goToBio(): void {
    this.activePage = 'bio';
  }

    goToProject(): void {
    this.activePage = 'project';
  }
}