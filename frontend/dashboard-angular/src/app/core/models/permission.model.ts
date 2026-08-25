export interface PermissionModel {
  id: number;
  clientId: number;
  apiCode: string;
  createdAt: string;
}

export interface PermissionRequest {
  clientId: number;
  apiCode: string;
}