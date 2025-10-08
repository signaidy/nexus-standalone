import { vi } from 'vitest';

// Mock SvelteKit dynamic env (public + private)
// Adjust keys to the ones your +page.server.ts actually reads
vi.mock('$env/dynamic/public', () => ({
  env: {
    PUBLIC_BACKEND_URL: process.env.PUBLIC_BACKEND_URL ?? 'http://backend:8080/nexus'
  }
}));

vi.mock('$env/dynamic/private', () => ({
  env: {
    JWT_SECRET: 'test-secret',          // example
    COOKIE_NAME: 'token',               // example
    // add any other private vars used by +page.server.ts
  }
}));
