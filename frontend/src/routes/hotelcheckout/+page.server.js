import { PUBLIC_BACKEND_URL } from '$env/static/public';

export const actions = {
  default: async({request, cookies}) => {
    const token = cookies.get('token');
    // Generate a random string based on the current date
    const currentDate = new Date();
    const bundle = currentDate.toISOString().replace(/\W/g, '');
    console.log(bundle);
      const formData = await request.formData();
      const providerId = formData.get("providerId")
      const flightProviderId = formData.get("FlightProviderId")
      const flightId = formData.get("flight_id");
      const roundTrip = formData.get("roundTrip");
      const scale = formData.get("scale");
      const Rscale = formData.get("Rscale"); 
      let hotelid = formData.get("hotelid");
      let hotelname = formData.get("hotelname");
      let user_id = formData.get("user_id");
      const checkin = formData.get("checkin");
      const checkout = formData.get("checkout");
      const price = formData.get("price")
      const random = formData.get("randomValue")
      const roomtype = formData.get("roomtype");
      const guests = formData.get("guests");
      const bedAmount = formData.get("bedAmount");
      let days = formData.get("days");
      let city = formData.get("city");
      let beds = formData.get("beds");
      const totalprice = formData.get("totalprice");
      const body = {
        userid: user_id,
        hotelId: hotelid,
        hotel:hotelname,
        dateStart: checkin,
        dateEnd: checkout,
        roomType: roomtype,
        reservationNumber: random,
        location: city,
        bedSize: beds, 
        price: price, 
        bedAmount: bedAmount,
        totalDays: days, 
        totalPrice: totalprice, 
        guests: guests,
        bundle: bundle
      };
      const response = await fetch(`${PUBLIC_BACKEND_URL}/reservations?providerId=${providerId}`, {
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
      if (flightId != null) {
      let method = formData.get("paymenth_method");
      let amount = formData.get("passengers");
      // Extracting form data
      const type = formData.get("category");
      const state = formData.get("state");
      let departureDate = formData.get("departureDate");
      const departureLocation = formData.get("departureLocation");
      const arrivalLocation = formData.get("arrivalLocation");
      const price = formData.get("price");
      const rating = formData.get("rating");
      // Format departureDate to 'yyyy-MM-dd'
      departureDate = departureDate.split(" ")[0];
      // Constructing the body object
      const body = {
        user_id: user_id,
        flight_id: flightId,
        type: type,
        state: state,
        userId: user_id,
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
      const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${flightProviderId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(body),
    });
    const result = await response.json();
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
          user_id: user_id,
          flight_id: flightId,
          type: type,
          state: state,
          userId: user_id,
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
        const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${flightProviderId}`, {
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
          user_id: user_id,
          flight_id: flightId,
          type: type,
          state: state,
          userId: user_id,
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
        const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${flightProviderId}`, {
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
            user_id: user_id,
            flight_id: flightId,
            type: type,
            state: state,
            userId: user_id,
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
          const response = await fetch(`${PUBLIC_BACKEND_URL}/flights/purchase/${amount}/${method}/${flightProviderId}`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(body),
          });
          const result = await response.json();
        }
      }}
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