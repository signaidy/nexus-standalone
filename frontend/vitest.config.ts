import { defineConfig } from 'vitest/config';

export default defineConfig({
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