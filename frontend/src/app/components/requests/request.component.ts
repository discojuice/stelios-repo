import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RequestService } from '../../service/request.service';
import { RequestItem } from '../../models/request';


@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './request.component.html',
  styleUrl: './request.component.css'
})
export class RequestsComponent implements OnInit {

  requests: RequestItem[] = [];

  form: RequestItem = {
    question: '',
    answer: '',
    createdOn: new Date(),
    category: '',
    department: '',
    requestNo: ''
  };

  editingId: number | null = null;

  searchQuestion = '';
  searchAnswer = '';
  searchId = '';

  message = '';
  error = '';

  constructor(
    private requestService: RequestService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.requestService.getAllRequests().subscribe({
      next: data => {
        this.requests = [...data];
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load requests.';
        this.cdr.detectChanges();
      }
    });
  }

  searchRequests(): void {
    this.clearMessages();

    const question = this.searchQuestion.trim();
    const answer = this.searchAnswer.trim();

    if (!question && !answer) {
      this.loadRequests();
      return;
    }

    this.requestService.searchRequests(question, answer).subscribe({
      next: data => {
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
    this.searchQuestion = '';
    this.searchAnswer = '';
    this.loadRequests();
  }

  createRequest(): void {
    this.clearMessages();

    const payload = {
      question: this.form.question.trim(),
      answer: this.form.answer.trim(),
      category: this.form.category.trim(),
      department: this.form.department.trim(),
      requestNo: this.form.requestNo.trim()
    };

    if (!payload.question || !payload.answer) {
      this.error = 'Question and answer are required.';
      return;
    }

    this.requestService.createRequest(payload).subscribe({
      next: () => {
        this.message = 'Request created successfully.';
        this.resetForm();
        this.loadRequests();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to create request.';
        this.cdr.detectChanges();
      }
    });
  }

  editRequest(item: RequestItem): void {
    this.editingId = item.id ?? null;

    this.form = {
      question: item.question,
      answer: item.answer,
      createdOn: item.createdOn,
      category: item.category,
      department: item.department,
      requestNo: item.requestNo
    };

    this.clearMessages();
  }

  updateRequest(): void {
    if (this.editingId === null) return;

    this.clearMessages();

    const payload = {
      question: this.form.question.trim(),
      answer: this.form.answer.trim(),
      category: this.form.category.trim(),
      department: this.form.department.trim(),
      requestNo: this.form.requestNo.trim()
    };

    if (!payload.question || !payload.answer) {
      this.error = 'Question and answer are required.';
      return;
    }

    this.requestService.updateRequest(this.editingId, payload).subscribe({
      next: () => {
        this.message = 'Request updated successfully.';
        this.cancelEdit();
        this.loadRequests();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to update request.';
        this.cdr.detectChanges();
      }
    });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.resetForm();
  }

  deleteRequest(id?: number): void {
    if (!id) return;

    this.clearMessages();

    this.requestService.deleteRequest(id).subscribe({
      next: () => {
        this.message = `Request ${id} deleted successfully.`;

        if (this.editingId === id) {
          this.cancelEdit();
        }

        this.loadRequests();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to delete request.';
        this.cdr.detectChanges();
      }
    });
  }

  private resetForm(): void {
    this.form = {
      question: '',
      answer: '',
      createdOn: new Date(),
      category: '',
      department: '',
      requestNo: ''
    };
  }

  private clearMessages(): void {
    this.message = '';
    this.error = '';
  }
}