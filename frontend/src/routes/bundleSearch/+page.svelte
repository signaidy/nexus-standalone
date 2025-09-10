<script lang="ts">
  import SearchBar from "$lib/components/bundleSearch/bundlesearchBar.svelte";
  import FlightCard from "$lib/components/bundleSearch/flightCard.svelte";
  import FlightCardSkeleton from "$lib/components/bundleSearch/flightCardSkeleton.svelte";
  import { page } from "$app/stores";

  export let data;
  export let form;
  console.log(data);
</script>

<div
  class="min-h-[calc(100vh-5rem)] bg-[url('$lib/assets/home-background.jpg')] bg-cover bg-fixed"
>
  <div class="flex flex-col container items-center p-8 h-full">
    <SearchBar cities={data.cities}/>
    {#await data.flights}
      {#each Array(3).fill(0) as _}
        <FlightCardSkeleton />
      {/each}
    {:then flights}
      {#if flights.length === 0}
        <div class="self-start font-bold font-lg mt-5 my-3 bg-background p-3 rounded-lg">No flights found for specified cities on {$page.url.searchParams.get("departureDay")} for {$page.url.searchParams.get("passengers")} passengers</div>
      {:else}
        <div class="self-start font-bold font-lg mt-5 my-3 bg-background p-3 rounded-lg">Results for flights from {flights[0].originCityName} to {flights[0].destinationCityName} on {$page.url.searchParams.get("departureDay")} for {$page.url.searchParams.get("passengers")} passengers</div>
      {/if}
      {#each flights as flight}
        <FlightCard {flight} {form} user={data.user} passengers={$page.url.searchParams.get("passengers")}/>
      {/each}
    {:catch error}
      <p>{error.message}</p>
    {/await}
  </div>
</div>
