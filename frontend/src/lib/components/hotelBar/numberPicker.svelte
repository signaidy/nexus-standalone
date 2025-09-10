<script lang="ts">
  import { Button } from "$lib/components/ui/button";
  import { Minus, Plus } from "lucide-svelte";

  export let guests = 1;

  function handleClick(adjustment: number) {
    guests = guests + adjustment;
  }
</script>

<div class="flex items-center justify-center gap-x-2">
  <Button
    variant="outline"
    size="icon"
    class="h-8 w-8 shrink-0 rounded-full"
    on:click={() => handleClick(-1)}
    disabled={guests <= 1}
  >
    <Minus class="h-4 w-4" />
    <span class="sr-only">Decrease</span>
  </Button>
  <div class="flex flex-col text-center">
    <div class="font-bold tracking-tighter">
      {guests}
    </div>
    <div class="text-[0.70rem] uppercase text-muted-foreground">Guests</div>
  </div>
  <Button
    variant="outline"
    size="icon"
    class="h-8 w-8 shrink-0 rounded-full"
    on:click={() => handleClick(1)}
  >
    <Plus class="h-4 w-4" />
    <span class="sr-only">Increase</span>
  </Button>
</div>
