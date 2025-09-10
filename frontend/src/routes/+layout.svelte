<script lang="ts">
  import Header from "$lib/components/header/header.svelte";
  import "../app.css";

  export let data;
</script>

<Header {data} />
<slot />
