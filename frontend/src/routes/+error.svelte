<script>
  import RoborError from "../lib/assets/roboterror.gif";
  export let status;
</script>

<div class="text-center grid place-items-center space">
  <img
    class="imgm"
    src={RoborError}
    alt="Computer man"
    style="width:220px;height:220px;"
  />
  <p class="mb-4 text-lg text-red-500">PAGE NOT FOUND!</p>
  <p class="mb-4 text-lg text-gray-600">Ops! Looks like you're lost.</p>
  <div class="animate-bounce">
    <svg
      class="mx-auto h-16 w-16 text-red-500"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        stroke-width="2"
        d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
      ></path>
    </svg>
  </div>
  <p class="mt-4 text-gray-600">
    Let's get you back <a href="/" class="text-blue-500">home</a>.
  </p>
</div>

<style>
  .imgm {
    margin: 10px;
  }

  .space {
    padding-top: 200px;
  }
</style>
