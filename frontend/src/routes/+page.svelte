<script lang="ts">
  import SearchBar from "$lib/components/searchBar/searchBar.svelte";
  import FeaturedFlights from "$lib/components/home/featuredFlights.svelte";
  import Footer from "$lib/components/footer/footer.svelte";
  import { Plane, Hotel, Package } from "lucide-svelte";
  import HotelBar from "$lib/components/hotelBar/hotelBar.svelte";
    import BundlesearchBar from "$lib/components/bundleSearch/bundlesearchBar.svelte";

  export let data;

  let selectvalue = "PlaneSearch";
</script>

<div
  class="h-[calc(100vh-5rem)] bg-[url('$lib/assets/home-background.jpg')] bg-cover"
>
  <div class="flex flex-col container justify-center items-center h-full">
    <div class="flex flex-col bg-background/50 rounded-lg">
      <div class="p-5">
        <div class="flex justify-center items-baseline flex-wrap">
          <div class="flex m-2">
            <button
              on:click={() => (selectvalue = "PlaneSearch")}
              class="text-base rounded-r-none hover:scale-110 focus:outline-none flex justify-center px-4 py-2 rounded font-bold cursor-pointer
            hover:bg-gray-200
            bg-gray-100
            text-gray-700
            border duration-200 ease-in-out
            border-gray-600 transition"
            >
              <div class="flex leading-5">
                <Plane />
                <p>Airline search</p>
              </div>
            </button>
            <button
              on:click={() => (selectvalue = "HotelSearch")}
              class="text-base rounded-l-none border-l-0 hover:scale-110 focus:outline-none flex justify-center px-4 py-2 rounded font-bold cursor-pointer
            hover:bg-teal-200
            bg-gray-100
            text-gray-700
            border duration-200 ease-in-out
            border-gray-600 transition"
            >
              <div class="flex leading-5">
                <Hotel />
                <p>Hotel search</p>
              </div>
            </button>
            <button
              on:click={() => (selectvalue = "Packages")}
              class="text-base rounded-l-none border-l-0 hover:scale-110 focus:outline-none flex justify-center px-4 py-2 rounded font-bold cursor-pointer
          hover:bg-teal-200
          bg-teal-100
          text-teal-700
          border duration-200 ease-in-out
          border-teal-600 transition"
            >
              <div class="flex leading-5">
                <Package />
                <p>Build Package</p>
              </div>
            </button>
          </div>
        </div>
      </div>
      {#if selectvalue == "PlaneSearch"}
        <SearchBar cities={data.cities} />
      {:else if selectvalue == "HotelSearch"}
        <HotelBar cities={data.citieshotels}/>
      {:else if selectvalue == "Packages"}
        <BundlesearchBar cities={data.cities}/>
      {:else}
        <div></div>
      {/if}
    </div>
  </div>
</div>
<FeaturedFlights />
<Footer />
