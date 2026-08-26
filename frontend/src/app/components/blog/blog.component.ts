import { ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BlogPostService } from '../../service/blog-post.service';
import { BlogPost, GroupedPost } from '../../models/blog-post';
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

  groupedPosts: GroupedPost[] = [];
  commentsByGroup: { [group: number]: BlogComment[] } = {};
  newComments: { [group: number]: BlogComment } = {};
  commentMessages: { [group: number]: string } = {};

  isLoading = true;

  // lightbox state
  lightboxPost: GroupedPost | null = null;
  lightboxIndex = 0;

  constructor(
    private blogPostService: BlogPostService,
    private blogCommentService: BlogCommentService,
    private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.blogPostService.getPosts().subscribe({
      next: (data: BlogPost[]) => {
        this.groupedPosts = this.groupPosts(data);
        this.isLoading = false;

        this.groupedPosts.forEach(group => {
          this.loadComments(group.groupId);
          this.newComments[group.groupId] = { authorName: '', commentText: '' };
        });

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('error fired:', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private groupPosts(posts: BlogPost[]): GroupedPost[] {
    const map = new Map<number, GroupedPost>();

    for (const post of posts) {
      let grouped = map.get(post.groupId);

      if (!grouped) {
        grouped = {
          groupId: post.groupId,
          representativeId: post.id,
          title: post.title,
          content: post.content,
          createdOn: post.createdOn,
          media: []
        };
        map.set(post.groupId, grouped);
      }

      grouped.media.push({ mediaUrl: post.mediaUrl, mediaType: post.mediaType });

      // keep the earliest id as the representative (in case rows aren't inserted in order)
      if (post.id < grouped.representativeId) {
        grouped.representativeId = post.id;
      }

      // prefer non-empty content if the first row happened to have none
      if (!grouped.content && post.content) {
        grouped.content = post.content;
      }
    }

    return Array.from(map.values()).sort(
      (a, b) => new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime()
    );
  }

  loadComments(group: number): void {
    // NOTE: replace `group` here with the representativeId if your API still expects a post id
    this.blogCommentService.getComments(group).subscribe({
      next: data => {
        this.commentsByGroup[group] = data;
        this.cdr.detectChanges();
      },
      error: err => console.error('COMMENTS ERROR', err)
    });
  }

  submitComment(group: number): void {
    const comment = this.newComments[group];

    if (!comment || !comment.commentText.trim()) {
      return;
    }

    // NOTE: same as above - swap `group` for representativeId if the API needs a real post id
    this.blogCommentService.createComment(group, comment).subscribe({
      next: savedComment => {
        this.commentsByGroup[group] = [
          savedComment,
          ...(this.commentsByGroup[group] || [])
        ];

        this.newComments[group] = { authorName: '', commentText: '' };
        this.commentMessages[group] = 'Comment added successfully.';

        setTimeout(() => {
          this.commentMessages[group] = '';
        }, 3000);
      },
      error: err => console.error(err)
    });
  }

  mediaGridClass(post: GroupedPost): string {
    return 'count-' + Math.min(post.media.length, 5);
  }

  openLightbox(post: GroupedPost, index: number): void {
    this.lightboxPost = post;
    this.lightboxIndex = index;
  }

  closeLightbox(): void {
    this.lightboxPost = null;
  }

  nextMedia(): void {
    if (!this.lightboxPost) return;
    this.lightboxIndex = (this.lightboxIndex + 1) % this.lightboxPost.media.length;
  }

  prevMedia(): void {
    if (!this.lightboxPost) return;
    this.lightboxIndex =
      (this.lightboxIndex - 1 + this.lightboxPost.media.length) % this.lightboxPost.media.length;
  }

  @HostListener('window:keydown', ['$event'])
  handleKeydown(event: KeyboardEvent): void {
    if (!this.lightboxPost) return;
    if (event.key === 'ArrowRight') this.nextMedia();
    if (event.key === 'ArrowLeft') this.prevMedia();
    if (event.key === 'Escape') this.closeLightbox();
  }
}