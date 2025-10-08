import { fail, redirect } from "@sveltejs/kit";
import { env } from '$env/dynamic/public';

export function load({ locals, url }) {
    const base = env.PUBLIC_BACKEND_URL || '/nexus';
    async function getCitiesHotels() {
        const response = await fetch(`${base}/reservations/cities`, {
          method: "GET",
        });
    
        const result = await response.json();
        return result;
      }

    async function getHotels() {
      console.log(url.searchParams.toString());
      const response = await fetch(
        `${base}/reservations/hotelsearch?${url.searchParams.toString()}`,
        {
          method: "GET"
        }
      );
  
      const result = await response.json();
      console.log(result);
      return result;
    }

  return {
    user: locals.user,
    citieshotels: getCitiesHotels(),
    hotels: getHotels()
  };
}
