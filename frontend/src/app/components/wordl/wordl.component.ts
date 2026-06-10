import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

type LetterState = 'correct' | 'present' | 'absent' | '';
type Language = 'greek' | 'slovenian';

@Component({
  selector: 'app-wordl',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './wordl.component.html',
  styleUrls: ['./wordl.component.css']
})
export class WordlComponent implements OnInit {
  readonly wordLength = 5;
  readonly maxTries = 6;

  selectedLanguage: Language = 'slovenian';

  answerFiles: Record<Language, string> = {
    greek: 'assets/greek-words.json',
    slovenian: 'assets/slovenian-words.json'
  };

  guessFiles: Record<Language, string> = {
    greek: 'assets/greek-words-guess.json',
    slovenian: 'assets/slovenian-words-guess.json'
  };

  words: string[] = [];
  allowedGuesses: string[] = [];
  secretWord = '';
  shake = false;

  currentGuess = '';
  currentRow = 0;

  guesses: string[] = Array(this.maxTries).fill('');
  states: LetterState[][] = Array.from({ length: this.maxTries }, () =>
    Array(this.wordLength).fill('')
  );

  message = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    const savedLanguage = localStorage.getItem('wordle-language') as Language | null;

    if (savedLanguage === 'greek' || savedLanguage === 'slovenian') {
      this.selectedLanguage = savedLanguage;
    }

    this.loadGame();
  }

  shakeRow: number | null = null;


  // triggerShake(): void {
  //   this.shake = true;

  //   setTimeout(() => {
  //     this.shake = false;
  //   }, 400);
  // }



  triggerShake(): void {
    this.shakeRow = null;

    setTimeout(() => {
      this.shakeRow = this.currentRow;
    }, 0);

    setTimeout(() => {
      this.shakeRow = null;
    }, 500);
  }

  loadGame(): void {
    this.http.get<string[]>(this.answerFiles[this.selectedLanguage]).subscribe(answerWords => {
      this.words = answerWords.map(word => word.toUpperCase());

      this.http.get<string[]>(this.guessFiles[this.selectedLanguage]).subscribe(guessWords => {
        this.allowedGuesses = guessWords.map(word => word.toUpperCase());

        this.words.forEach(word => {
          if (!this.allowedGuesses.includes(word)) {
            this.allowedGuesses.push(word);
          }
        });

        this.secretWord = this.getRandomWord();
        this.restart(false);
      });
    });
  }

  changeLanguage(language: Language): void {
    if (this.selectedLanguage === language) return;

    this.selectedLanguage = language;
    localStorage.setItem('wordle-language', language);
    this.loadGame();
  }


  getRandomWord(): string {
    return this.words[Math.floor(Math.random() * this.words.length)];
  }

  submitGuess(): void {
    const guess = this.currentGuess.toUpperCase();

    if (this.guesses.includes(guess)) {
      this.message = 'You already tried this word.';
      this.triggerShake();

      return;
    }

    if (!this.secretWord || this.isGameOver()) return;

    if (guess.length !== this.wordLength) {
      this.message = 'Word must be 5 letters.';
      this.triggerShake();
      return;
    }

    if (!this.allowedGuesses.includes(guess)) {
      this.message =
        this.selectedLanguage === 'greek'
          ? 'This word is not in the Greek dictionary.'
          : 'This word is not in the Slovenian dictionary.';
      this.triggerShake();
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
      this.triggerShake();
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

  restart(newWord: boolean = true): void {
    if (newWord && this.words.length > 0) {
      this.secretWord = this.getRandomWord();
    }

    this.currentGuess = '';
    this.currentRow = 0;
    this.guesses = Array(this.maxTries).fill('');
    this.states = Array.from({ length: this.maxTries }, () =>
      Array(this.wordLength).fill('')
    );
    this.message = '';
  }
}