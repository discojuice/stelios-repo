import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-coverage',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './coverage.component.html',
  styleUrls: ['./coverage.component.css']
})
export class CoverageComponent implements OnInit {
  isLoading = true;
  readonly isLocal = !environment.production;
  errorMessage: string | null = null;
  coverageStatus: any = null;
  // apiUrl = 'http://localhost:8080/api/coverage';

  constructor(private http: HttpClient) { }
  coverageReportUrl = `${environment.apiUrl}/coverage-report`;

  ngOnInit(): void {
    this.checkApiHealth();
  }

  /**
   * Check if API is running
   */
  checkApiHealth(): void {
    console.log('Checking API health...');
    this.http.get(`${environment.apiUrl}/health`)
      .subscribe({
        next: (response) => {
          console.log('API Health OK:', response);
          this.loadCoverageStatus();
        },
        error: (error) => {
          console.error('API Health Check Error:', error);
          this.errorMessage = 'Backend API is not running. Please start: mvn spring-boot:run';
          this.isLoading = false;
        }
      });
  }


  /**
   * Load coverage status
   */
  loadCoverageStatus(): void {
    console.log('Loading coverage status...');
    this.http.get(`${environment.apiUrl}/status`)
      .subscribe({
        next: (status: any) => {
          console.log('Coverage status:', status);
          this.coverageStatus = status;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading coverage status:', error);
          this.errorMessage = 'Failed to load coverage status';
          this.isLoading = false;
        }
      });
  }

  /**
   * Refresh the status
   */
  refreshReport(): void {
    console.log('Refreshing status...');
    this.isLoading = true;
    this.errorMessage = null;
    this.loadCoverageStatus();
  }
}