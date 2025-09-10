<script lang="ts">
  import { Button } from "$lib/components/ui/button";
  import { ArrowRight, Pyramid } from "lucide-svelte";
</script>

<div
  class="rounded-lg my-5 grid grid-cols-2 bg-background shadow w-full animate-pulse"
>
  <!-- Left Container -->
  <div class="flex flex-col gap-y-10 p-5">
    <!-- Upper -->
    <div class="flex gap-x-3">
      <div class="flex flex-col gap-y-5">
        <div class="text-3xl font-bold w-32 h-2 bg-muted-foreground rounded-lg" />
        <div class="text-muted-foreground w-20 h-2 bg-muted-foreground rounded-lg" />
      </div>
      <div class="flex flex-col justify-between w-96">
        <div class="flex items-center h-full">
          <hr class="grow" />
          <Pyramid class="mx-3 h-5 w-5" />
          <hr class="grow" />
        </div>
        <div class="text-muted-foreground bg-muted-foreground rounded-lg self-center h-2 w-16" />
      </div>
      <div class="flex flex-col gap-y-5">
        <div class="text-3xl font-bold w-32 h-2 bg-muted-foreground rounded-lg" />
        <div class="text-muted-foreground w-20 h-2 bg-muted-foreground rounded-lg" />
      </div>
    </div>
    <!-- Lower -->
    <Button variant="link" disabled class="group p-0 self-baseline">
      Hotel Details
      <ArrowRight
        class="shrink-0 ml-2 h-4 w-4 transition-transform group-hover:-rotate-45"
      />
    </Button>
  </div>
  <!-- Right Container -->
  <div class="flex p-5 gap-x-5 rounded-r-lg bg-muted">
    <div
      class="flex flex-col border rounded-md p-3 w-1/2 gap-y-3 shadow bg-background"
    >
      <div class="text-sm text-muted-foreground">Tourist</div>
      <div class="text-3xl font-medium tracking-tighter bg-muted-foreground rounded-lg w-12 h-8" />
    </div>
    <div
      class="flex flex-col border rounded-md p-3 w-1/2 gap-y-3 shadow bg-background"
    >
      <div class="text-sm text-muted-foreground">Business</div>
      <div class="text-3xl font-medium tracking-tighter bg-muted-foreground rounded-lg w-12 h-8" />
    </div>
  </div>
</div>
