<script lang="ts">
    export let key: string;
    export let value: string;
</script>
<div class="flex flex-col gap-y-1 text-sm border-b pb-2 last:border-b-0 last:pb-0">
  <div class="font-medium">{key}</div>
  <div class="text-muted-foreground">{value}</div>
</div>
