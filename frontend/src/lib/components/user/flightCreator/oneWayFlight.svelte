<script lang="ts">
  import { enhance } from "$app/forms";
  // Components
  import DatePicker from "$lib/components/user/administration/flightCreator/datePicker.svelte";
  import LocationPicker from "$lib/components/user/administration/flightCreator/locationPicker.svelte";
  // UI Components
  import { Button } from "$lib/components/ui/button";
  import { Input } from "$lib/components/ui/input";
  import { Label } from "$lib/components/ui/label";
  // Icons
  import { PlaneTakeoff } from "lucide-svelte";
  // Date Utilities
  import { type DateValue } from "@internationalized/date";

  let loading = false;

  let departureDay: DateValue | undefined;
  let arrivalDay: DateValue | undefined;
  let originCity: string;
  let destinationCity: string;
</script>

<form
  method="POST"
  use:enhance={() => {
    loading = true;

    return async ({ update }) => {
      await update();
      loading = false;
    };
  }}
  class="rounded-lg border-t flex bg-background w-full"
>
  <!-- Hidden Inputs to transfer as Form Data -->
  <input type="hidden" name="departureDay" value={departureDay} />
  <input type="hidden" name="arrivalDay" value={arrivalDay} />
  <input type="hidden" name="originCity" value={originCity} />
  <input type="hidden" name="destinationCity" value={destinationCity} />
  <input type="hidden" name="type" value="one-way" />
  <!-- Left Container -->
  <div class="flex flex-col gap-y-5 p-5 grow">
    <!-- Upper -->
    <div class="flex justify-between">
      <div class="flex flex-col gap-y-2">
        <div class="font-bold text-sm">Departure Day</div>
        <DatePicker bind:value={departureDay} />
      </div>
      <div class="flex flex-col gap-y-2">
        <div class="font-bold text-sm">Arrival Day</div>
        <DatePicker bind:value={arrivalDay} />
      </div>
    </div>
    <!-- Lower -->
    <div class="flex gap-x-3">
      <div class="flex flex-col gap-y-3">
        <div class="flex flex-col gap-y-2">
          <Label for="departureTime" class="font-bold">Departure Time</Label>
          <Input type="time" id="departureTime" name="departureTime" />
        </div>
        <div class="flex flex-col gap-y-2">
          <div class="font-bold text-sm">Origin City</div>
          <LocationPicker bind:value={originCity} />
        </div>
      </div>
      <div class="flex flex-col justify-between grow">
        <div class="flex items-center h-full">
          <hr class="grow" />
          <PlaneTakeoff class="mx-3 h-5 w-5" />
          <hr class="grow" />
        </div>
        <div class="text-muted-foreground self-center">1 stop, 15h</div>
      </div>
      <div class="flex flex-col gap-y-3">
        <div class="flex flex-col gap-y-2">
          <Label for="arrivalTime" class="font-bold">Arrival Time</Label>
          <Input type="time" id="arrivalTime" name="arrivalTime" />
        </div>
        <div class="flex flex-col gap-y-2">
          <div class="font-bold text-sm">Destination City</div>
          <LocationPicker bind:value={destinationCity} />
        </div>
      </div>
    </div>
    <!-- Submit -->
    <Button type="submit" disabled={loading}>Create Flight</Button>
  </div>
  <!-- Right Container -->
  <div class="flex p-5 gap-x-5 rounded-r-lg bg-muted">
    <div
      class="flex flex-col border rounded-md p-3 w-fit gap-y-5 shadow bg-background"
    >
      <div class="font-bold">Tourist Tickets</div>
      <div class="flex flex-col gap-y-2">
        <Label for="touristCapacity">Capacity</Label>
        <Input
          id="touristCapacity"
          name="touristCapacity"
          class="w-28"
          type="number"
        />
      </div>
      <div class="flex flex-col gap-y-2">
        <Label for="touristPrice">Price</Label>
        <Input
          id="touristPrice"
          name="touristPrice"
          class="w-28"
          type="number"
        />
      </div>
      <div class="flex flex-col gap-y-2">
        <Label for="touristQuantity">Quantity</Label>
        <Input
          id="touristQuantity"
          name="touristQuantity"
          class="w-28"
          type="number"
        />
      </div>
    </div>
    <div
      class="flex flex-col border rounded-md p-3 w-fit gap-y-5 shadow bg-background"
    >
      <div class="font-bold">Business Tickets</div>
      <div class="flex flex-col gap-y-2">
        <Label for="businessCapacity">Capacity</Label>
        <Input
          id="businessCapacity"
          name="businessCapacity"
          class="w-28"
          type="number"
        />
      </div>
      <div class="flex flex-col gap-y-2">
        <Label for="businessPrice">Price</Label>
        <Input
          id="businessPrice"
          name="businessPrice"
          class="w-28"
          type="number"
        />
      </div>
      <div class="flex flex-col gap-y-2">
        <Label for="businessQuantity">Quantity</Label>
        <Input
          id="businessQuantity"
          name="businessQuantity"
          class="w-28"
          type="number"
        />
      </div>
    </div>
  </div>
</form>
