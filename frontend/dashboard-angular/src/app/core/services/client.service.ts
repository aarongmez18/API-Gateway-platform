import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { ClientModel, ClientRequest } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_CONFIG.baseUrl}${API_CONFIG.usersManagement}/clients`;

  findAll(): Observable<ClientModel[]> {
    return this.http.get<ClientModel[]>(this.url);
  }

  findById(id: number): Observable<ClientModel> {
    return this.http.get<ClientModel>(`${this.url}/${id}`);
  }

  create(request: ClientRequest): Observable<ClientModel> {
    return this.http.post<ClientModel>(this.url, request);
  }

  update(id: number, request: ClientRequest): Observable<ClientModel> {
    return this.http.put<ClientModel>(`${this.url}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
