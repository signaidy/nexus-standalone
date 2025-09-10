import { PUBLIC_BACKEND_URL } from '$env/static/public';

export function load({ locals }) {
  // async function getCities() {
  //   const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/avianca/cities`, {
  //     method: "GET",
  //   });

  //   const result = await response.json();
  //   return result;
  // }

  // async function getCitiesHotels() {
  //   const response = await fetch(`${PUBLIC_BACKEND_URL}/reservations/cities`, {
  //     method: "GET",
  //   });

  //   const result = await response.json();
  //   return result;
  // }

  return {
    user: locals.user,
    cities: ["Rome", "England"],
    citieshotels: ["Rome", "England"]
  };
}
