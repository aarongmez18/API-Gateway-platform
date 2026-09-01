import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { NgxEchartsDirective, provideEchartsCore } from 'ngx-echarts';
import * as echarts from 'echarts/core';
import type { EChartsCoreOption } from 'echarts/core';
import { BarChart, LineChart } from 'echarts/charts';
import { GridComponent, TooltipComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { ApiModel } from '../../core/models/api.model';
import { RequestDashboard } from '../../core/models/request-log.model';
import { ApiService } from '../../core/services/api.service';
import { ClientService } from '../../core/services/client.service';
import { RequestLogService } from '../../core/services/request-log.service';
import { StatCardComponent } from '../../shared/components/stat-card/stat-card.component';

echarts.use([BarChart, LineChart, GridComponent, TooltipComponent, CanvasRenderer]);

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [StatCardComponent, NgxEchartsDirective],
  providers: [provideEchartsCore({ echarts })],
  templateUrl: './dashboard.page.html',
  styleUrl: './dashboard.page.css'
})
export class DashboardPage {

  private readonly apiService = inject(ApiService);
  private readonly clientService = inject(ClientService);
  private readonly requestLogService = inject(RequestLogService);

  readonly loading = signal(true);
  readonly error = signal('');
  readonly dashboard = signal<RequestDashboard | null>(null);
  readonly apis = signal<ApiModel[]>([]);
  readonly activeApis = signal(0);
  readonly activeClients = signal(0);

  readonly requestsByHourChart = computed<EChartsCoreOption>(() => this.lineChart(this.dashboard()?.requestsByHour ?? []));
  readonly errorsByHourChart = computed<EChartsCoreOption>(() => this.lineChart(this.dashboard()?.errorsByHour ?? []));
  readonly topApisChart = computed<EChartsCoreOption>(() => this.barChart(this.dashboard()?.topApis.map(item => ({ name: this.apiName(item.apiCode), value: item.count })) ?? []));
  readonly topClientsChart = computed<EChartsCoreOption>(() => this.barChart(this.dashboard()?.topClients.map(item => ({ name: item.clientName || `Cliente ${item.clientId}`, value: item.count })) ?? []));

  constructor() { this.load(); }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    forkJoin({
      dashboard: this.requestLogService.dashboard(),
      apis: this.apiService.findAll(),
      clients: this.clientService.findAll()
    }).subscribe({
      next: ({ dashboard, apis, clients }) => {
        this.dashboard.set(dashboard);
        this.apis.set(apis);
        this.activeApis.set(apis.filter(api => api.active).length);
        this.activeClients.set(clients.filter(client => client.active).length);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el dashboard. Comprueba si los servicios están disponibles.');
        this.loading.set(false);
      }
    });
  }

  private apiName(apiCode: string): string { return this.apis().find(api => api.code === apiCode)?.name ?? apiCode; }

  private lineChart(data: { hour: number; count: number }[]): EChartsCoreOption {
    return {
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis' },
      grid: { left: 42, right: 18, top: 20, bottom: 36 },
      xAxis: { type: 'category', boundaryGap: false, data: data.map(item => `${String(item.hour).padStart(2, '0')}:00`), axisLine: { lineStyle: { color: '#333' } }, axisLabel: { color: '#777', interval: 2 }, axisTick: { show: false } },
      yAxis: { type: 'value', minInterval: 1, axisLabel: { color: '#777' }, splitLine: { lineStyle: { color: '#222' } } },
      series: [{ type: 'line', data: data.map(item => item.count), smooth: true, showSymbol: false, lineStyle: { width: 2 }, areaStyle: { opacity: .08 } }]
    };
  }

  private barChart(data: { name: string; value: number }[]): EChartsCoreOption {
    return {
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: 120, right: 22, top: 15, bottom: 25 },
      xAxis: { type: 'value', minInterval: 1, axisLabel: { color: '#777' }, splitLine: { lineStyle: { color: '#222' } } },
      yAxis: { type: 'category', data: data.map(item => item.name), axisLabel: { color: '#aaa', width: 105, overflow: 'truncate' }, axisLine: { lineStyle: { color: '#333' } }, axisTick: { show: false } },
      series: [{ type: 'bar', data: data.map(item => item.value), barMaxWidth: 22 }]
    };
  }
}