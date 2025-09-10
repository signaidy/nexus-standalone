import { PUBLIC_BACKEND_URL } from '$env/static/public';

export function load({ locals, url }) {
  const userId = locals.user.userId;
  const user = locals.user;
  async function getUserFlights() {
    const response = await fetch(
      `${PUBLIC_BACKEND_URL}/flights/user/${userId}`,
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
    flights: getUserFlights(),
  };
}
