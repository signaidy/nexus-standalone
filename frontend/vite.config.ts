// frontend/vite.config.ts
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
  server: { port: 3002 },
  preview: { host: true, port: 3000 },
  plugins: [sveltekit()],
  build: { sourcemap: false },
  ssr: {
    noExternal: ['lucide-svelte', 'bits-ui', 'cmdk-sv', 'tailwind-variants', 'prom-client']
  },

  test: {
    environment: 'node',
    include: ['tests/**/*.test.ts'],
    restoreMocks: true,
    clearMocks: true,
    coverage: {
      enabled: true,
      provider: 'v8',
      reporter: ['lcov', 'text'],
      reportsDirectory: 'coverage'
    }
  }
});
