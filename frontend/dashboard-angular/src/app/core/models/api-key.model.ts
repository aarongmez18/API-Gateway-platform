export interface ApiKeyModel {
  id: number;
  clientId: number;
  clientName: string;
  active: boolean;
  createdAt: string;
}

export interface ApiKeyRequest {
  clientId: number;
  active: boolean;
}
