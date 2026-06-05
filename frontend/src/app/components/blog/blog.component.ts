import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogPostService, BlogPost } from '../../service/blog-post.service';

@Component({
  selector: 'app-blog',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css']
})
export class BlogComponent implements OnInit {

  blogPosts: BlogPost[] = [];

  constructor(private blogPostService: BlogPostService) {}

  ngOnInit(): void {
    console.log('BlogComponent loaded');
    this.loadBlogPosts();
  }

  loadBlogPosts(): void {
    this.blogPostService.getPosts().subscribe({
      next: data => {
        console.log('BLOG DATA:', data);
        this.blogPosts = data;
      },
      error: err => {
        console.error('BLOG ERROR:', err);
      }
    });
  }
}