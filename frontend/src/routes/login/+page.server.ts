import { redirect, fail } from '@sveltejs/kit';
import { env } from '$env/dynamic/public';

export const actions = {
  default: async ({ cookies, request }) => {
    const data = await request.formData();
    const base = env.PUBLIC_BACKEND_URL || 'http://localhost:8080/nexus';

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

      const result = await resp.json();
      if (!result?.token) {
        return fail(500, { error: 'Invalid response from server' });
      }

      // Safer cookies (httpOnly for token; set secure=true if you have HTTPS)
      cookies.set('token', result.token, {
        path: '/',
        httpOnly: true,
        sameSite: 'lax',
        secure: false // change to true when behind HTTPS
      });
      cookies.set('user', JSON.stringify(result.user ?? {}), {
        path: '/',
        httpOnly: false,
        sameSite: 'lax',
        secure: false
      });

      throw redirect(303, '/'); // IMPORTANT: must throw
    } catch (err) {
      const msg = err instanceof Error ? err.message : 'Unknown error';
      return fail(500, { error: msg });
    }
  }
};