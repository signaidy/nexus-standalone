<script lang="ts">
  import ProviderCard from "$lib/components/providers/ProviderCard.svelte";
  import ProviderCardSkeleton from "$lib/components/providers/ProviderCardSkeleton.svelte";
  import { MoveLeft } from "lucide-svelte";
  import { enhance } from "$app/forms";
    import Button from "$lib/components/ui/button/button.svelte";

  export let data;
  export let form;
  let showModal = false;
    let providerName = "";
    let providerUrl = "";
    let type = "";
    let gainsHotel = 0;
    let gainsFlight = 0;
    let percentageDiscount = 0;
</script>

<div class="flex items-center mt-4 mb-8 gap-x-3">
  <a href="/user/administration"><MoveLeft /></a>
  <h1 class="text-xl font-bold">Providers Administration</h1>
</div>

<div class="flex flex-col gap-y-3">
  <button class="bg-black hover:bg-blue-700 text-white font-bold py-2 px-4 rounded" on:click={() => showModal = true}>
    Add New Provider
  </button>
  {#await data.providers}
    {#each Array(3).fill(0) as _}
      <ProviderCardSkeleton />
    {/each}
  {:then providers}
    {#each providers as provider}
      <ProviderCard {form} {provider} />
    {/each}
  {:catch error}
    <p>{error.message}</p>
  {/await}
  <!-- Modal -->
  {#if showModal}
  <div class="fixed z-10 inset-0 overflow-y-auto">    
    <form action="?/createProvider" method="POST">
    {#if form?.error}
        <div class="text-sm text-red-500 font-medium">
        Error: {form.error}
        </div>
    {/if}
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 transition-opacity" aria-hidden="true">
          <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
        <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div class="sm:flex sm:items-start">
              <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                <h3 class="text-lg leading-6 font-medium text-gray-900">Add New Provider</h3>
                <div class="mt-2">
                    <div class="mb-4">
                      <label for="providerName" class="block text-gray-700 text-sm font-bold mb-2">Provider Name:</label>
                      <input type="text" id="providerName" name="providerName" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" bind:value={providerName}>
                    </div>
                    <div class="mb-4">
                      <label for="providerUrl" class="block text-gray-700 text-sm font-bold mb-2">Provider URL:</label>
                      <input type="text" id="providerUrl" name="providerUrl" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" bind:value={providerUrl}>
                    </div>
                    <div class="mb-4">
                      <label for="type" class="block text-gray-700 text-sm font-bold mb-2">Type:</label>
                      <select id="type" name="type" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" bind:value={type}>
                        <option value="">Select Type</option>
                        <option value="HOTEL">Hotel</option>
                        <option value="AEROLINEA">Aerolinea</option>
                      </select>
                    </div>
                    {#if type === 'HOTEL'}
                    <div class="mb-4">
                      <label for="gainsHotel" class="block text-gray-700 text-sm font-bold mb-2">Gains (Hotel):</label>
                      <input type="number" id="gainsHotel" name="gainsHotel" step="0.01" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" bind:value={gainsHotel}>
                    </div>
                    {/if}
                    {#if type === 'AEROLINEA'}
                    <div class="mb-4">
                      <label for="gainsFlight" class="block text-gray-700 text-sm font-bold mb-2">Gains (Flight):</label>
                      <input type="number" id="gainsFlight" name="gainsFlight" step="0.01" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" bind:value={gainsFlight}>
                    </div>
                    {/if}
                    <div class="mb-4">
                      <label for="percentageDiscount" class="block text-gray-700 text-sm font-bold mb-2">Percentage Discount:</label>
                      <input type="number" id="percentageDiscount" name="percentageDiscount" step="0.01" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" bind:value={percentageDiscount}>
                    </div>
                </div>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <Button type="submit" class="w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-black hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm">
              Save
            </Button>
            <Button type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm" on:click={() => showModal = false}>
              Cancel
            </Button>
          </div>
        </div>
      </div>
    </form>
  </div>
  {/if}
</div>

