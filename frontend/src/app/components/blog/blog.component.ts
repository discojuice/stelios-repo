import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogPostService, BlogPost } from '../../service/blog-post.service';
import { BlogComment } from '../../models/blog-comment';
import { BlogCommentService } from '../../service/blog-comment.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-blog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css']
})
export class BlogComponent implements OnInit {

  blogPosts: BlogPost[] = [];
  commentsByPostId: { [postId: number]: BlogComment[] } = {};
  newComments: { [postId: number]: BlogComment } = {};
  commentMessages: { [postId: number]: string } = {};

  isLoading = true;

  constructor(
    private blogPostService: BlogPostService,
    private blogCommentService: BlogCommentService,
    private cdr: ChangeDetectorRef) { }


  ngOnInit(): void {
    this.blogPostService.getPosts().subscribe({
      next: (data) => {
        console.log('next fired, data:', data);
        this.blogPosts = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('error fired:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }



  loadComments(postId: number): void {
    this.blogCommentService.getComments(postId).subscribe({
      next: data => {
        console.log('COMMENTS for post', postId, data);
        this.commentsByPostId[postId] = data;
      },
      error: err => console.error('COMMENTS ERROR', err)
    });
  }

  submitComment(postId: number): void {
    const comment = this.newComments[postId];

    if (!comment || !comment.commentText.trim()) {
      return;
    }

    this.blogCommentService.createComment(postId, comment).subscribe({
      next: savedComment => {
        this.commentsByPostId[postId] = [
          savedComment,
          ...(this.commentsByPostId[postId] || [])
        ];

        this.newComments[postId] = {
          authorName: '',
          commentText: ''
        };

        this.commentMessages[postId] = 'Comment added successfully.';

        setTimeout(() => {
          this.commentMessages[postId] = '';
        }, 3000);
      },
      error: err => console.error(err)
    });
  }
}