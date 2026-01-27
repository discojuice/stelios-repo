import { Component, OnInit,ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';



type Author = { id?: number; name: string };
type Book = { id?: number; title: string; authorId: number; authorName: string };
type BookRequest = { title: string; authorId: number | null };

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
})


export class AppComponent implements OnInit {
  ngOnInit(): void {
    // this.loadAuthors();
  }

  private authorsUrl = 'http://localhost:8080/api/authors';
  private booksUrl = 'http://localhost:8080/api/books';

  authors: Author[] = [];
  books: Book[] = [];

  authorForm: Author = { name: '' };
  authorEditingId: number | null = null;

  bookForm: BookRequest = { title: '', authorId: null };
  bookEditingId: number | null = null;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  loadAuthors() {
  console.log('clicked loadAuthors');
  this.http.get<Author[]>(this.authorsUrl).subscribe(data => {
    console.log('response', data);
    this.authors = [...data];
    this.cdr.detectChanges(); // ✅ forces UI update immediately
  });
}

  

  saveAuthor() {
    if (!this.authorForm.name.trim()) return;

    const req = { name: this.authorForm.name };

    if (this.authorEditingId === null) {
      this.http.post(this.authorsUrl, req).subscribe(() => this.loadAuthors());
    } else {
      this.http.put(`${this.authorsUrl}/${this.authorEditingId}`, req)
        .subscribe(() => this.loadAuthors());
    }

    this.cancelAuthorEdit();
  }

  editAuthor(a: Author) {
    this.authorEditingId = a.id ?? null;
    this.authorForm = { name: a.name };
  }

  cancelAuthorEdit() {
    this.authorEditingId = null;
    this.authorForm = { name: '' };
  }

  deleteAuthor(id?: number) {
    if (!id) return;
    this.http.delete(`${this.authorsUrl}/${id}`).subscribe(() => this.loadAuthors());
  }

  // loadBooks() {
  //   this.http.get<Book[]>(this.booksUrl).subscribe(r => this.books = r);
  // }

  loadBooks() {
  console.log('clicked loadAuthors');
  this.http.get<Book[]>(this.booksUrl).subscribe(data => {
    console.log('response', data);
    this.books = [...data];
    this.cdr.detectChanges(); // ✅ forces UI update immediately
  });
}

  saveBook() {
    if (!this.bookForm.title || this.bookForm.authorId === null) return;

    const payload = {
      title: this.bookForm.title,
      authorId: this.bookForm.authorId
    };

    if (this.bookEditingId === null) {
      this.http.post(this.booksUrl, payload).subscribe(() => this.loadBooks());
    } else {
      this.http.put(`${this.booksUrl}/${this.bookEditingId}`, payload)
        .subscribe(() => this.loadBooks());
    }

    this.cancelBookEdit();
  }

  editBook(b: Book) {
    this.bookEditingId = b.id ?? null;
    this.bookForm = { title: b.title, authorId: b.authorId };
  }

  cancelBookEdit() {
    this.bookEditingId = null;
    this.bookForm = { title: '', authorId: null };
  }

  deleteBook(id?: number) {
    if (!id) return;
    this.http.delete(`${this.booksUrl}/${id}`).subscribe(() => this.loadBooks());
  }
}
