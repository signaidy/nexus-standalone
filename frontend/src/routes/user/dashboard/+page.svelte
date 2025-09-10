<script lang="ts">
  import UserInfo from "$lib/components/user/dashboard/userInfo.svelte";
  import messi from "$lib/assets/messi.jpg";

  export let data;

  const userInfo = [
    { key: "Full Name", value: data.user.firstName + " " + data.user.lastName },
    { key: "Email", value: data.user.email },
    { key: "Password", value: "*********" },
    { key: "Age", value: data.user.age },
    { key: "Country of Origin", value: data.user.originCountry },
    { key: "Passport Number", value: data.user.passportNumber },
  ];
</script>

<h1 class="text-xl font-bold mb-8">Dashboard</h1>
<div class="grid grid-cols-2 gap-x-8">
  <div class="flex flex-col gap-y-3">
    <div class="font-bold">Profile</div>
    <div class="flex flex-col gap-y-2 border rounded-lg p-3">
      <img
        src={messi}
        alt="User Profile"
        class="rounded-full w-[4.5rem] h-[4.5rem] self-center object-cover animate-spin"
      />
      {#each userInfo as user}
        <UserInfo key={user.key} value={user.value} />
      {/each}
    </div>
  </div>
  <div class="flex flex-col gap-y-3">
    <div class="font-bold">Flights + Hotels Overview</div>
    <div class="flex flex-col gap-y-2 border rounded-lg p-3">
      <div class="text-sm font-medium">No flights booked</div>
    </div>
  </div>
</div>
