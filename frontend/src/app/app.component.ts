import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';

interface RequestItem {
  id?: number;
  question: string;
  answer: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent implements OnInit {

  activePage: 'home' | 'requests' | 'docs' = 'home';

  private apiUrl = 'http://localhost:8080/api/requests';

  requests: RequestItem[] = [];


constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}
  ngOnInit(): void {
  console.log('ngOnInit fired');
  this.activePage = 'requests';
  this.loadRequests();
}

  loadRequests(): void {
  console.log('loadRequests called');

  this.http.get<RequestItem[]>(this.apiUrl).subscribe({
    next: (data) => {
      console.log('requests loaded from backend:', data);
      console.log('loaded length:', data.length);

      this.requests = [...data];   // create a fresh array reference
      console.log('requests after assignment:', this.requests);

      this.cdr.detectChanges();    // force template refresh
    },
    error: (err) => {
      console.error('load failed', err);
      this.error = 'Failed to load requests.';
      this.cdr.detectChanges();
    }
  });
}

searchRequests(): void {
  this.clearMessages();

  const id = this.searchId.trim();
  const question = this.searchQuestion.trim();
  const answer = this.searchAnswer.trim();

  if (!id && !question && !answer) {
    this.loadRequests();
    return;
  }

  if (id) {
    this.http.get<RequestItem>(`${this.apiUrl}/${id}`).subscribe({
      next: (data) => {
        this.requests = [data];
        this.cdr.detectChanges();
      },
      error: () => {
        this.requests = [];
        this.error = 'Request not found.';
        this.cdr.detectChanges();
      }
    });
    return;
  }

  let params = new HttpParams();
  if (question) params = params.set('question', question);
  if (answer) params = params.set('answer', answer);

  this.http.get<RequestItem[]>(`${this.apiUrl}/search`, { params }).subscribe({
    next: (data) => {
      this.requests = [...data];
      this.cdr.detectChanges();
    },
    error: () => {
      this.requests = [];
      this.error = 'Search failed.';
      this.cdr.detectChanges();
    }
  });
}

resetSearch(): void {
  console.log('resetSearch called');
  this.searchId = '';
  this.searchQuestion = '';
  this.searchAnswer = '';
  this.loadRequests();
}

  form: RequestItem = {
    question: '',
    answer: ''
  };

  editingId: number | null = null;

  searchQuestion = '';
  searchAnswer = '';
  searchId = '';

  message = '';
  error = '';


  goToHome(): void {
    this.activePage = 'home';
    this.loadRequests();
    }

  goToRequests(): void {
    this.activePage = 'requests';
    this.loadRequests();
  }

  goToDocs(): void {
    this.activePage = 'docs';
  }


  createRequest(): void {
    this.clearMessages();

    const payload = {
      question: this.form.question.trim(),
      answer: this.form.answer.trim()
    };

    if (!payload.question || !payload.answer) {
      this.error = 'Question and answer are required.';
      return;
    }

    this.http.post(this.apiUrl, payload).subscribe({
      next: () => {
        this.message = 'Request created successfully.';
        this.form = { question: '', answer: '' };
        this.loadRequests();
        this.activePage = 'requests';
      },
      error: () => this.error = 'Failed to create request.'
    });
  }

  editRequest(item: RequestItem): void {
    this.editingId = item.id ?? null;
    this.form = {
      question: item.question,
      answer: item.answer
    };
    this.activePage = 'requests';
    this.clearMessages();
  }

  updateRequest(): void {
    if (this.editingId == null) return;

    this.clearMessages();

    const payload = {
      question: this.form.question.trim(),
      answer: this.form.answer.trim()
    };

    if (!payload.question || !payload.answer) {
      this.error = 'Question and answer are required.';
      return;
    }

    this.http.put(`${this.apiUrl}/${this.editingId}`, payload).subscribe({
      next: () => {
        this.message = 'Request updated successfully.';
        this.cancelEdit();
        this.loadRequests();
      },
      error: () => this.error = 'Failed to update request.'
    });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.form = { question: '', answer: '' };
  }

  deleteRequest(id?: number): void {
    if (!id) return;

    this.clearMessages();

    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => {
        this.message = `Request ${id} deleted successfully.`;
        if (this.editingId === id) {
          this.cancelEdit();
        }
        this.loadRequests();
      },
      error: () => this.error = 'Failed to delete request.'
    });
  }

  private clearMessages(): void {
    this.message = '';
    this.error = '';
  }
}