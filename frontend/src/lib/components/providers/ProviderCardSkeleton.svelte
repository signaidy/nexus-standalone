<script lang="ts">
    import { Button } from "$lib/components/ui/button";
  </script>
  
  <div class="flex flex-col gap-y-2">
    <div class="flex text-sm font-bold items-center gap-x-3">
      Provider #
      <div class="bg-muted-foreground w-28 h-2 animate-pulse rounded-lg" />
    </div>
    <div class="flex flex-wrap border rounded-lg gap-x-3">
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Name</div>
        <div class="text-muted-foreground">
          <div class="bg-muted-foreground h-2 rounded-lg w-28 animate-pulse" />
        </div>
        <div class="font-medium">URL</div>
        <div class="text-muted-foreground">
          <div class="bg-muted-foreground h-2 rounded-lg w-28 animate-pulse" />
        </div>
      </div>
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Type</div>
        <div class="text-muted-foreground">
          <div class="bg-muted-foreground h-2 rounded-lg w-28 animate-pulse" />
        </div>
        <div class="font-medium">Gains</div>
        <div class="text-muted-foreground">
          <div class="bg-muted-foreground h-2 rounded-lg w-28 animate-pulse" />
        </div>
      </div>
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Discount</div>
        <div class="text-muted-foreground">
          <div class="bg-muted-foreground h-2 rounded-lg w-28 animate-pulse" />
        </div>
      </div>     
      <div class="pr-3 py-3">
        <Button disabled>Edit</Button>
      </div>
    </div>
  </div>
  