import { describe, it, expect, vi } from 'vitest';

vi.mock('$env/dynamic/public', () => ({
  env: { PUBLIC_BACKEND_URL: process.env.PUBLIC_BACKEND_URL ?? 'http://backend:8080/nexus' }
}));

import { actions } from '../../src/routes/login/+page.server';

describe('login action - success', () => {
  it('calls /nexus/auth/login and redirects on success; sets cookies', async () => {
    process.env.PUBLIC_BACKEND_URL = 'http://backend:8080/nexus';

    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ token: 'T0K3N', user: { id: 1, email: 'user@example.com' } })
    });

    const cookies = { set: vi.fn() };
    const request = {
      formData: async () => ({
        get: (k: string) => (k === 'email' ? 'user@example.com' : 'secret')
      })
    } as any;

    // If your action uses SvelteKit's redirect(303, ...), it throws:
    await expect(actions.default({ cookies, request, fetch: fetchMock } as any))
      .rejects.toMatchObject({ status: 303 });

    expect(fetchMock).toHaveBeenCalledWith(
      'http://backend:8080/nexus/auth/login',
      expect.objectContaining({ method: 'POST' })
    );
    expect(cookies.set).toHaveBeenCalledWith('token', 'T0K3N', expect.any(Object));
    expect(cookies.set).toHaveBeenCalledWith(
      'User',
      expect.stringContaining('"email":"user@example.com"'),
      expect.any(Object)
    );
  });
});