export interface HourlyMetric { hour: number; count: number; }

export interface ApiUsage { apiCode: string; count: number; }

export interface ClientUsage { clientId: number; clientName: string | null; count: number; }

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

export interface RequestDashboard {
  totalRequests: number;
  todayRequests: number;
  errors: number;
  averageResponseTimeMs: number;
  requestsByHour: HourlyMetric[];
  errorsByHour: HourlyMetric[];
  topApis: ApiUsage[];
  topClients: ClientUsage[];
}