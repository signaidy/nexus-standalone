import { fail, redirect } from "@sveltejs/kit";
import { PUBLIC_BACKEND_URL } from '$env/static/public';

export function load({ locals, url }) {
    async function getCitiesHotels() {
        const response = await fetch(`${PUBLIC_BACKEND_URL}/reservations/cities`, {
          method: "GET",
        });
    
        const result = await response.json();
        return result;
      }

    async function getHotels() {
      console.log(url.searchParams.toString());
      const response = await fetch(
        `${PUBLIC_BACKEND_URL}/reservations/hotelsearch?${url.searchParams.toString()}`,
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
