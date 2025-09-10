import { redirect, fail, isRedirect } from '@sveltejs/kit';
import { env } from '$env/dynamic/public';

export const actions = {
  default: async ({ cookies, request }) => {
    const data = await request.formData();
    const base = env.PUBLIC_BACKEND_URL || 'http://localhost:8080/nexus';

    // Only wrap the network bits
    try {
      const resp = await fetch(`${base}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: data.get('email'),
          password: data.get('password')
        })
      });

      if (!resp.ok) {
        const text = await resp.text().catch(() => '');
        return fail(resp.status, { error: `Login failed (${resp.status}) ${text}`.trim() });
      }

      const result = await resp.json().catch(() => null);
      if (!result?.token) {
        return fail(500, { error: 'Invalid response from server' });
      }

      cookies.set('token', result.token, {
        path: '/', httpOnly: true, sameSite: 'lax', secure: false
      });
      cookies.set('User', JSON.stringify(result.user ?? {}), {
        path: '/', httpOnly: false, sameSite: 'lax', secure: false
      });
    } catch (e) {
      // If a redirect ever bubbles up here
      if (isRedirect(e)) throw e;
      const msg = e instanceof Error ? e.message : 'Unknown error';
      return fail(500, { error: msg });
    }

    throw redirect(303, '/');
  }
};