// frontend/src/lib/server/flights.ts
import { env } from '$env/dynamic/public';

const base = env.PUBLIC_BACKEND_URL || '/nexus';

async function fetchJson(url: string, init?: RequestInit) {
  const res = await fetch(url, init);
  // If you want to handle 403 uniformly, throw here:
  if (res.status === 403) {
    const err: any = new Error('Forbidden');
    err.status = 403;
    throw err;
  }
  return res.json();
}

export function getCities() {
  return fetchJson(`${base}/flights/avianca/cities`, { method: 'GET' });
}

export function getFlights(
  type: 'round-trip' | 'one-way',
  searchParams: URLSearchParams
) {
  const endpoint =
    type === 'round-trip' ? 'round-trip-flights' : 'one-way-flights';
  const qs = searchParams.toString();
  return fetchJson(`${base}/flights/avianca/${endpoint}?${qs}`, { method: 'GET' });
}

export function postCommentary(args: {
  token?: string | null;
  parentComment: number;
  userId: FormDataEntryValue | null;
  content: FormDataEntryValue | null;
  flightId: FormDataEntryValue | null;
  userName: string;
}) {
  const { token, ...body } = args;
  return fetchJson(`${base}/comments`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {})
    },
    body: JSON.stringify(body)
  });
}

export function postRating(args: {
  userId: FormDataEntryValue | null;
  flightId: FormDataEntryValue | null;
  value: FormDataEntryValue | null;
}) {
  return fetchJson(`${base}/flights/create-rating`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(args)
  });
}