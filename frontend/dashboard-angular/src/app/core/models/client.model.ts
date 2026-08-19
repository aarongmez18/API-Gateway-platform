export interface ClientModel {
  id: number;
  name: string;
  active: boolean;
  createdAt: string;
}

export interface ClientRequest {
  name: string;
  active: boolean;
}
