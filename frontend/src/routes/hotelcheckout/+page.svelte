<script>
  import { selectedFlight, selectedCategory } from '$lib/stores/selectedFlight';
  import { MoveLeft } from "lucide-svelte";
  import avianca from "$lib/assets/Avianca-Ticket-logo.png";
  import visaImage from "$lib/assets/visa.png";
  import mastercardImage from "$lib/assets/mastercard.png";
  import { page } from "$app/stores";
    import { onMount } from 'svelte';
  export let data;
  export let isOpen = false;

  function getRandomNumber() {
    return Math.random();
  }
  
  let flight = null;
  let category = null;
  let totalBundle = null;
  let unsubscribe = null;
  let catUnsuscribe = null;
  if (selectedFlight) {
    unsubscribe = selectedFlight.subscribe(value => {
      flight = value;
    });
    catUnsuscribe = selectedCategory.subscribe(value =>{
      category = value;
    })
    console.log(flight)
  }
  const randomValue = getRandomNumber();
  let userid = data.user.userId;
  let hotelid = $page.url.searchParams.get("hotelid");
  let hotelname = $page.url.searchParams.get("hotelname");
  let providerId = $page.url.searchParams.get("providerId");
  let checkin = $page.url.searchParams.get("checkin").split(" ")[0];
  let checkout = $page.url.searchParams.get("checkout").split(" ")[0];
  let checkinDate = new Date(checkin);
  let checkoutDate = new Date(checkout);
  let differenceInMilliseconds = checkoutDate.getTime() - checkinDate.getTime();
  let days;
  if (differenceInMilliseconds == 0) {
    days = 1;
  } else {
    days = Math.ceil(differenceInMilliseconds / (1000 * 3600 * 24));
  }
  let roomtype = $page.url.searchParams.get("roomtype");
  let price = $page.url.searchParams.get("price");
  let guests = $page.url.searchParams.get("guests");
  let city = $page.url.searchParams.get("city");
  let beds = $page.url.searchParams.get("beds");
  let bedAmount = $page.url.searchParams.get("bedAmount");

  let card = "";
  let state = "active";
  
  if (flight) {
    if (category == "premium") {
      if (flight.scale && flight.returnFlight && flight.returnFlight.scale) {
        totalBundle = ((flight.businessPrice + flight.scale.businessPrice + flight.returnFlight.businessPrice + flight.returnFlight.scale.businessPrice) * guests) + (price * days);
      } else if (flight.scale && flight.returnFlight && !flight.returnFlight.scale) {
        totalBundle = ((flight.businessPrice + flight.scale.businessPrice + flight.returnFlight.businessPrice) * guests) + (price * days);
      } else if ((!flight.scale && flight.returnFlight && !flight.returnFlight.scale)) {
        totalBundle = ((flight.businessPrice + flight.returnFlight.businessPrice + flight.returnFlight.businessPrice) * guests) + (price * days);
      } else if ((!flight.scale && flight.returnFlight && flight.returnFlight.scale)) {
        totalBundle = ((flight.businessPrice + flight.returnFlight.businessPrice) * days) + (price * days);
      }
    } else {
      if (flight.scale && flight.returnFlight && flight.returnFlight.scale) {
        totalBundle = ((flight.businessPrice + flight.scale.businessPrice + flight.returnFlight.businessPrice + flight.returnFlight.scale.businessPrice) * guests) + (price * days);
      } else if (flight.scale && flight.returnFlight && !flight.returnFlight.scale) {
        totalBundle = ((flight.businessPrice + flight.scale.businessPrice + flight.returnFlight.businessPrice) * guests) + (price * days);
      } else if ((!flight.scale && flight.returnFlight && !flight.returnFlight.scale)) {
        totalBundle = ((flight.businessPrice + flight.returnFlight.businessPrice + flight.returnFlight.businessPrice)  * guests) + (price * days);
      } else if ((!flight.scale && flight.returnFlight && flight.returnFlight.scale)) {
        totalBundle = ((flight.businessPrice + flight.returnFlight.businessPrice) * guests) + (price * days);
      }
    }
  }
  const today = new Date();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  const year = today.getFullYear();

  const formattedDate = month + "/" + day + "/" + year;
  onMount(() => {
    return {unsubscribe, catUnsuscribe};
  });
