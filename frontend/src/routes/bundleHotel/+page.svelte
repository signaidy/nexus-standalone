<script lang="ts">
    import HotelCard from "$lib/components/hotelseach/hotelCard.svelte"
    import HotelBar from "$lib/components/hotelBar/hotelBar.svelte";
    import HotelCardSkeleton from "$lib/components/hotelseach/hotelCardSkeleton.svelte"
    import { page } from "$app/stores";
  
    export let data;
    // export let form;
    // console.log(data);
    console.log(data.hotels.value)
  </script>
  
  <div
    class="min-h-[calc(100vh-5rem)] bg-[url('$lib/assets/home-background.jpg')] bg-cover bg-fixed"
  >
    <div class="flex flex-col container items-center p-8 h-full">
      <HotelBar cities={data.citieshotels}/>
      {#await data.hotels}
        {#each Array(3).fill(0) as _}
          <HotelCardSkeleton />
        {/each}
      {:then hotels}
        {#if hotels.length === 0}
          <div class="self-start font-bold font-lg mt-10 my-3 bg-background p-3 rounded-lg">No Hotels found on {$page.url.searchParams.get("city")}</div>
        {:else}
          <div class="self-start font-bold font-lg mt-10 my-3 bg-background p-3 rounded-lg">Results for hotels on {hotels[0].location.city} for {$page.url.searchParams.get("guests")} guests</div>
        {/if}
        {#each hotels as hotel}
          <HotelCard {hotel} user={data.user} guests={$page.url.searchParams.get("guests")}/>
        {/each}
      {:catch error}
        <p>{error.message}</p>
      {/await}
    </div>
  </div>
  