import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

type LetterState = 'correct' | 'present' | 'absent' | '';

@Component({
  selector: 'app-tutorials',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './tutorials.component.html',
  styleUrls: ['./tutorials.component.css']
})
export class TutorialsComponent implements OnInit {
  readonly wordLength = 5;
  readonly maxTries = 6;

  words: string[] = [];
  secretWord = '';

  currentGuess = '';
  currentRow = 0;

  guesses: string[] = Array(this.maxTries).fill('');
  states: LetterState[][] = Array.from({ length: this.maxTries }, () =>
    Array(this.wordLength).fill('')
  );

  message = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<string[]>('assets/slovenian-words.json').subscribe(words => {
      this.words = words;
      this.secretWord = this.getRandomWord();
    });
  }

  getRandomWord(): string {
    return this.words[Math.floor(Math.random() * this.words.length)];
  }

  submitGuess(): void {
    const guess = this.currentGuess.toUpperCase();

    if (!this.secretWord) return;

    if (guess.length !== this.wordLength) {
      this.message = 'Word must be 5 letters.';
      return;
    }

    if (!this.words.includes(guess)) {
      this.message = 'This word is not in the Slovenian dictionary.';
      return;
    }

    this.guesses[this.currentRow] = guess;
    this.states[this.currentRow] = this.checkGuess(guess);

    if (guess === this.secretWord) {
      this.message = 'You won!';
      this.currentGuess = '';
      return;
    }

    this.currentRow++;
    this.currentGuess = '';

    if (this.currentRow === this.maxTries) {
      this.message = `Game over. Word was ${this.secretWord}.`;
    } else {
      this.message = '';
    }
  }

  checkGuess(guess: string): LetterState[] {
    const result: LetterState[] = Array(this.wordLength).fill('absent');
    const secretLetters = this.secretWord.split('');

    for (let i = 0; i < this.wordLength; i++) {
      if (guess[i] === secretLetters[i]) {
        result[i] = 'correct';
        secretLetters[i] = '';
      }
    }

    for (let i = 0; i < this.wordLength; i++) {
      if (result[i] === 'correct') continue;

      const index = secretLetters.indexOf(guess[i]);

      if (index !== -1) {
        result[i] = 'present';
        secretLetters[index] = '';
      }
    }

    return result;
  }

  getLetter(row: number, col: number): string {
    return this.guesses[row]?.[col] || '';
  }

  isGameOver(): boolean {
    return this.message === 'You won!' || this.currentRow === this.maxTries;
  }

  restart(): void {
    this.secretWord = this.getRandomWord();
    this.currentGuess = '';
    this.currentRow = 0;
    this.guesses = Array(this.maxTries).fill('');
    this.states = Array.from({ length: this.maxTries }, () =>
      Array(this.wordLength).fill('')
    );
    this.message = '';
  }
}