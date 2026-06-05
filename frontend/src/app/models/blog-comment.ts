export interface BlogComment {
  id?: number;
  blogPostId?: number;
  authorName: string;
  commentText: string;
  createdOn?: string;
}