import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { keycloak } from './app/core/auth/keycloak';

async function start(): Promise<void> {
  try {
    await keycloak.init({ onLoad: 'login-required', pkceMethod: 'S256' });
    console.log('Usuario autenticado:', keycloak.tokenParsed);
    await bootstrapApplication(App, appConfig);
  } catch (error) {
    console.error('Error inicializando Keycloak:', error);
  }
}

start();