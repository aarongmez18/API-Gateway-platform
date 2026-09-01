import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { PageResponse, RequestDashboard, RequestLog } from '../models/request-log.model';

export interface RequestLogFilters {
  clientId?: number | null;
  apiCode?: string | null;
  statusCode?: number | null;
  page?: number;
  size?: number;
}

@Injectable({ providedIn: 'root' })
export class RequestLogService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_CONFIG.baseUrl}${API_CONFIG.requestsManagement}/request-logs`;

  find(filters: RequestLogFilters = {}): Observable<PageResponse<RequestLog>> {
    let params = new HttpParams().set('page', filters.page ?? 0).set('size', filters.size ?? 50);
    if (filters.clientId != null) params = params.set('clientId', filters.clientId);
    if (filters.apiCode) params = params.set('apiCode', filters.apiCode);
    if (filters.statusCode != null) params = params.set('statusCode', filters.statusCode);
    return this.http.get<PageResponse<RequestLog>>(this.url, { params });
  }

  dashboard(): Observable<RequestDashboard> { return this.http.get<RequestDashboard>(`${this.url}/dashboard`); }
}