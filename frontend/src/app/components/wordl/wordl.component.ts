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
        slovenian: 'assets/wordl/slovenian-words.json',
        english: 'assets/wordl/english-words.json'
    };

    guessFiles: Record<Language, string> = {
        greek: 'assets/wordl/greek-words-guess.json',
        slovenian: 'assets/wordl/slovenian-words-guess.json',
        english: 'assets/wordl/english-words-guess.json'
    };

    keyboardLayouts: Record<Language, string[][]> = {
        english: [
            ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P','⌫'],
            ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
            ['Z', 'X', 'C', 'V', 'B', 'N', 'M', 'ENTER']
        ],
        slovenian: [
            ['Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'I', 'O', 'P', 'Š','⌫'],
            ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Č'],
            ['Y', 'X', 'C', 'V', 'B', 'N', 'M', 'Ž', 'ENTER']
        ],
        greek: [
            ['Ε', 'Ρ', 'Τ', 'Υ', 'Θ', 'Ι', 'Ο', 'Π','⌫'],
            ['Α', 'Σ' , 'Δ', 'Φ', 'Γ', 'Η', 'Ξ', 'Κ', 'Λ'],
            ['Ζ', 'Χ', 'Ψ', 'Ω' , 'Β', 'Ν', 'Μ', 'ENTER']
        ]
    };

    keyboardRows: string[][] = this.keyboardLayouts[this.selectedLanguage];
    keyboardStates: Record<string, LetterState> = {};

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

        this.keyboardRows = this.keyboardLayouts[this.selectedLanguage];
        this.loadGame();
    }

    focusBoard(): void {
        setTimeout(() => this.gameBoard?.nativeElement.focus(), 0);
    }

    handleKeyDown(event: KeyboardEvent): void {
        if (this.isGameOver()) return;

        if (event.key === 'Enter') {
            event.preventDefault();
            this.submitGuess();
            return;
        }

        if (event.key === 'Backspace') {
            event.preventDefault();
            this.removeLetter();
            return;
        }

        const letter = event.key.toUpperCase();

        if (letter.length === 1 && this.isValidLetter(letter)) {
            event.preventDefault();
            this.addLetter(letter);
        }
    }

    pressKey(key: string): void {
        if (this.isGameOver()) return;

        if (key === 'ENTER') {
            this.submitGuess();
            return;
        }

        if (key === '⌫') {
            this.removeLetter();
            return;
        }

        this.addLetter(key);
    }

    addLetter(letter: string): void {
        if (this.currentGuess.length >= this.wordLength) return;

        if (this.isValidLetter(letter)) {
            this.currentGuess += letter.toUpperCase();
            this.message = '';
            this.focusBoard();
        }
    }

    removeLetter(): void {
        this.currentGuess = this.currentGuess.slice(0, -1);
        this.message = '';
        this.focusBoard();
    }

    isValidLetter(letter: string): boolean {
        if (this.selectedLanguage === 'greek') {
            return /^[Α-ΩΪΫ]$/.test(letter);
        }

        return /^[A-ZČŠŽ]$/.test(letter);
    }

    loadGame(): void {
        this.keyboardRows = this.keyboardLayouts[this.selectedLanguage];

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
        this.keyboardRows = this.keyboardLayouts[language];
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

        const rowStates = this.checkGuess(guess);

        this.guesses[this.currentRow] = guess;
        this.states[this.currentRow] = rowStates;
        this.updateKeyboardStates(guess, rowStates);

        this.currentRow++;
        this.currentGuess = '';

        if (guess === this.secretWord) {
            this.message = 'You won!';
            return;
        }

        if (this.currentRow === this.maxTries) {
            this.message = `Game over. Word was ${this.secretWord}.`;
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

    updateKeyboardStates(guess: string, rowStates: LetterState[]): void {
        const priority: Record<LetterState, number> = {
            '': 0,
            absent: 1,
            present: 2,
            correct: 3
        };

        for (let i = 0; i < guess.length; i++) {
            const letter = guess[i];
            const newState = rowStates[i];
            const oldState = this.keyboardStates[letter] || '';

            if (priority[newState] > priority[oldState]) {
                this.keyboardStates[letter] = newState;
            }
        }
    }

    getKeyboardKeyState(key: string): LetterState {
        if (key === 'ENTER' || key === '⌫') return '';
        return this.keyboardStates[key] || '';
    }

    getDisplayLetter(rowIndex: number, colIndex: number): string {
        if (rowIndex < this.currentRow) {
            return this.guesses[rowIndex][colIndex] || '';
        }

        if (rowIndex === this.currentRow && !this.isGameOver()) {
            return this.currentGuess[colIndex] || '';
        }

        return '';
    }

    triggerShake(): void {
        this.shakeRow = this.currentRow;

        setTimeout(() => {
            this.shakeRow = null;
        }, 700);
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
        this.keyboardStates = {};
        this.guesses = Array(this.maxTries).fill('');
        this.states = Array.from({ length: this.maxTries }, () =>
            Array(this.wordLength).fill('')
        );
        this.message = '';

        this.focusBoard();
    }
}