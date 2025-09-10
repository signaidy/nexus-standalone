<script lang="ts">
  import background from "$lib/assets/background.jpg";
</script>

<div class="flex flex-col container p-8 gap-y-5">
  <div class="font-bold text-xl">Featured Flights + Hotels</div>
  <div class="grid grid-cols-3 gap-x-10">
    <img src={background} class="bg-primary h-60 w-[584px] rounded-lg object-cover" alt="Paris Background"/>
    <img src={background} class="bg-primary h-60 w-[584px] rounded-lg object-cover" alt="Paris Background"/>
    <img src={background} class="bg-primary h-60 w-[584px] rounded-lg object-cover" alt="Paris Background"/>
  </div>
</div>
