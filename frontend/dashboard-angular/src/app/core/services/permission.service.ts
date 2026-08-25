import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { PermissionModel, PermissionRequest } from '../models/permission.model';

@Injectable({ providedIn: 'root' })
export class PermissionService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_CONFIG.baseUrl}${API_CONFIG.usersManagement}/permissions`;

  findByClient(clientId: number): Observable<PermissionModel[]> {
    return this.http.get<PermissionModel[]>(`${this.url}/client/${clientId}`);
  }

  grant(request: PermissionRequest): Observable<PermissionModel> {
    return this.http.post<PermissionModel>(this.url, request);
  }

  revoke(clientId: number, apiCode: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/client/${clientId}/api/${encodeURIComponent(apiCode)}`);
  }
}