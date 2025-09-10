import { env } from '$env/dynamic/public';
const base = env.PUBLIC_BACKEND_URL || 'http://localhost:8080/nexus';

export function load({ locals, url }) {
  const userId = locals.user.userId;
  const user = locals.user;
  async function getUserReservations() {
    const response = await fetch(
      `${base}/reservations/user/${userId}`,
      {
        method: "GET"
      }
    );
    const result = await response.json();
    console.log(result);
    return result;
  }

  return {
    user: user,
    reservations: getUserReservations(),
  };
}
