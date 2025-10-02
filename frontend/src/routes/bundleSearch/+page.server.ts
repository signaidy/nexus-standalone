import { fail, redirect } from "@sveltejs/kit";
import { env } from '$env/dynamic/public';

export function load({ locals, url }) {
  const base = env.PUBLIC_BACKEND_URL || '/nexus';
  async function getOneWayFlights() {
    const response = await fetch(
      `${base}/flights/avianca/one-way-flights?${url.searchParams.toString()}`,
      {
        method: "GET"
      }
    );

    const result = await response.json();
    return result;
  }

  async function getRoundTripFlights() {
    const response = await fetch(
      `${base}/flights/avianca/round-trip-flights?${url.searchParams.toString()}`,
      {
        method: "GET"
      }
    );

    const result = await response.json();
    return result;
  }

  async function getCities() {
    const response = await fetch(`${base}/flights/avianca/cities`, {
      method: "GET",
    });

    const result = await response.json();
    return result;
  }

  let flightsFunction;
  const type = url.searchParams.get('type');
  
  if (type === 'round-trip') {
    flightsFunction = getRoundTripFlights();
  } else {
    flightsFunction = getOneWayFlights();
  }

  return {
    user: locals.user,
    cities: getCities(),
    flights: flightsFunction,
  };
}


export const actions = {  
  createCommentary: async ({ request, cookies, locals }) => {
    const base = env.PUBLIC_BACKEND_URL || '/nexus';
    const data = await request.formData();
    const token = cookies.get('token');
    const user = locals.user.firstName;
    let parentId = data.get("parentId") || 0;
    if (!parentId || parentId === "null") {
      parentId = 0;
    }
    console.log(parentId)
    try {
      const response = await fetch(`${base}/comments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`

        },
        body: JSON.stringify({
          parentComment: parentId,
          userId: data.get("userId"),
          content: data.get("content"),
          flightId: data.get("flightId"),
          userName: user
        }),
      });
      if (response.status === 403) {
        // Redirect user to login page
        console.log(response.status)
        return { status: 403, redirect: '/login' };
      }

      const result = await response.json();

      if (result.error) {
        throw new Error(result.error);
      }
    } catch (error) {
      if (error instanceof Error) {
        return fail(500, {
          error: error.message,
        });
      }
    }
  },
  createRating: async ({ request }) => {    
    const base = env.PUBLIC_BACKEND_URL || '/nexus';
    const data = await request.formData();
    try {
      const response = await fetch(`${base}/flights/create-rating`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: data.get("userId"),
          flightId: data.get("flightId"),
          value: data.get("rating"),
        }),
      });
      const result = await response.json();

      if (result.error) {
        throw new Error(result.error);
      }
    } catch (error) {
      if (error instanceof Error) {
        return fail(500, {
          error: error.message,
        });
      }
    }
  },
};