</script>

{#if isOpen}
  <div
    class="fixed left-0 top-0 flex h-full w-full items-center justify-center bg-black bg-opacity-50 py-10"
  >
    <div
      class="max-h-full w-full max-w-xl overflow-y-auto sm:rounded-2xl bg-white"
    >
      <div class="w-full">
        <div class="m-8 my-20 max-w-[400px] mx-auto">
          <div class="mb-8">
            <h1 class="mb-4 text-3xl font-extrabold">
              Thank you for your purchase
            </h1>
            <p class="text-gray-600">
              Now you can decide to continue searching for other travels or go
              to see your receipt
            </p>
          </div>
          <div class="space-y-4">
            <a
              href="/"
              class="p-3 bg-black rounded-full text-white w-full font-semibold"
              >Continue buying</a
            >
            <a
              href="user/shopping_history"
              class="p-3 bg-white border rounded-full w-full font-semibold"
              >Go check your receipt</a
            >
          </div>
        </div>
      </div>
    </div>
  </div>
{/if}

{#if flight == null}
<form method="POST">
  <div class="min-w-screen min-h-screen bg-gray-50 py-3">
    <div class="px-5">
      <div class="mb-0">
        <a
          href="/"
          class="focus:outline-none hover:underline text-gray-500 text-sm"
          ><MoveLeft /></a
        >
      </div>
      <div class="mb-2">
        <h1 class="text-3xl md:text-5xl font-bold text-gray-600">Checkout</h1>
      </div>
      <div class="mb-5 text-gray-400">
        <a href="/" class="focus:outline-none hover:underline text-gray-500"
          >Home</a
        >
        /
        <a href="#" class="focus:outline-none hover:underline text-gray-500"
          >Cart</a
        >
        / <span class="text-gray-600">Checkout</span>
      </div>
    </div>
    <div
      class="w-full bg-white border-t border-b border-gray-200 px-5 py-10 text-gray-800"
    >
      <div class="w-full">
        <div class="-mx-3 md:flex items-start">
          <div class="px-3 md:w-7/12 lg:pr-10">
            <div
              class="w-full mx-auto text-gray-800 font-light mb-6 border-b border-gray-200 pb-6"
            >
              <div class="w-full flex items-center">
                <div
                  class="overflow-hidden rounded-lg w-16 h-16 bg-gray-50 border border-gray-200"
                >
                  <img src={avianca} alt="" />
                </div>
                <div class="flex-grow pl-3">
                  <h6 class="font-semibold uppercase text-gray-600">
                    {roomtype}
                  </h6>
                  <p class="text-gray-400">Amount of nights: {days}</p>
                </div>
                <div>
                  <span class="font-semibold text-gray-600 text-xl"
                    >${price * days}</span
                  ><span class="font-semibold text-gray-600 text-sm">.00</span>
                </div>
              </div>
            </div>
            <div class="mb-6 pb-6 border-b border-gray-200 text-gray-800">
              <div class="w-full flex mb-3 items-center">
                <div class="flex-grow">
                  <span class="text-gray-600">Price per night</span>
                </div>
                <div class="pl-3">
                  <span class="font-semibold">${price}.00</span>
                </div>
              </div>
              <div class="w-full flex items-center">
                <div class="flex-grow">
                  <span class="text-gray-600">Discount</span>
                </div>
                <div class="pl-3">
                  <span class="font-semibold">$00.00</span>
                </div>
              </div>
            </div>
            <div
              class="mb-6 pb-6 border-b border-gray-200 md:border-none text-gray-800 text-xl"
            >
              <div class="w-full flex items-center">
                <div class="flex-grow">
                  <span class="text-gray-600">Total</span>
                </div>
                <div class="pl-3">
                  <span class="font-semibold">${price * days}.00</span>
                </div>
              </div>
            </div>
          </div>
          <div class="px-3 md:w-5/12">
            <div
              class="w-full mx-auto rounded-lg bg-white border border-gray-200 p-3 text-gray-800 font-light mb-6"
            >
              <div class="w-full flex mb-3 items-center">
                <div class="w-38">
                  <span class="text-gray-600 font-semibold"
                    >Amount of guests:</span
                  >
                </div>
                <div class="flex-grow pl-3">
                  <span>{guests}</span>
                </div>
                <div class="w-38">
                  <span class="text-gray-600 font-semibold"
                    >Amount of beds:</span
                  >
                </div>
                <div class="flex-grow pl-3">
                  <span>{bedAmount}</span>
                </div>
                <div class="w-38">
                  <span class="text-gray-600 font-semibold">City:</span>
                </div>
                <div class="flex-grow pl-3">
                  <span>{city}</span>
                </div>
              </div>
            </div>
            <div
              class="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6"
            >
              <div class="w-full p-3 border-b border-gray-200">
                <div class="mb-5">
                  <label for="type1" class="flex cursor-pointer items-center">
                    <input
                      on:click={() => (card = "Visa")}
                      type="radio"
                      value="visa"
                      class="form-radio h-5 w-5 text-indigo-500"
                      name="type"
                      id="type1"
                    />
                    <img src={visaImage} class="ml-3 h-12" />
                    <input
                      on:click={() => (card = "MasterCard")}
                      type="radio"
                      class="form-radio h-5 w-20 text-indigo-500"
                      value="mastercard"
                      name="type"
                      id="type1"
                    />
                    <img src={mastercardImage} class="ml-1 h-12" />
                  </label>
                </div>
                <div>
                  <div class="mb-3">
                    <label class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                      >Name on card</label
                    >
                    <div>
                      <input
                        class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                        type="text"
                      />
                    </div>
                  </div>
                  <div class="mb-3">
                    <label class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                      >Card number</label
                    >
                    <div>
                      <input
                        class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                        placeholder="0000-0000-0000-0000"
                        type="text"
                        required
                      />
                    </div>
                  </div>
                  <div class="mb-3 -mx-2 flex items-end">
                    <div class="px-2 w-1/4">
                      <label
                        class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                        >Expiration date</label
                      >
                      <div>
                        <select
                          class="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer"
                        >
                          <option value="01">01 - January</option>
                          <option value="02">02 - February</option>
                          <option value="03">03 - March</option>
                          <option value="04">04 - April</option>
                          <option value="05">05 - May</option>
                          <option value="06">06 - June</option>
                          <option value="07">07 - July</option>
                          <option value="08">08 - August</option>
                          <option value="09">09 - September</option>
                          <option value="10">10 - October</option>
                          <option value="11">11 - November</option>
                          <option value="12">12 - December</option>
                        </select>
                      </div>
                    </div>
                    <div class="px-2 w-1/4">
                      <select
                        class="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer"
                      >
                        <option value="2020">2020</option>
                        <option value="2021">2021</option>
                        <option value="2022">2022</option>
                        <option value="2023">2023</option>
                        <option value="2024">2024</option>
                        <option value="2025">2025</option>
                        <option value="2026">2026</option>
                        <option value="2027">2027</option>
                        <option value="2028">2028</option>
                        <option value="2029">2029</option>
                      </select>
                    </div>
                    <div class="px-2 w-1/4">
                      <label
                        class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                        >Security code</label
                      >
                      <div>
                        <input
                          class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                          placeholder="000"
                          type="text"
                          required
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <button
                type="submit"
                class="block w-full max-w-xs mx-auto bg-indigo-500 hover:bg-indigo-700 focus:bg-indigo-700 text-white rounded-lg px-3 py-2 font-semibold"
                ><i class="mdi mdi-lock-outline mr-1"></i>Pay Now</button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
    <input type="hidden" name="hotelid" value={hotelid} />
    <input type="hidden" name="providerId" value={providerId} />
    <input type="hidden" name="hotelname" value={hotelname} />
    <input type="hidden" name="randomValue" value={randomValue} />
    <input type="hidden" name="user_id" value={userid} />
    <input type="hidden" name="checkin" value={checkin} />
    <input type="hidden" name="checkout" value={checkout} />
    <input type="hidden" name="roomtype" value={roomtype} />
    <input type="hidden" name="guests" value={guests} />
    <input type="hidden" name="days" value={days} />
    <input type="hidden" name="price" value={price} />
    <input type="hidden" name="city" value={city} />
    <input type="hidden" name="beds" value={beds} />
    <input type="hidden" name="bedAmount" value={bedAmount} />
    <input type="hidden" name="totalprice" value={price * days} />
  </div>
</form>
{:else}
  <form method="POST">
    <div class="min-w-screen min-h-screen bg-gray-50 py-3">
      <div class="px-5">
        <div class="mb-0">
          <a
            href="/"
            class="focus:outline-none hover:underline text-gray-500 text-sm"
            ><MoveLeft /></a
          >
        </div>
        <div class="mb-2">
          <h1 class="text-3xl md:text-5xl font-bold text-gray-600">Checkout</h1>
        </div>
        <div class="mb-5 text-gray-400">
          <a href="/" class="focus:outline-none hover:underline text-gray-500"
            >Home</a
          >
          /
          <a href="#" class="focus:outline-none hover:underline text-gray-500"
            >Cart</a
          >
          / <span class="text-gray-600">Checkout</span>
        </div>
      </div>
      <div
        class="w-full bg-white border-t border-b border-gray-200 px-5 py-10 text-gray-800"
      >
        <div class="w-full">
          <div class="-mx-3 md:flex items-start">
            <div class="px-3 md:w-7/12 lg:pr-10">
              <div
                class="w-full mx-auto text-gray-800 font-light mb-6 border-b border-gray-200 pb-6"
              >
                <div class="w-full flex items-center">
                  <div
                    class="overflow-hidden rounded-lg w-16 h-16 bg-gray-50 border border-gray-200"
                  >
                    <img src={avianca} alt="" />
                  </div>
                  <div class="flex-grow pl-3">
                    <h4 class="font-semibold uppercase text-gray-600">
                      Hotel
                    </h4>
                    <h6 class="font-semibold uppercase text-gray-600">
                      {roomtype}
                    </h6>
                    <p class="text-gray-400">Amount of nights: {days}</p>
                  </div>
                  <div>
                    <span class="font-semibold text-gray-600 text-xl"
                      >${price * days}</span
                    ><span class="font-semibold text-gray-600 text-sm">.00</span>
                  </div>
                </div>
              </div>
              <div class="mb-6 pb-6 border-b border-gray-200 text-gray-800">
                <div class="w-full flex mb-3 items-center">
                  <div class="flex-grow">
                    <span class="text-gray-600">Price per night</span>
                  </div>
                  <div class="pl-3">
                    <span class="font-semibold">${price}.00</span>
                  </div>
                </div>
                <div class="w-full flex items-center">
                  <div class="flex-grow">
                    <span class="text-gray-600">Discount</span>
                  </div>
                  <div class="pl-3">
                    <span class="font-semibold">$00.00</span>
                  </div>
                </div>
              </div>
              <div
                class="mb-6 pb-6 border-b border-gray-200 md:border-none text-gray-800 text-xl"
              >
                <div class="w-full flex items-center">
                  <div class="flex-grow">
                    <span class="text-gray-600">Total</span>
                  </div>
                  <div class="pl-3">
                    <span class="font-semibold">${price * days}.00</span>
                  </div>
                </div>
              </div>
                    <div
                      class="w-full mx-auto text-gray-800 font-light mb-6 border-b border-gray-200 pb-6"
                    >
                      <div class="w-full flex items-center">
                        <div
                          class="overflow-hidden rounded-lg w-16 h-16 bg-gray-50 border border-gray-200"
                        >
                          <img src={avianca} alt="" />
                        </div>
                        <div class="flex-grow pl-3">
                          <h3 class="font-semibold uppercase text-gray-600">
                            Flight
                          </h3>
                          <h6 class="font-semibold uppercase text-gray-600">
                            {category} Flight
                          </h6>
                          <p class="text-gray-400">x {guests}</p>
                        </div>
                        {#if flight?.returnFlight == null}
                          {#if flight?.scale == null}
                            {#if category == 'economy'}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${flight?.touristPrice * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {:else}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${flight?.businessPrice * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {/if}
                          {:else}
                            {#if category == 'economy'}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${(flight?.touristPrice + flight?.scale.touristPrice) * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {:else}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${(flight?.businessPrice + flight?.scale.businessPrice) * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {/if}
                          {/if}
                        {:else}
                          {#if flight?.scale == null}
                            {#if category == 'economy'}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${(flight?.touristPrice + flight?.returnFlight.touristPrice) * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {:else}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${(flight?.businessPrice + flight?.returnFlight.businessPrice) * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {/if}
                          {:else}
                            {#if category == 'economy'}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${(flight?.touristPrice + flight?.returnFlight.touristPrice + flight?.scale.touristPrice) * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {:else}
                              <div>
                                <span class="font-semibold text-gray-600 text-xl">${(flight?.businessPrice + flight?.returnFlight.businessPrice + flight?.scale.businessPrice) * guests}</span>
                                <span class="font-semibold text-gray-600 text-sm">.00</span>
                              </div>
                            {/if}
                          {/if}
                        {/if}
                      </div>
                    </div>
                    <div class="mb-6 pb-6 border-b border-gray-200 text-gray-800">
                  <div class="w-full flex mb-3 items-center">
                    <div class="flex-grow">
                      <span class="text-gray-600">Unitary price</span>
                    </div>
                    {#if flight?.returnFlight == null}
                      {#if flight?.scale == null}
                        {#if category == 'economy'}
                          <div class="pl-3">
                            <span class="font-semibold">${flight?.touristPrice}.00</span>
                          </div>
                          {:else}
                          <div class="pl-3">
                            <span class="font-semibold">${flight?.businessPrice}.00</span>
                          </div>
                        {/if}
                        {:else}
                          {#if category == 'economy'}
                            <div class="pl-3">
                              <span class="font-semibold">${(flight?.touristPrice + flight?.scale.touristPrice)}.00</span>
                            </div>
                            {:else}
                            <div class="pl-3">
                              <span class="font-semibold">${(flight?.businessPrice + flight?.scale.businessPrice)}.00</span>
                            </div>
                          {/if}
                      {/if}
                    {:else}
                      {#if flight?.scale == null}
                        {#if category == 'economy'}
                        <div class="pl-3">
                          <span class="font-semibold">${(flight?.touristPrice + flight?.returnFlight.touristPrice)}.00</span>
                        </div>
                        {:else}
                        <div class="pl-3">
                          <span class="font-semibold">${(flight?.businessPrice + flight?.returnFlight.businessPrice)}.00</span>
                        </div>
                      {/if}
                      {:else}
                        {#if category == 'economy'}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.touristPrice + flight?.returnFlight.touristPrice + flight?.scale.touristPrice)}.00</span>
                          </div>
                          {:else}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.businessPrice + flight?.returnFlight.businessPrice + flight?.scale.businessPrice)}.00</span>
                          </div>
                        {/if}
                      {/if}
                    {/if}
                  </div>
                  <div class="w-full flex items-center">
                    <div class="flex-grow">
                      <span class="text-gray-600">Discount</span>
                    </div>
                    <div class="pl-3">
                      <span class="font-semibold">$00.00</span>
                    </div>
                  </div>
                </div>
                <div
                  class="mb-6 pb-6 border-b border-gray-200 md:border-none text-gray-800 text-xl"
                >
                  <div class="w-full flex items-center">
                    <div class="flex-grow">
                      <span class="text-gray-600">Total</span>
                    </div>
                    {#if flight?.returnFlight == null}
                        {#if flight?.scale == null}
                          {#if category == 'economy'}
                            <div class="pl-3">
                              <span class="font-semibold">${flight?.touristPrice * guests}.00</span>
                            </div>
                          {:else}
                            <div class="pl-3">
                              <span class="font-semibold">${flight?.businessPrice * guests}.00</span>
                            </div>
                          {/if}
                      {:else}
                        {#if category == 'economy'}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.touristPrice + flight?.scale.touristPrice) * guests}.00</span>
                          </div>
                        {:else}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.businessPrice + flight?.scale.businessPrice) * guests}.00</span>
                          </div>
                        {/if}
                      {/if}
                    {:else}
                      {#if flight?.scale == null}
                        {#if category == 'economy'}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.touristPrice + flight?.returnFlight.touristPrice)}.00</span>
                          </div>
                          {:else}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.businessPrice + flight?.returnFlight.businessPrice)}.00</span>
                          </div>
                        {/if}
                      {:else}
                        {#if category == 'economy'}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.touristPrice + flight?.returnFlight.touristPrice + flight?.scale.touristPrice) * guests}.00</span>
                          </div>
                        {:else}
                          <div class="pl-3">
                            <span class="font-semibold">${(flight?.businessPrice + flight?.returnFlight.businessPrice + flight?.scale.businessPrice) * guests}.00</span>
                          </div>
                        {/if}
                      {/if}
                    {/if}
                </div>
                <div
                  class="mb-6 pb-6 border-b border-gray-200 md:border-none text-gray-800 text-xl"
                >
                  <div class="w-full flex items-center">
                    <div class="flex-grow">
                      <span class="text-gray-600">Total Package</span>
                    </div>
                    <div class="pl-3">
                      <span class="font-semibold">${totalBundle}.00</span>
                    </div>
                  </div>
                </div>
                <div class="mb-6 pb-6 border-b border-gray-200 text-gray-800">
                  <div class="w-full flex items-center">
                    <div class="flex-grow">
                      <span class="text-gray-600">Discount (5%)</span>
                    </div>
                    <div class="pl-3">
                      <span class="font-semibold">${totalBundle * 0.05}</span>
                    </div>
                  </div>
                </div>
                <div
                  class="mb-6 pb-6 border-b border-gray-200 md:border-none text-gray-800 text-xl"
                >
                  <div class="w-full flex items-center">
                    <div class="flex-grow">
                      <span class="text-gray-600">Total</span>
                    </div>
                    <div class="pl-3">
                      <span class="font-semibold">${totalBundle * 0.95}</span>
                    </div>
                  </div>
                </div>
            </div>
          </div>
            <div class="px-3 md:w-5/12">
              <div
                class="w-full mx-auto rounded-lg bg-white border border-gray-200 p-3 text-gray-800 font-light mb-6"
              >
                <div class="w-full flex mb-3 items-center">
                  <div class="w-38">
                    <span class="text-gray-600 font-semibold"
                      >Amount of guests:</span
                    >
                  </div>
                  <div class="flex-grow pl-3">
                    <span>{guests}</span>
                  </div>
                  <div class="w-38">
                    <span class="text-gray-600 font-semibold"
                      >Amount of beds:</span
                    >
                  </div>
                  <div class="flex-grow pl-3">
                    <span>{bedAmount}</span>
                  </div>
                  <div class="w-38">
                    <span class="text-gray-600 font-semibold">City:</span>
                  </div>
                  <div class="flex-grow pl-3">
                    <span>{city}</span>
                  </div>
                </div>
              </div>
              <div
              class="w-full mx-auto rounded-lg bg-white border border-gray-200 p-3 text-gray-800 font-light mb-6"
            >
              <div class="w-full flex mb-3 items-center">
                <div class="w-32">
                  <span class="text-gray-600 font-semibold">Leaving from:</span>
                </div>
                <div class="flex-grow pl-3">
                  <span>{flight?.originCityName}</span>
                </div>
                {#if flight?.scale != null}
                  <div class="w-32">
                    <span class="text-gray-600 font-semibold">Scale on:  </span>
                  </div>
                  <div class="flex-grow pl-3">
                    <span>{flight?.destinationCityName}</span>
                  </div>
                  {#if flight?.returnFlight.scale != null}                  
                    <div class="flex-grow pl-3">
                      <span class="text-gray-600 font-semibold">and</span>
                    </div>
                    <div class="flex-grow pl-3">
                      <span>{flight?.returnFlight.destinationCityName}</span>
                    </div>
                  {/if}
                {/if}
              </div>
              <div class="w-full flex items-center">
                <div class="w-32">
                  <span class="text-gray-600 font-semibold">Going to: </span>
                </div>
                {#if flight?.scale != null}
                  <div class="flex-grow pl-3">
                    <span>{flight?.scale.destinationCityName}</span>
                  </div>
                {:else}
                <div class="flex-grow pl-3">
                  <span>{flight?.destinationCityName}</span>
                </div>
                {/if}
                {#if flight?.returnFlight != null}
                  <div class="flex-grow pl-3">
                    <span>Round Trip</span>
                  </div>
                {/if}
              </div>
            </div>
              <div
                class="w-full mx-auto rounded-lg bg-white border border-gray-200 text-gray-800 font-light mb-6"
              >
                <div class="w-full p-3 border-b border-gray-200">
                  <div class="mb-5">
                    <label for="type1" class="flex cursor-pointer items-center">
                      <input
                        on:click={() => (card = "Visa")}
                        type="radio"
                        value="visa"
                        class="form-radio h-5 w-5 text-indigo-500"
                        name="type"
                        id="type1"
                      />
                      <img src={visaImage} class="ml-3 h-12" />
                      <input
                        on:click={() => (card = "MasterCard")}
                        type="radio"
                        class="form-radio h-5 w-20 text-indigo-500"
                        value="mastercard"
                        name="type"
                        id="type1"
                      />
                      <img src={mastercardImage} class="ml-1 h-12" />
                    </label>
                  </div>
                  <div>
                    <div class="mb-3">
                      <label class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                        >Name on card</label
                      >
                      <div>
                        <input
                          class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                          type="text"
                        />
                      </div>
                    </div>
                    <div class="mb-3">
                      <label class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                        >Card number</label
                      >
                      <div>
                        <input
                          class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                          placeholder="0000-0000-0000-0000"
                          type="text"
                          required
                        />
                      </div>
                    </div>
                    <div class="mb-3 -mx-2 flex items-end">
                      <div class="px-2 w-1/4">
                        <label
                          class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                          >Expiration date</label
                        >
                        <div>
                          <select
                            class="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer"
                          >
                            <option value="01">01 - January</option>
                            <option value="02">02 - February</option>
                            <option value="03">03 - March</option>
                            <option value="04">04 - April</option>
                            <option value="05">05 - May</option>
                            <option value="06">06 - June</option>
                            <option value="07">07 - July</option>
                            <option value="08">08 - August</option>
                            <option value="09">09 - September</option>
                            <option value="10">10 - October</option>
                            <option value="11">11 - November</option>
                            <option value="12">12 - December</option>
                          </select>
                        </div>
                      </div>
                      <div class="px-2 w-1/4">
                        <select
                          class="form-select w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors cursor-pointer"
                        >
                          <option value="2020">2020</option>
                          <option value="2021">2021</option>
                          <option value="2022">2022</option>
                          <option value="2023">2023</option>
                          <option value="2024">2024</option>
                          <option value="2025">2025</option>
                          <option value="2026">2026</option>
                          <option value="2027">2027</option>
                          <option value="2028">2028</option>
                          <option value="2029">2029</option>
                        </select>
                      </div>
                      <div class="px-2 w-1/4">
                        <label
                          class="text-gray-600 font-semibold text-sm mb-2 ml-1"
                          >Security code</label
                        >
                        <div>
                          <input
                            class="w-full px-3 py-2 mb-1 border border-gray-200 rounded-md focus:outline-none focus:border-indigo-500 transition-colors"
                            placeholder="000"
                            type="text"
                            required
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div>
                <button
                  type="submit"
                  class="block w-full max-w-xs mx-auto bg-indigo-500 hover:bg-indigo-700 focus:bg-indigo-700 text-white rounded-lg px-3 py-2 font-semibold"
                  ><i class="mdi mdi-lock-outline mr-1"></i>Pay Now</button
                >
              </div>
            </div>
          </div>
        </div>
      </div>
      <input type="hidden" name="hotelid" value={hotelid} />
      <input type="hidden" name="hotelname" value={hotelname} />
      <input type="hidden" name="providerId" value={providerId} />
      <input type="hidden" name="randomValue" value={randomValue} />
      <input type="hidden" name="user_id" value={userid} />
      <input type="hidden" name="checkin" value={checkin} />
      <input type="hidden" name="checkout" value={checkout} />
      <input type="hidden" name="roomtype" value={roomtype} />
      <input type="hidden" name="guests" value={guests} />
      <input type="hidden" name="days" value={days} />
      <input type="hidden" name="price" value={price*0.95} />
      <input type="hidden" name="city" value={city} />
      <input type="hidden" name="beds" value={beds} />
      <input type="hidden" name="bedAmount" value={bedAmount} />
      <input type="hidden" name="totalprice" value={price * 0.95 * days} />
      <input type="hidden" name="paymenth_method" value={card} />
      <input type="hidden" name="passengers" value={guests} />
      <input type="hidden" name="flight_id" value={flight?.flightId} />
      <input type="hidden" name="category" value={category} />
      <input type="hidden" name="state" value={state} />
      <input type="hidden" name="departureDate" value={flight?.departureDate} />
      <input type="hidden" name="departureLocation" value={flight?.originCityName} />
      <input type="hidden" name="arrivalLocation" value={flight?.destinationCityName} />
      <input type="hidden" name="returnDate" value={flight?.returnDate} />
      <input type="hidden" name="rating" value={flight?.rating.average} />
      <input type="hidden" name="FlightProviderId" value={flight?.providerId} />
      {#if category === 'economy'}
        <input type="hidden" name="category" value="economy" />
        <input type="hidden" name="price" value={flight?.touristPrice * 0.95} />
      {:else if category === 'premium'}
        <input type="hidden" name="category" value="premium" />
        <input type="hidden" name="price" value={flight?.businessPrice * 0.95} />
      {/if}
      {#if flight?.returnFlight != null}
        <input type="hidden" name="roundTrip" value="1" />
        <input type="hidden" name="Rflight_id" value={flight?.returnFlight.flightId} />
        <input type="hidden" name="RdepartureDate" value={flight?.returnFlight.departureDate} />
        <input type="hidden" name="RdepartureLocation" value={flight?.returnFlight.originCityName} />
        <input type="hidden" name="RarrivalLocation" value={flight?.returnFlight.destinationCityName} />
        <input type="hidden" name="RreturnDate" value={flight?.returnFlight.returnDate} />
        <input type="hidden" name="Rrating" value={flight?.returnFlight.rating.average} />
        <input type="hidden" name="Rprice" value={flight?.returnFlight.touristPrice * 0.95} />
        {#if flight?.returnFlight.scale != null}
          <input type="hidden" name="Rscale" value="1" />
          <input type="hidden" name="RSflight_id" value={flight?.returnFlight.scale.flightId} />
          <input type="hidden" name="RSdepartureDate" value={flight?.returnFlight.scale.departureDate} />
          <input type="hidden" name="RSdepartureLocation" value={flight?.returnFlight.scale.originCityName} />
          <input type="hidden" name="RSarrivalLocation" value={flight?.returnFlight.scale.destinationCityName} />
          <input type="hidden" name="RSreturnDate" value={flight?.returnFlight.scale.returnDate} />
          <input type="hidden" name="RSrating" value={flight?.returnFlight.scale.rating.average} />
          <input type="hidden" name="RSprice" value={flight?.returnFlight.scale.touristPrice * 0.95} />
        {/if}
      {/if}
      {#if flight?.scale != null}
        <input type="hidden" name="scale" value="1" />
        <input type="hidden" name="Sflight_id" value={flight?.scale.flightId} />
        <input type="hidden" name="SdepartureDate" value={flight?.scale.departureDate} />
        <input type="hidden" name="SdepartureLocation" value={flight?.scale.originCityName} />
        <input type="hidden" name="SarrivalLocation" value={flight?.scale.destinationCityName} />
        <input type="hidden" name="SreturnDate" value={flight?.scale.returnDate} />
        <input type="hidden" name="Srating" value={flight?.scale.rating.average} />
        <input type="hidden" name="Sprice" value={flight?.scale.touristPrice * 0.95} />
      {/if}
    </div>
  </form>
{/if}
