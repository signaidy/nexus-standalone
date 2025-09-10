<script>
  import { onMount } from "svelte";
  import { Banknote, GitMerge } from 'lucide-svelte';
  import zenlogo from "$lib/assets/zen-logo.png"; 

  export let data;
  let userid = data.user.userId
  let flights = data.reservations;
</script>

<h1 class="text-xl font-bold mb-8">Booked Reservations</h1>
<div class="max-w-screen-lg mx-auto">
  {#await data.reservations}
    
  {:then flights} 
    {#if flights.length>0}
      {#each flights as {reservationNumber, roomType, state, hotelId, location, bedSize, dateStart, dateEnd, hotel, bundle }}
        <div class="p-10">
          <div
            class="max-w-full bg-white flex flex-col rounded overflow-hidden shadow-lg"
          >
            <div class="flex flex-row items-baseline flex-nowrap bg-gray-100 p-2">
              <h1 class="ml-1 uppercase font-bold text-black-500">Hotel</h1>
              <p class="ml-1 font-normal text-gray-500">{hotel}</p>
              <h1 class="ml-1 uppercase font-bold text-black-500">Ticket</h1>
              <p class="ml-1 font-normal text-gray-500">{reservationNumber}</p>
              {#if state == "active"}
              <h1 class="text-green-700 ml-auto">Active</h1> 
              {:else if  state == "cancelled"}
              <p class="text-red-700 ml-auto">Canceled</p>
              {:else}
              <p class="text-red-700 ml-auto">a chikita</p>
              {/if} 
            </div>
            <div class="mt-2 flex justify-start bg-white p-2">
              <div
                class="flex mx-2 ml-6 h8 px-2 flex-row items-baseline rounded-full bg-gray-100 p-1"
              >
                <div class="reduct"><Banknote/></div>
                <p class="font-normal text-sm ml-1 text-gray-500">{roomType}</p>
              </div>
              {#if bundle != null}
                  <div
                    class="flex mx-2 ml-6 h8 px-2 flex-row items-baseline rounded-full bg-gray-100 p-1"
                  >
                    <div class="reduct"><GitMerge /></div>
                    <p class="font-normal text-sm ml-1 text-gray-500">Bundle</p>
                  </div>
                {/if}
            </div>
            <div class="mt-2 flex sm:flex-row mx-6 sm:justify-between flex-wrap">
              <div class="flex flex-row place-items-center p-2">
                <img
                  alt={hotel}
                  class="w-20 h-10"
                  src={zenlogo}
                  style="opacity: 1; transform-origin: 0% 50% 0px; transform: none;"
                />
                <div class="flex flex-col ml-2 mx-2">
                  <!-- <p class="text-s text-black-500 font-bold">Avianca</p> -->
                  <p class="text-xs text-gray-500">Reservation Start Date</p><p>{dateStart}</p>
                  <p class="text-xs text-gray-500">Reservation End Date</p><p>{dateEnd}</p>
                </div>
              </div>
  
              <div class="flex flex-col p-2">
                <p class="text-black-500">
                  <span class="font-bold">Location:</span>
                </p>
                <p class="text-gray-500">{location}</p>
              </div>
              <div class="flex flex-col flex-wrap p-2">
                <p class="text-black-500">
                  <span class="font-bold">Room Size:</span>
                </p>
                <p class="text-gray-500">{bedSize}</p>
              </div>
  
            </div>
                      
          </div>
        </div>
      {/each}
      {:else}
      <div>
        <p class="text-gray-500">No Hotel reservations booked yet</p>
      </div>
    {/if}
  {/await}
</div>


<style>
  .reduct{
    height: 17px;
    width: 25px;
    color: grey;
  }

  .statef {
    margin-left: 920px;
  }

</style>