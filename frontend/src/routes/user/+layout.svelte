<script lang="ts">
  import NavBar from "$lib/components/user/navBar.svelte";

  export let data;
</script>

<div class="flex container h-[calc(100vh-5rem)] overflow-y-auto">
  <NavBar user={data.user}/>
  <div class="py-8 pl-8 grow">
    <slot />
  </div>
</div>
