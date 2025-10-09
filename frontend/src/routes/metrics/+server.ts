// src/routes/metrics/+server.ts
import type { RequestHandler } from './$types';
import { metricsText } from '$lib/server/metrics';

export const GET: RequestHandler = async () => {
  const body = await metricsText();
  return new Response(body, {
    headers: { 'Content-Type': 'text/plain; version=0.0.4; charset=utf-8' }
  });
};