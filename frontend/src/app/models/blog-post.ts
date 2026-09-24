export interface BlogPost {
  id: number;
  title: string;
  content: string;
  mediaUrl: string;
  mediaType: 'image' | 'video';
  posterUrl?: string;
  createdOn: string;
  groupId: number;
}

export interface GroupedPost {
  groupId: number;
  representativeId: number;
  title: string;
  content: string;
  createdOn: string;
  media: GroupedMedia[];
}

export interface GroupedMedia {
  mediaUrl: string;
  mediaType: 'image' | 'video';
  // Small preview shown in the grid; the full mediaUrl is only loaded in the lightbox.
  thumbUrl: string;
}