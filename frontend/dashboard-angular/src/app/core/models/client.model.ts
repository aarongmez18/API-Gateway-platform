export interface ClientModel {
  id: number;
  name: string;
  active: boolean;
  rateLimitPerMinute: number;
  createdAt: string;
}

export interface ClientRequest {
  name: string;
  active: boolean;
  rateLimitPerMinute: number;
}