import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { ApiKeyCreatedModel, ApiKeyModel, ApiKeyRequest } from '../models/api-key.model';

@Injectable({ providedIn: 'root' })
export class ApiKeyService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_CONFIG.baseUrl}${API_CONFIG.usersManagement}/api-keys`;

  findAll(): Observable<ApiKeyModel[]> {
    return this.http.get<ApiKeyModel[]>(this.url);
  }

  findById(id: number): Observable<ApiKeyModel> {
    return this.http.get<ApiKeyModel>(`${this.url}/${id}`);
  }

  findByClientId(clientId: number): Observable<ApiKeyModel[]> {
    return this.http.get<ApiKeyModel[]>(`${this.url}/client/${clientId}`);
  }

  create(request: ApiKeyRequest): Observable<ApiKeyCreatedModel> {
    return this.http.post<ApiKeyCreatedModel>(this.url, request);
  }

  update(id: number, request: ApiKeyRequest): Observable<ApiKeyModel> {
    return this.http.put<ApiKeyModel>(`${this.url}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
