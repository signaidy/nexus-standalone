import { fail } from "@sveltejs/kit";
import { PUBLIC_BACKEND_URL } from '$env/static/public';

export async function load({ locals, cookies }) {
  async function getProviders() {
    const token = cookies.get("token")
    const response = await fetch(`${PUBLIC_BACKEND_URL}/providers`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    const result = await response.json();
    return result;
  }

  return { user: locals.user, providers: getProviders() };
}

export const actions = {
    updateProvider: async ({ request, cookies }) => {
      const token = cookies.get("token");
      const data = await request.formData();

      for (const [, value] of data) {
        if (value === "" || value === "undefined") {
          return fail(400, {
            error: "Please fill in all the fields",
          });
        }
      }

      try {
        const bodyData: {
          providerName: FormDataEntryValue | null;
          providerUrl: FormDataEntryValue | null;
          type: FormDataEntryValue | null;
          percentageDiscount: FormDataEntryValue | null;
          gainsHotel?: FormDataEntryValue | null; // Declaring gainsHotel as an optional property
          gainsFlights?: FormDataEntryValue | null; // Declaring gainsFlights as an optional property
        } = {
          providerName: data.get("providerName"),
          providerUrl: data.get("providerUrl"),
          type: data.get("type"),
          percentageDiscount: data.get("percentage"),
        };

        // Check the type of the provider and assign gains accordingly
        if (data.get("type") === 'HOTEL') {
          bodyData.gainsHotel = data.get("gains");
        } else if (data.get("type") === 'AEROLINEA') {
          bodyData.gainsFlights = data.get("gains");
        }

        const response = await fetch(`${PUBLIC_BACKEND_URL}/providers/${data.get("providerId")}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify(bodyData),
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
  },createProvider: async ({ request, cookies }) => {
    console.log("Received POST request to create a new provider");
    const token = cookies.get("token");
    const formData = await request.formData();
    const type = formData.get("type");
      const gains = type === "HOTEL" ? formData.get("gainsHotel") : formData.get("gainsFlight");
      const requestData = {
        providerName: formData.get("providerName"),
        providerUrl: formData.get("providerUrl"),
        type: type,
        percentageDiscount: parseFloat(formData.get("percentageDiscount")),
        [type === "HOTEL" ? "gainsHotel" : "gainsFlight"]: gains
      };
    
    console.log(requestData);
    try {
      const response = await fetch(`${PUBLIC_BACKEND_URL}/providers`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(requestData),
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
  },deleteProvider: async ({request, cookies}) => {
    console.log("Received DELETE request to delete a provider");
    const token = cookies.get("token");
    let id = (await request.formData()).get("providerId")
    try {
      const response = await fetch(`${PUBLIC_BACKEND_URL}/providers/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        }
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
  }
};
