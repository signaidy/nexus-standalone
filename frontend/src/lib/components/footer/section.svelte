<script lang="ts">
  export let title: string;
</script>

<div class="flex flex-col">
  <div class="font-bold mb-3">{title}</div>
  <slot />
</div>
