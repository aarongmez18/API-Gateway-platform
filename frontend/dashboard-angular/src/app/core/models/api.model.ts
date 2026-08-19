export interface ApiModel {
  id: number;
  name: string;
  targetUrl: string;
  path: string;
  active: boolean;
  createdAt: string;
}

export interface ApiRequest {
  name: string;
  targetUrl: string;
  path: string;
  active: boolean;
}
