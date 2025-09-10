<script lang="ts">
  import { UserRoundCog } from "lucide-svelte";
  import { Plane } from "lucide-svelte";
  import { TicketPercent } from "lucide-svelte";
  import { Settings } from "lucide-svelte";
  import Userconfig from "$lib/components/user/user-config/+page.svelte";
  import Flightsinsert from "$lib/components/Flights/+page.svelte";
  import { Button } from "$lib/components/ui/button";
  import {MoveLeft} from 'lucide-svelte';

  let valueselected = "";
</script>

<h1 class="text-xl font-bold mb-1">Administration</h1>
<div>
  <div class="w-full max-w-md mx-auto">
    <div class="px-7 bg-white shadow-lg rounded-2xl mb-5">
      <div class="flex">
        <div class="flex-1 group">
          <a
            href="/user/administration/users"
            class="flex items-end justify-center text-center mx-auto px-4 pt-2 w-full text-gray-400 group-hover:text-indigo-500 border-b-2 border-transparent group-hover:border-indigo-500"
          >
            <span class="block px-1 pt-1 pb-2">
              <i class="far fa-home text-2xl pt-1 mx-4 block"
                ><UserRoundCog /></i
              >
              <span class="block text-xs">Users</span>
            </span>
          </a>
        </div>
        <div class="flex-1 group">
          <a
            on:click={() => (valueselected = "Settings")}
            href="#"
            class="flex items-end justify-center text-center mx-auto px-4 pt-2 w-full text-gray-400 group-hover:text-indigo-500 border-b-2 border-transparent group-hover:border-indigo-500"
          >
            <span class="block px-1 pt-1 pb-2">
              <i class="far fa-cog text-2xl pt-1 mx-4 block"><Settings /></i>
              <span class="block text-xs pb-1">Settings</span>
            </span>
          </a>
        </div>
      </div>
    </div>
  </div>
</div>

{#if valueselected == "Users"}
  <Userconfig />
{:else if valueselected == "Settings"}
<Button href="/user/administration/update-aboutus">About us</Button>
<Button href="/user/administration/proveedores">Providers</Button>
{/if}
