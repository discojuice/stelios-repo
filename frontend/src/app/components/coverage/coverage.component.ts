import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-coverage',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './coverage.component.html',
  styleUrls: ['./coverage.component.css']
})
export class CoverageComponent implements OnInit {
  coverageHtml: SafeResourceUrl | null = null;
  isLoading = true;
  errorMessage: string | null = null;
  coverageStatus: any = null;

  constructor(
    private http: HttpClient,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.loadCoverageStatus();
    this.loadCoverageReport();
  }

  loadCoverageStatus(): void {
    this.http.get('/api/coverage/status')
      .subscribe({
        next: (status: any) => {
          this.coverageStatus = status;
        },
        error: (error) => {
          console.error('Error loading coverage status:', error);
        }
      });
  }

  loadCoverageReport(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.http.get('/api/coverage', { responseType: 'text' })
      .subscribe({
        next: (html: string) => {
          this.coverageHtml = this.sanitizer.bypassSecurityTrustResourceUrl(
            'data:text/html;charset=UTF-8,' + encodeURIComponent(html)
          );
          this.isLoading = false;
        },
        error: (error) => {
          this.errorMessage = 'Failed to load coverage report. Please run tests first: mvn clean test';
          this.isLoading = false;
          console.error('Error loading coverage:', error);
        }
      });
  }

  refreshReport(): void {
    this.loadCoverageReport();
    this.loadCoverageStatus();
  }
}