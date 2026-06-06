import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-bio',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './bio.component.html',
  styleUrl: './bio.component.css'
})
export class BioComponent {

  bioPdfUrl: SafeResourceUrl;

  constructor(private sanitizer: DomSanitizer) {
    this.bioPdfUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
      'assets/bio/cv.pdf'
    );
  }
}