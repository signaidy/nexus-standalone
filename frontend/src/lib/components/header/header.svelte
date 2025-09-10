<script lang="ts">
  import { Button } from "$lib/components/ui/button";
  import { CircleUserRound } from "lucide-svelte";
  import logo from "$lib/assets/logo.svg";
  import imageTab from "$lib/assets/Image-Tab.png";

  export let data: { user: User | null };
</script>

<link class="tabimage" rel="icon" type="image/png" href={imageTab}/>
<header class="h-20 border-b">
  <div class="flex h-full container justify-between items-center">
    <a href="/">
      <img src={logo} alt="Avianca Logo" class="w-[81px] h-[60px]" />
    </a>
    <nav class="flex gap-x-3 text-sm font-bold">
      <Button href="/aboutus" variant="ghost" class="font-bold">About Us</Button>
    </nav>
    {#if data.user}
      <Button href="/user/dashboard" variant="ghost" class="font-bold">
        <CircleUserRound class="mr-2" />{data.user.firstName +
          " " +
          data.user.lastName}
      </Button>
    {:else}
      <Button href="/signin">
        Join | Sign In <CircleUserRound class="ml-2" />
      </Button>
    {/if}
  </div>
</header>
