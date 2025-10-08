import { redirect, type RequestHandler } from '@sveltejs/kit';

const clear = (cookies: import('@sveltejs/kit').Cookies) => {
  cookies.set('token', '', { path: '/', httpOnly: true, sameSite: 'lax', secure: false, maxAge: 0 });
  cookies.set('User',  '', { path: '/', httpOnly: false, sameSite: 'lax', secure: false, maxAge: 0 });
};

export const POST: RequestHandler = ({ cookies }) => {
  clear(cookies);
  throw redirect(303, '/');
};

export const GET: RequestHandler = ({ cookies }) => {
  clear(cookies);
  throw redirect(303, '/');
};