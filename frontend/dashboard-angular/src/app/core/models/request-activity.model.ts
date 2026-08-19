export interface RequestActivity {
  id: string;
  method: string;
  endpoint: string;
  status: number | null;
  durationMs: number | null;
  startedAt: Date;
  state: 'pending' | 'success' | 'error';
}