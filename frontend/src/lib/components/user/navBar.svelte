<script lang="ts">
  import { enhance } from "$app/forms";
  import { Button } from "$lib/components/ui/button";
  import {
    LayoutDashboard,
    BedDouble,
    Table2,
    Bolt,
    WalletCards,
    AreaChart,
    LogOut,
    Plane
  } from "lucide-svelte";

  export let user: User;

  const routes = [
    {
      value: "dashboard",
      label: "Dashboard",
      href: "/user/dashboard",
      icon: LayoutDashboard,
    },
    {
      value: "flights",
      label: "Flights",
      href: "/user/flights",
      icon: Plane,
    },
    {
      value: "hotels",
      label: "Hotels",
      href: "/user/hotels",
      icon: BedDouble,
    },
  ];

  const adminRoutes = [
    {
      value: "administration",
      label: "Administration",
      href: "/user/administration",
      icon: Bolt,
    },
    {
      value: "flightAdmin",
      label: "Administrate Flights",
      href: "/user/flightAdmin",
      icon: Plane,
    },
    {
      value: "hotelAdmin",
      label: "Administrate Reservations",
      href: "/user/hotelAdmin",
      icon: BedDouble,
    },
  ];
</script>

<nav
  class="flex flex-col gap-y-3 py-8 pr-8 border-r sticky top-0 overflow-y-auto"
>
  {#each routes as route}
    <Button href={route.href} class="justify-normal">
      <svelte:component this={route.icon} class="shrink-0 mr-2 h-4 w-4" />
      {route.label}
    </Button>
  {/each}
  {#if user.role === "ADMIN" || user.role === "EMPLOYEE"}
    <div class="text-sm border-b-primary border-b font-medium pb-1">
      Administrator
    </div>
    {#each adminRoutes as adminRoute}
      <Button href={adminRoute.href} class="justify-normal">
        <svelte:component
          this={adminRoute.icon}
          class="shrink-0 mr-2 h-4 w-4"
        />
        {adminRoute.label}
      </Button>
    {/each}
  {/if}
  <form
    method="POST"
    action="administration?/logOut"
    class="flex flex-col"
    use:enhance
  >
    <Button class="justify-normal" type="submit">
      <LogOut class="shrink-0 mr-2 h-4 w-4" />Log Out
    </Button>
  </form>
</nav>
