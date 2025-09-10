<script lang="ts">
  import { Star, MapPin } from "lucide-svelte";
  import { Button } from "$lib/components/ui/button";
  import HotelCardModal from "$lib/components/hotelseach/hotelCardModal.svelte";
  import { goto } from "$app/navigation";
  import type { DateRange } from "bits-ui";
  import { page } from "$app/stores";

  export let hotel;
  console.log(hotel);
  export let guests: string | null;
  export let user: User;
  // export let form;
  let isValid = true;
  let checkin = $page.url.searchParams.get("check-in");
  let checkout = $page.url.searchParams.get("check-out");

  $page.url.searchParams.get("guests")

  function searchRooms(hotel_id, city) {
    if (hotel_id && city && checkin && checkout && guests) {
      const searchParams = new URLSearchParams();
      searchParams.append("id", hotel_id);
      searchParams.append("city", city);
      searchParams.append("providerId", hotel.providerId);
      searchParams.append("check-in", checkin);
      searchParams.append("check-out", checkout);
      searchParams.append("guests", guests.toString());
      goto(`/roomsearch?${searchParams.toString()}`);
    } else {
      isValid = false;
    }
  }
</script>

<article class="border rounded-md grid-cols-[0.75fr_1fr] grid h-72">
  <div class="relative">
    <img
      src={hotel.picture}
      alt={`${hotel.name} Hotel Image`}
      class="object-cover rounded-l-md"
    />
  </div>
  <div class="flex flex-col justify-between rounded-r-md bg-background">
    <div class="flex flex-col gap-y-1 p-4 h-full">
      <div class="font-medium tracking-tight text-xl">{hotel.name}</div>
      <div class="flex gap-x-1 text-sm items-center text-muted-foreground">
        <div>{Math.round(hotel.reviews.average)}</div>
        <Star class="w-4 h-4 text-black fill-yellow-500" />
        <div>({hotel.reviews.count} reviews)</div>
        <div>|</div>
        <MapPin class="w-4 h-4 text-black fill-red-700" />
        <div>{hotel.location.address}</div>
      </div>
      <p class="text-sm line-clamp-3 mt-4">{hotel.description}</p>
    </div>
    <div class="ml-3">
      <!-- Lower -->
      <HotelCardModal {hotel} {user} />
    </div>
    <div class="border-t p-3 flex justify-end gap-x-3">
      <div class="flex gap-x-1 items-end">
        <div class="text-sm mb-[2px] text-muted-foreground">from</div>
        <div class="font-bold text-xl">
          {hotel.rooms.juniorSuite.price}
        </div>
        <div class="text-sm mb-[2px] text-muted-foreground">USD / Night</div>
      </div>
      <Button on:click={searchRooms(hotel._id, hotel.location.city)}>View Rooms</Button>
    </div>
  </div>
</article>

<!-- <div class="rounded-lg my-3 grid grid-cols-2 bg-background shadow w-full"> -->
<!--Left Container -->
<!-- <div class="flex flex-col gap-y-5 p-5">
    Upper
    <div class="flex gap-x-3">
      <div class="flex flex-col">
        <div class="text-3xl font-bold">
          {hotel.name}
        </div>
        <div class="text-muted-foreground">{hotel.name}</div>
      </div>
      <div class="flex flex-col justify-between w-96">
        <div class="flex items-center h-full">
          <hr class="grow" />
          <Pyramid class="mx-3 h-5 w-5" />
          <hr class="grow" />
        </div>
        <div class="text-muted-foreground self-center">No stops</div>
      </div>
      <div class="flex flex-col">
        <div class="text-3xl font-bold">{hotel.name}</div>
        <div class="text-muted-foreground">{hotel.name}</div>
      </div>
    </div> -->
<!-- Lower -->
<!-- <HotelCardModal {flight} {form} {user}/> -->
<!-- <HotelCardModal {hotel} {user}/>
  </div> -->
<!-- Right Container -->
<!-- <div class="flex p-5 gap-x-5 rounded-r-lg bg-muted">  -->
<!-- {#if Number(flight.touristQuantity) < Number(passengers)} -->
<!-- <div
        class="flex flex-col border rounded-md p-3 w-1/2 gap-y-3 shadow bg-background"
      >
        <div class="text-sm text-muted-foreground">
          <div>Tourist</div>
          <div>{hotel.name} available</div>
        </div>
        <div class="text-3xl font-medium tracking-tighter">
          Not enough tickets available
        </div>
      </div> -->
<!-- {:else} -->
<!-- <a
        href={`/checkout?flightId=${flight.flightId}&passengers=${passengers}&category=economy`}
        class="flex flex-col border rounded-md p-3 w-1/2 gap-y-3 shadow bg-background"
      > -->
<!-- <div class="text-sm text-muted-foreground">
          <div>Tourist</div>
          <div>{hotel.name} available</div>
        </div>
        <div class="text-3xl font-medium tracking-tighter">
          {hotel.name} $
        </div> -->
<!-- </a> -->
<!-- {/if} -->
<!-- {#if Number(flight.businessQuantity) < Number(passengers)} -->
<!-- <div
        class="flex flex-col border rounded-md p-3 w-1/2 gap-y-3 shadow bg-background"
      >
        <div class="text-sm text-muted-foreground">
          <div>Business</div>
          <div>{hotel.name} available</div>
        </div>
        <div class="text-3xl font-medium tracking-tighter">
          Not enough tickets available
        </div>
      </div> -->
<!-- {:else} -->
<!-- <a
        href={`/checkout?flightId=${flight.flightId}&passengers=${passengers}&category=premium`}
        class="flex flex-col border rounded-md p-3 w-1/2 gap-y-3 shadow bg-background"
      > -->
<!-- <div class="text-sm text-muted-foreground">
          <div>Business</div>
          <div>{hotel.name} available</div>
        </div>
        <div class="text-3xl font-medium tracking-tighter">
          {hotel.name} $
        </div> -->
<!-- </a> -->
<!-- {/if} -->
<!-- </div>
</div>  -->
