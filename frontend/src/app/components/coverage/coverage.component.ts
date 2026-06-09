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
  apiUrl = 'http://localhost:8080/api/coverage'; // ← UPDATE THIS IF NEEDED

  constructor(
    private http: HttpClient,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.checkApiHealth();
  }

  /**
   * Check if API is running
   */
  checkApiHealth(): void {
    this.http.get(`${this.apiUrl}/health`)
      .subscribe({
        next: () => {
          this.loadCoverageStatus();
          this.loadCoverageReport();
        },
        error: (error) => {
          this.errorMessage = 'Backend API is not running. Please start the backend server on http://localhost:8080';
          this.isLoading = false;
          console.error('API Health Check Error:', error);
        }
      });
  }

  /**
   * Load coverage status
   */
  loadCoverageStatus(): void {
    this.http.get(`${this.apiUrl}/status`)
      .subscribe({
        next: (status: any) => {
          this.coverageStatus = status;
        },
        error: (error) => {
          console.error('Error loading coverage status:', error);
          this.errorMessage = 'Failed to load coverage status. Make sure the backend is running.';
        }
      });
  }

  /**
   * Load coverage report HTML
   */
  loadCoverageReport(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.http.get(`${this.apiUrl}`, { responseType: 'text' })
      .subscribe({
        next: (html: string) => {
          this.coverageHtml = this.sanitizer.bypassSecurityTrustResourceUrl(
            'data:text/html;charset=UTF-8,' + encodeURIComponent(html)
          );
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading coverage report:', error);
          this.isLoading = false;
          
          if (error.status === 404) {
            this.errorMessage = 'Coverage report not found. Run: mvn clean test';
          } else if (error.status === 0) {
            this.errorMessage = 'Cannot connect to backend. Is it running on http://localhost:8080?';
          } else {
            this.errorMessage = `Failed to load report (Error ${error.status})`;
          }
        }
      });
  }

  /**
   * Refresh the report
   */
  refreshReport(): void {
    this.loadCoverageStatus();
    this.loadCoverageReport();
  }
}