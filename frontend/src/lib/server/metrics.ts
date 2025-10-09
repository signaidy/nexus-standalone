// src/lib/server/metrics.ts
import client from 'prom-client';

// Create ONE global registry (avoid duplicate metric registration on HMR)
const registry = new client.Registry();
client.collectDefaultMetrics({ register: registry, prefix: 'nexus_front_' });

// HTTP request counter
export const httpRequests = new client.Counter({
  name: 'nexus_front_http_requests_total',
  help: 'Total HTTP requests',
  labelNames: ['route', 'method', 'status']
});

// HTTP duration histogram (seconds)
export const httpDuration = new client.Histogram({
  name: 'nexus_front_http_request_duration_seconds',
  help: 'HTTP request duration in seconds',
  labelNames: ['route', 'method', 'status'],
  buckets: [0.025, 0.05, 0.1, 0.25, 0.5, 1, 2, 5]
});

registry.registerMetric(httpRequests);
registry.registerMetric(httpDuration);

// Expose metrics text
export async function metricsText(): Promise<string> {
  return registry.metrics();
}