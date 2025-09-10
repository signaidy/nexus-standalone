<script lang="ts">
  import UserCard from "$lib/components/user/users/UserCard.svelte";
  import UserCardSkeleton from "$lib/components/user/users/UserCardSkeleton.svelte";
  import { MoveLeft } from "lucide-svelte";

  export let data;
  export let form;
</script>

<div class="flex items-center mt-4 mb-8 gap-x-3">
  <a href="/user/administration"><MoveLeft /></a>
  <h1 class="text-xl font-bold">Users Administration</h1>
</div>

<div class="flex flex-col gap-y-3">
  {#await data.users}
    {#each Array(3).fill(0) as _}
      <UserCardSkeleton />
    {/each}
  {:then users}
    {#each users as user}
      <UserCard {form} {user} />
    {/each}
  {:catch error}
    <p>{error.message}</p>
  {/await}
</div>
