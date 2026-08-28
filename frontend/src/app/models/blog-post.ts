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
  media: { mediaUrl: string; mediaType: 'image' | 'video'; posterUrl?: string }[];
}