<script>
      import { User } from "lucide-svelte";
</script>

<div class="w-full max-w-md mx-5 bg-white shadow-md rounded-md px-6 py-4 my-6">
    <div class="sm:flex sm:justify-between">
      <div class="flex items-center">
        <div class="h-12 w-12 rounded-full">
          <User />
        </div>
        <div class="ml-2">
          <h3 class="text-lg text-gray-800 font-medium">Username</h3>
          <span class="text-gray-600">Email</span>
        </div>
      </div>
      <div class="mt-2 sm:mt-0">
        <button
          class="flex items-center text-white bg-yellow-600 rounded px-2 py-1 focus:outline-none focus:shadow-outline"
        >
          <span class="ml-1 text-sm">Enterprise</span>
        </button>
      </div>
    </div>
    <div class="flex justify-between items-center mt-4">
      <div>
        <h4 class="text-gray-600 text-sm">Passport number</h4>
        <span class="mt-2 text-xl font-medium text-gray-800">----------</span>
      </div>
      <div>
        <h4 class="text-gray-600 text-sm">Origin country</h4>
        <span class="mt-2 text-xl font-medium text-gray-800">---------------</span
        >
      </div>
      <div>
        <h4 class="text-gray-600 text-sm">Age</h4>
        <span class="mt-2 text-xl font-medium text-gray-800">-------</span>
      </div>
    </div>
    <div class="mt-3">
      <button
        class="flex items-center text-white bg-black rounded px-2 py-1 focus:outline-none focus:shadow-outline"
      >
        <span class="ml-1 text-sm">Edit</span>
      </button>
    </div>
  </div>