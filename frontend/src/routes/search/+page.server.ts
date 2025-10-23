import type { Actions, PageServerLoad } from './$types';
import { fail, redirect } from '@sveltejs/kit';
import { getCities, getFlights, postCommentary, postRating } from '$lib/server/flights';

export const load: PageServerLoad = async ({ locals, url }) => {
  const type = (url.searchParams.get('type') as 'round-trip' | 'one-way') ?? 'one-way';
  return {
    user: locals.user,
    cities: getCities(),
    flights: getFlights(type, url.searchParams)
  };
};

export const actions: Actions = {
  createCommentary: async ({ request, cookies, locals }) => {
    try {
      const data = await request.formData();
      const token = cookies.get('token');
      const userName = locals.user.firstName;
      const rawParent = data.get('parentId');
      const parentComment =
        rawParent && rawParent !== 'null' ? Number(rawParent) : 0;

      await postCommentary({
        token,
        parentComment,
        userId: data.get('userId'),
        content: data.get('content'),
        flightId: data.get('flightId'),
        userName
      });

      return;
    } catch (err: any) {
      if (err?.status === 403) {
        throw redirect(302, '/login');
      }
      return fail(500, { error: err?.message ?? 'Unknown error' });
    }
  },

  createRating: async ({ request }) => {
    try {
      const data = await request.formData();
      await postRating({
        userId: data.get('userId'),
        flightId: data.get('flightId'),
        value: data.get('rating')
      });
      return;
    } catch (err: any) {
      return fail(500, { error: err?.message ?? 'Unknown error' });
    }
  }
};