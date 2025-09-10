<script lang="ts">
  import { Star, MapPin } from "lucide-svelte";
  import { Button } from "$lib/components/ui/button";
  import HotelCardModal from "$lib/components/hotelseach/hotelCardModal.svelte";
  import { goto } from "$app/navigation";
  import type { DateRange } from "bits-ui";
  import { page } from "$app/stores";
  import { BedDouble, UserRound } from "lucide-svelte";

  export let room;
  export let hotel;
  export let hotelname; 
  export let city; 
  export let guests: string | null;
  export let user: User;
  // export let form;
  let checkin = $page.url.searchParams.get("check-in");
  let checkout = $page.url.searchParams.get("check-out");
  let providerId = $page.url.searchParams.get("providerId");

  const handleCheckout = (hotel, guests, roomType, price, hotelname, beds, city, bedAmount, providerId) => {
    goto(
      `/hotelcheckout?hotelid=${hotel}&checkin=${checkin}&checkout=${checkout}&roomtype=${roomType}&price=${price}&guests=${guests}&hotelname=${hotelname}&beds=${beds}&city=${city}&bedAmount=${bedAmount}&providerId=${providerId}`
    );
  };
</script>

{#each Object.keys(room) as roomType}
  {#if roomType == "juniorSuite" || roomType == "standardSuite" || roomType == "bigSuite" || roomType == "doubleSuite"}
    <article
      class="border rounded-md grid-cols-[0.75fr_1fr] grid h-72 mt-10 mb-20"
    >
      <div class="relative">
        <img
          src={room[roomType].picture}
          alt={`${room[roomType].picture} Hotel Image`}
          class="object-cover rounded-l-md"
        />
      </div>
      <div class="flex flex-col justify-between rounded-r-md bg-background">
        <div class="flex flex-col gap-y-1 p-4 h-full">
          <div class="font-medium tracking-tight text-xl">
            {#if roomType == "juniorSuite"}
              Juior Suite
            {:else if roomType == "standardSuite"}
              Standard Suite
            {:else if roomType == "doubleSuite"}
              Double Suite
            {:else}
              Big Suite
            {/if}
          </div>
          <div class="flex gap-x-1 text-sm items-center text-muted-foreground">
            <BedDouble class="w-4 h-4 text-black fill-yellow-500" />
            <div>{Math.round(room[roomType].beds.amount)}</div>
            <div>{room[roomType].beds.size} Size Beds</div>
            <div>|</div>
            <UserRound class="w-4 h-4 text-black fill-red-700" />
            <div>{room[roomType].maxOccupancy} maximum occupancy</div>
          </div>
          <p class="text-sm line-clamp-3 mt-4">{room[roomType].description}</p>
        </div>
        <div class="ml-3">
          <!-- Lower  -->
          <!-- <HotelCardModal {hotel} {user} /> -->
        </div>
        <div class="border-t p-3 flex justify-end gap-x-3">
          <div class="flex gap-x-1 items-end">
            <div class="font-bold text-xl">
              {room[roomType].price}
            </div>
            <div class="text-sm mb-[2px] text-muted-foreground">
              USD / Night
            </div>
          </div>
          <Button
            on:click={() =>
              handleCheckout(
                hotel,
                guests,
                roomType,
                room[roomType].price, 
                hotelname, 
                room[roomType].beds.size, 
                city,
                room[roomType].beds.amount,
                providerId
              )}>Continue</Button
          >
        </div>
      </div>
    </article>
  {/if}
{/each}
