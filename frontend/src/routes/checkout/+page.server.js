import { PUBLIC_BACKEND_URL } from '$env/static/public';

export const actions = {
  default: async({request, cookies}) => {
    const token = cookies.get('token');
    let bundle = null;
      const formData = await request.formData(); 
      const providerId = formData.get("providerId")
      const roundTrip = formData.get("roundTrip");
      const scale = formData.get("scale");
      const Rscale = formData.get("Rscale");
      let method = formData.get("paymenth_method");
      let amount = formData.get("passengers");
      // Extracting form data
      const userId = formData.get("user_id");
      const flightId = formData.get("flight_id");
      const type = formData.get("category");
      const state = formData.get("state");
      let departureDate = formData.get("departureDate");
      const departureLocation = formData.get("departureLocation");
      const arrivalLocation = formData.get("arrivalLocation");
      const price = formData.get("price");
      const rating = formData.get("rating");
      // Format departureDate to 'yyyy-MM-dd'
      departureDate = departureDate.split(" ")[0];
      // Generate a random string based on the current date
    if (roundTrip !=null || scale != null) { 
      const currentDate = new Date();
      bundle = currentDate.toISOString().replace(/\W/g, '');
    }
    console.log("Received POST request to purchase a flight for provider number:" + providerId)
      // Constructing the body object
      const body = {
        user_id: userId,
        flight_id: flightId,
        type: type,
        state: state,
        userId: userId,
        flightId: flightId,
        departureDate: departureDate,
        departureLocation: departureLocation,
        arrivalLocation: arrivalLocation,
        price: price,
        returnDate: null, // Assuming returnDate is always null for this case
        rating: rating,
        bundle: bundle
      };
      console.log(body);
      const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${providerId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(body),
    });
    const result = await response.json();
    console.log(result)
    if(!result.ok){
      if (scale != null) {
        const flightId = formData.get("Sflight_id");
        let departureDate = formData.get("SdepartureDate");
        const departureLocation = formData.get("SdepartureLocation");
        const arrivalLocation = formData.get("SarrivalLocation");
        const price = formData.get("Sprice");
        const rating = formData.get("Srating");
        // Format departureDate to 'yyyy-MM-dd'
        departureDate = departureDate.split(" ")[0];
        const body = {
          user_id: userId,
          flight_id: flightId,
          type: type,
          state: state,
          userId: userId,
          flightId: flightId,
          departureDate: departureDate,
          departureLocation: departureLocation,
          arrivalLocation: arrivalLocation,
          price: price,
          returnDate: null, // Assuming returnDate is always null for this case
          rating: rating,
          bundle: bundle
        };
        console.log(body);
        const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${providerId}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify(body),
        });
        const result = await response.json();
      }
      if (roundTrip != null) {
        const flightId = formData.get("Rflight_id");
        let departureDate = formData.get("RdepartureDate");
        const departureLocation = formData.get("RdepartureLocation");
        const arrivalLocation = formData.get("RarrivalLocation");
        const price = formData.get("Rprice");
        const rating = formData.get("Rrating");
        // Format departureDate to 'yyyy-MM-dd'
        departureDate = departureDate.split(" ")[0];
        const body = {
          user_id: userId,
          flight_id: flightId,
          type: type,
          state: state,
          userId: userId,
          flightId: flightId,
          departureDate: departureDate,
          departureLocation: departureLocation,
          arrivalLocation: arrivalLocation,
          price: price,
          returnDate: null, // Assuming returnDate is always null for this case
          rating: rating,
          bundle: bundle
        };
        console.log(body);
        const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${providerId}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify(body),
        });
        const result = await response.json();
        if (Rscale != null) {
          const flightId = formData.get("RSflight_id");
          let departureDate = formData.get("RSdepartureDate");
          const departureLocation = formData.get("RSdepartureLocation");
          const arrivalLocation = formData.get("RSarrivalLocation");
          const price = formData.get("RSprice");
          const rating = formData.get("RSrating");
          // Format departureDate to 'yyyy-MM-dd'
          departureDate = departureDate.split(" ")[0];
          const body = {
            user_id: userId,
            flight_id: flightId,
            type: type,
            state: state,
            userId: userId,
            flightId: flightId,
            departureDate: departureDate,
            departureLocation: departureLocation,
            arrivalLocation: arrivalLocation,
            price: price,
            returnDate: null, // Assuming returnDate is always null for this case
            rating: rating,
            bundle: bundle
          };
          console.log(body);
          const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${providerId}`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(body),
          });
          const result = await response.json();
        }
      }
      return {
          message: "Thanks for your purchase!"
      }
    } else {
      return{ 
          message: "Error"
      }
    }
  }
}