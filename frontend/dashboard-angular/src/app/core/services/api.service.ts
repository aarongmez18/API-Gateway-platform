import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { ApiModel, ApiRequest } from '../models/api.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_CONFIG.baseUrl}${API_CONFIG.apiManagement}/apis`;

  findAll(): Observable<ApiModel[]> {
    return this.http.get<ApiModel[]>(this.url);
  }

  findById(id: number): Observable<ApiModel> {
    return this.http.get<ApiModel>(`${this.url}/${id}`);
  }

  create(request: ApiRequest): Observable<ApiModel> {
    return this.http.post<ApiModel>(this.url, request);
  }

  update(id: number, request: ApiRequest): Observable<ApiModel> {
    return this.http.put<ApiModel>(`${this.url}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
