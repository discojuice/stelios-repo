import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

type LetterState = 'correct' | 'present' | 'absent' | '';
type Language = 'greek' | 'slovenian' | 'english';

@Component({
  selector: 'app-wordl',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wordl.component.html',
  styleUrls: ['./wordl.component.css']
})
export class WordlComponent implements OnInit {
  @ViewChild('gameBoard') gameBoard!: ElementRef<HTMLDivElement>;

  readonly wordLength = 5;
  readonly maxTries = 6;

  selectedLanguage: Language = 'greek';

  answerFiles: Record<Language, string> = {
    greek: 'assets/wordl/greek-words.json',
    slovenian: 'assets/wordlslovenian-words.json',
    english: 'assets/wordl/english-words.json'
  };

  guessFiles: Record<Language, string> = {
    greek: 'assets/wordl/greek-words-guess.json',
    slovenian: 'assets/wordl/slovenian-words-guess.json',
    english: 'assets/wordl/english-words-guess.json'
  };

  words: string[] = [];
  allowedGuesses: string[] = [];
  secretWord = '';

  currentGuess = '';
  currentRow = 0;
  shakeRow: number | null = null;

  guesses: string[] = Array(this.maxTries).fill('');
  states: LetterState[][] = Array.from({ length: this.maxTries }, () =>
    Array(this.wordLength).fill('')
  );

  message = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    const savedLanguage = localStorage.getItem('wordle-language') as Language | null;

    if (savedLanguage === 'greek' || savedLanguage === 'slovenian' || savedLanguage === 'english') {
      this.selectedLanguage = savedLanguage;
    }

    this.loadGame();
  }

  focusBoard(): void {
    setTimeout(() => {
      this.gameBoard?.nativeElement.focus();
    }, 0);
  }

  handleKeyDown(event: KeyboardEvent): void {
    if (this.isGameOver()) return;

    const key = event.key;

    if (key === 'Enter') {
      event.preventDefault();
      this.submitGuess();
      return;
    }

    if (key === 'Backspace') {
      event.preventDefault();
      this.currentGuess = this.currentGuess.slice(0, -1);
      this.message = '';
      return;
    }

    if (key.length === 1 && this.currentGuess.length < this.wordLength) {
      const letter = key.toUpperCase();

      if (this.isValidLetter(letter)) {
        event.preventDefault();
        this.currentGuess += letter;
        this.message = '';
      }
    }
  }

  isValidLetter(letter: string): boolean {
    if (this.selectedLanguage === 'greek') {
      return /^[Α-ΩΪΫ]$/.test(letter);
    }

    return /^[A-ZČŠŽ]$/.test(letter);
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
        this.focusBoard();
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

    if (!this.secretWord || this.isGameOver()) return;

    if (guess.length !== this.wordLength) {
      this.message = 'Word must be 5 letters.';
      this.triggerShake();
      return;
    }

    if (this.guesses.includes(guess)) {
      this.message = 'You already tried this word.';
      this.triggerShake();
      return;
    }

    if (!this.allowedGuesses.includes(guess)) {
      this.message = 'This word is not in the dictionary.';
      this.triggerShake();
      return;
    }

    this.guesses[this.currentRow] = guess;
    this.states[this.currentRow] = this.checkGuess(guess);
    this.currentRow++;        // ✅ always increment first
    this.currentGuess = '';   // ✅ always clear

    if (guess === this.secretWord) {
      this.message = 'You won!';
      return;                 // currentRow now points past the winning row
    }                         // so getDisplayLetter reads from guesses[] correctly

    if (this.currentRow === this.maxTries) {
      this.message = `Game over. Word was ${this.secretWord}.`;
    }
  }


  triggerShake(): void {
    this.shakeRow = this.currentRow;

    setTimeout(() => {
      this.shakeRow = null;
    }, 700);
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

  // getDisplayLetter(row: number, col: number): string {
  //   if (row === this.currentRow) {
  //     return this.currentGuess[col] || '';
  //   }

  //   return this.getLetter(row, col);
  // }

  getDisplayLetter(rowIndex: number, colIndex: number): string {
    // Show submitted guesses from the guesses array
    if (rowIndex < this.currentRow) {
      return this.guesses[rowIndex][colIndex] || '';
    }
    // Show current in-progress guess (only if game is still active)
    if (rowIndex === this.currentRow && !this.isGameOver()) {
      return this.currentGuess[colIndex] || '';
    }
    return '';
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
    this.shakeRow = null;
    this.guesses = Array(this.maxTries).fill('');
    this.states = Array.from({ length: this.maxTries }, () =>
      Array(this.wordLength).fill('')
    );
    this.message = '';

    this.focusBoard();
  }
}