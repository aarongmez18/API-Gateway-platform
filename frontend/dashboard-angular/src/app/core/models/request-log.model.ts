export interface RequestLog {
  id: number;
  clientId: number | null;
  clientName: string | null;
  apiCode: string;
  endpoint: string;
  method: string;
  statusCode: number;
  durationMs: number;
  requestedAt: string;
}

export interface PageResponse<T> {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}