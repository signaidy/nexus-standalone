<script lang="ts">
  import RoomCard from "$lib/components/roomsearch/roomCard.svelte";
  import HotelBar from "$lib/components/hotelBar/hotelBar.svelte";
  import HotelCardSkeleton from "$lib/components/hotelseach/hotelCardSkeleton.svelte";
  import { page } from "$app/stores";

  export let data;
  console.log(data); 
  let city = $page.url.searchParams.get("city");
  // export let form;
</script>

<div
  class="min-h-[calc(100vh-5rem)] bg-[url('$lib/assets/home-background.jpg')] bg-cover bg-fixed"
>
  <div class="flex flex-col container items-center p-8 h-full">
    <HotelBar cities={data.citieshotels} />
    {#await data.rooms}
      {#each Array(3).fill(0) as _}
        <HotelCardSkeleton />
      {/each}
    {:then rooms}
      {#if rooms.length === 0}
        <div
          class="self-start font-bold font-lg mt-10 my-3 bg-background p-3 rounded-lg"
        >
          No Hotels found on {$page.url.searchParams.get("city")}
        </div>
      {:else}
        <!-- <div class="self-start font-bold font-lg mt-10 my-3 bg-background p-3 rounded-lg">Results for hotels on {rooms[0].location.city} for {$page.url.searchParams.get("guests")} guests</div> -->
      {/if}
      {#each Object.values(rooms) as room}
        <RoomCard
          {room}
          hotel = {rooms.hotelId}
          hotelname = {rooms.name}
          city = {city}
          user={data.user}
          guests={$page.url.searchParams.get("guests")}
        />
      {/each}
    {:catch error}
      <p>{error.message}</p>
    {/await}
  </div>
</div>
