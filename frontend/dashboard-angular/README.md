# Gateway Control — Dashboard Angular

Frontend del proyecto API Gateway Platform.

## Requisitos

- Node.js compatible con Angular 21
- Gateway levantado en `http://localhost:9000`

## Ejecutar

```bash
npm install
npm start
```

## Proxy local

Angular utiliza `proxy.conf.json` para transformar:

```text
/gateway/api-management/apis
    -> http://localhost:9000/api-management/apis
```

Así el navegador no necesita llamar directamente de `4200` a `9000` durante desarrollo.

## Pantallas

- Dashboard
- APIs: listado + alta + edición + borrado
- Clientes: listado + alta + edición + borrado
- API Keys: listado + alta + edición + borrado
- Peticiones: monitor de las llamadas HTTP realizadas por Angular en la sesión

## Arquitectura

```text
src/app/
├── core/
│   ├── config/
│   ├── interceptors/
│   ├── models/
│   └── services/
├── features/
│   ├── dashboard/
│   ├── apis/
│   ├── clients/
│   ├── api-keys/
│   └── requests/
└── shared/
    └── components/
```
