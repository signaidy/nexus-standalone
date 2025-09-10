<script lang="ts">
  import LocationPicker from "$lib/components/hotelBar/locationPicker.svelte";
  import LocationPickerSkeleton from "$lib/components/hotelBar/locationPicker.svelte";
  import DateRangePicker from "$lib/components/hotelBar/dateRangePicker.svelte";
  import NumberPicker from "$lib/components/hotelBar/numberPicker.svelte";
  import { Button } from "$lib/components/ui/button";
  // Icons
  import { LandPlot, PlaneLanding, UsersRound } from "lucide-svelte";
  // Utilities
  import type { DateRange } from "bits-ui";
  import { goto } from "$app/navigation";

  export let cities: Promise<any> = [];
  let isValid = true;

  let city: string;
  let days: DateRange | undefined;
  let guests: number;

  function searchHotels() {
    if (city && days && days.start && days.end && guests) {
      const searchParams = new URLSearchParams();
      searchParams.append("city", city);
      searchParams.append("check-in", days.start.toString());
      searchParams.append("check-out", days.end.toString());
      searchParams.append("guests", guests.toString());
      goto(`/hotelsearch?${searchParams.toString()}`);
    } else {
      isValid = false;
    }
  }
</script>

{#if !isValid}
  <div class="ml-3 my-px text-red-500 font-medium">Please fill in all the fields</div>
{/if}
<div class="bg-background rounded-lg p-3 shadow flex gap-x-3 flex-wrap gap-y-3">
  <div class="flex flex-col gap-y-2 w-fit">
    <div class="font-bold flex gap-x-2 items-center">
      Cities<LandPlot class="shrink-0 w-5 h-5" />
    </div>
    {#await cities}
      <LocationPickerSkeleton />
    {:then cities}
      <LocationPicker {cities} bind:value={city} />
    {:catch error}
      <p>{error.message}</p>
    {/await}
  </div>
  <div class="flex flex-col gap-y-2 w-fit">
    <div class="font-bold">Check In - Check out</div>
    <DateRangePicker bind:value={days} />
  </div>
  <div class="flex flex-col gap-y-2 w-fit">
    <div class="font-bold flex gap-x-2 items-center ml-7">
      Guests<UsersRound class="shrink-0 w-5 h-5" />
    </div>
    <NumberPicker bind:guests />
  </div>
  <div class="flex flex-col gap-y-2 w-fit">
    <div class="text-background">Find</div>
    <Button on:click={searchHotels}>Find Hotels</Button>
  </div>
</div>
