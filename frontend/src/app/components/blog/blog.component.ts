import { CommonModule } from '@angular/common';
import { BlogPostService } from '../../service/blog-post.service';
import { BlogPost, GroupedPost } from '../../models/blog-post';
import { BlogComment } from '../../models/blog-comment';
import { BlogCommentService } from '../../service/blog-comment.service';
import { FormsModule } from '@angular/forms';
import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-blog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css']
})
export class BlogComponent implements OnInit, AfterViewInit, OnDestroy {

  groupedPosts: GroupedPost[] = [];
  commentsByGroup: { [group: number]: BlogComment[] } = {};
  newComments: { [group: number]: BlogComment } = {};
  commentMessages: { [group: number]: string } = {};

  isLoading = true;
  isLoadingMore = false;
  hasMore = true;
  page = 0;
  pageSize = typeof window !== 'undefined' && window.innerWidth < 768 ? 3 : 5;

  lightboxPost: GroupedPost | null = null;
  lightboxIndex = 0;

  @ViewChild('scrollAnchor') scrollAnchor!: ElementRef<HTMLDivElement>;
  private observer?: IntersectionObserver;

  constructor(
    private blogPostService: BlogPostService,
    private blogCommentService: BlogCommentService,
    private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadPage();
  }

  ngAfterViewInit(): void {
    this.setupObserver();
  }

  private setupObserver(): void {
    if (!this.scrollAnchor?.nativeElement || this.observer) {
      return;
    }

    this.observer = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && this.hasMore && !this.isLoadingMore) {
        this.loadPage();
      }
    }, { rootMargin: '400px' });

    this.observer.observe(this.scrollAnchor.nativeElement);
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }

  loadPage(): void {
    const loadingFirstPage = this.page === 0;

    if (loadingFirstPage) {
      this.isLoading = true;
    } else {
      this.isLoadingMore = true;
    }

    this.blogPostService.getPosts(this.page, this.pageSize).subscribe({
      next: (data: BlogPost[]) => {
        if (data.length === 0) {
          this.hasMore = false;
        } else {
          const newGroups = this.groupPosts(data);
          this.groupedPosts = [...this.groupedPosts, ...newGroups];

          newGroups.forEach(group => {
            this.loadComments(group.groupId, group.representativeId);
            this.newComments[group.groupId] = { authorName: '', commentText: '' };
          });

          this.page++;
        }

        this.isLoading = false;
        this.isLoadingMore = false;
        this.cdr.detectChanges();

        if (!this.observer) {
          setTimeout(() => this.setupObserver());
        }
      },
      error: (err) => {
        console.error('error fired:', err);
        this.isLoading = false;
        this.isLoadingMore = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadComments(groupId: number, postId: number): void {
    this.blogCommentService.getComments(postId).subscribe({
      next: data => {
        this.commentsByGroup[groupId] = data;
        this.cdr.detectChanges();
      },
      error: err => console.error('COMMENTS ERROR', err)
    });
  }

  submitComment(groupId: number, postId: number): void {
    const comment = this.newComments[groupId];

    if (!comment || !comment.commentText.trim()) {
      return;
    }

    this.blogCommentService.createComment(postId, comment).subscribe({
      next: savedComment => {
        this.commentsByGroup[groupId] = [
          savedComment,
          ...(this.commentsByGroup[groupId] || [])
        ];

        this.newComments[groupId] = { authorName: '', commentText: '' };
        this.commentMessages[groupId] = 'Comment added successfully.';

        this.cdr.detectChanges();

        setTimeout(() => {
          this.commentMessages[groupId] = '';
          this.cdr.detectChanges();
        }, 3000);
      },
      error: err => console.error(err)
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

      if (post.id < grouped.representativeId) {
        grouped.representativeId = post.id;
      }

      if (!grouped.content && post.content) {
        grouped.content = post.content;
      }
    }

    return Array.from(map.values()).sort(
      (a, b) => new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime()
    );
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

  trackByGroup(index: number, post: GroupedPost): number {
    return post.groupId;
  }
}