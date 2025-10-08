import { redirect, fail, isRedirect } from '@sveltejs/kit';
import { env as pub } from '$env/dynamic/public';
import { env as priv } from '$env/dynamic/private';

export const actions = {
  default: async (event) => {
    const { cookies, request, fetch, url } = event;
    const data = await request.formData();

    const baseRaw = priv.PRIVATE_BACKEND_URL ?? pub.PUBLIC_BACKEND_URL ?? 'http://backend.dev.svc.cluster.local:8080/nexus';
    const apiBase = baseRaw.startsWith('http')
      ? baseRaw.replace(/\/+$/, '')
      : new URL(baseRaw, url.origin).toString().replace(/\/+$/, '');

    try {
      const resp = await fetch(`${apiBase}/auth/login`, {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({
          email: String(data.get('email') ?? ''),
          password: String(data.get('password') ?? '')
        })
      });

      if (!resp.ok) {
        const text = await resp.text().catch(() => '');
        return fail(resp.status, { error: `Login failed (${resp.status}) ${text}`.trim() });
      }

      const result = await resp.json().catch(() => null);
      if (!result?.token) return fail(500, { error: 'Invalid response from server' });

      cookies.set('token', result.token, { path: '/', httpOnly: true, sameSite: 'lax', secure: false });
      cookies.set('User', JSON.stringify(result.user ?? {}), { path: '/', httpOnly: false, sameSite: 'lax', secure: false });

      throw redirect(303, '/');
    } catch (e) {
      console.error('[login action] error:', e);
      if (isRedirect(e)) throw e;
      const msg = e instanceof Error ? e.message : 'Unknown error';
      return fail(500, { error: msg });
    }
  }
};