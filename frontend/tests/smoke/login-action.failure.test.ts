import { describe, it, expect, vi } from 'vitest';

vi.mock('$env/dynamic/public', () => ({
  env: { PUBLIC_BACKEND_URL: 'http://backend:8080/nexus' }
}));

import { actions } from '../../src/routes/login/+page.server';

describe('login action - failure', () => {
  it('returns fail() with status and message on non-200', async () => {
    // @ts-expect-error
    global.fetch = vi.fn().mockResolvedValue({
      ok: false,
      status: 401,
      text: async () => 'Unauthorized'
    });

    const cookies = { set: vi.fn() };
    const request = {
      formData: async () => ({
        get: (k: string) => (k === 'email' ? 'bad@example.com' : 'wrong')
      })
    } as any;

    const result: any = await actions.default({ cookies, request } as any);
    expect(result?.status).toBe(401);
    expect(String(result?.data?.error || '')).toContain('Login failed (401)');
  });
});