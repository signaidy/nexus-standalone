<script>
  import { PUBLIC_BACKEND_URL } from '$env/static/public';

  let informationabout = getAboutInformation();

  async function getAboutInformation() {
    const response = await fetch(`${PUBLIC_BACKEND_URL}/aboutus/1`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
    console.log(response)
    const aboutdata = await response.json();
    console.log(aboutdata)
    return aboutdata;
  }
</script>

{#await informationabout}
  <div class="flex items-center justify-center w-full h-full mt-10">
    <div
      class="flex justify-center items-center space-x-1 text-xl text-gray-700"
    >
      <svg
        fill="none"
        class="w-8 h-8 animate-spin"
        viewBox="0 0 32 32"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          clip-rule="evenodd"
          d="M15.165 8.53a.5.5 0 01-.404.58A7 7 0 1023 16a.5.5 0 011 0 8 8 0 11-9.416-7.874.5.5 0 01.58.404z"
          fill="currentColor"
          fill-rule="evenodd"
        />
      </svg>
      <div>Loading ...</div>
    </div>
  </div>
{:then data}
  <div class="py-3" style="background-color:#dc143c">
    <div class="container m-auto px-6 mt-5 mb-5 text-gray-600 md:px-12 xl:px-6">
      <div
        class="space-y-6 md:space-y-0 md:flex md:gap-6 lg:items-center lg:gap-12"
      >
        <div class="md:5/12 lg:w-5/12">
          <img
            src={data.gif}
            alt="About us image"
            loading="lazy"
            width="500"
            height="500"
          />
        </div>
        <div class="md:7/12 lg:w-6/12">
          <div class="container">
            <div class="row">
              <div class="col-md-12 text-center">
                <i class="animate-charcter">{data.slogan}</i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div
    class="2xl:container 2xl:mx-auto lg:py-1 lg:px-20 md:py-12 md:px-6 py-9 px-4"
  >
    <div
      class="lg:mt-16 sm:mt-12 mt-16 flex lg:flex-row justify-between flex-col lg:gap-8 gap-12"
    >
      <div class="w-full xl:w-5/12 lg:w-6/12">
        <h2
          class="font-bold lg:text-4xl text-3xl lg:leading-9 leading-7 text-gray-800 dark:text-white"
        >
          Our Story
        </h2>
        <p
          class="font-normal text-base leading-6 text-justify text-gray-600 dark:text-white mt-4"
        >
          Taking flight over a century ago in 1919 as SCADTA, Avianca has woven
          its wings into the fabric of Colombian, and eventually Latin American,
          aviation. Originally ferrying mail across the Andes, it transformed
          into Colombia's flag carrier, aptly named "Avianca" - "belonging to
          the skies." Through turbulent times, including nationalization and
          privatization, it persevered, becoming a Star Alliance member and
          expanding its reach across continents. Today, Avianca stands as a
          symbol of Colombian resilience and innovation, connecting people and
          cultures with its trademark yellow wings, carrying a rich legacy into
          the future of travel.
        </p>
        <p
          class="font-normal text-base leading-6 text-justify text-gray-600 dark:text-white mt-6"
        >
          Avianca carries not just passengers, but a rich legacy of innovation
          and perseverance. As it navigates the ever-changing skies, one thing
          remains constant: Avianca's unwavering commitment to uniting people
          and shaping the future of travel, one soaring adventure at a time.
        </p>
      </div>

      <div class="lg:flex items-center w-full lg:w-1/2">
        <iframe width="532" height="300" class="spaceyt" src={data.yt}>
        </iframe>
      </div>
    </div>

    <section class="container mx-auto px- py-2 lg:py-7 mt-10">
      <div class="mt-10 grid grid-cols-1 gap-10 lg:grid-cols-3">
        {#if data.cardsAmount >= 1 }
        <div
          class="relative flex flex-col bg-clip-border rounded-xl bg-transparent text-gray-700 shadow-md relative grid min-h-[30rem] items-end overflow-hidden rounded-xl"
        >
          <img
            src={data.imgOne}
            alt="bg"
            class="absolute inset-0 h-full w-full object-cover object-center"
          />
          <div class="absolute inset-0 bg-black/70"></div>
          <div class="p-20 relative flex flex-col m">
            <h4
              class="block antialiased tracking-normal font-sans text-2xl font-semibold leading-snug text-center text-white"
            >
              {data.titleOne}
            </h4>
            <p
              class="block antialiased font-sans text-base font-light leading-relaxed text-center text-white my-2 font-normal"
            >
              {data.textOne}
            </p>
          </div>
        </div>
        {/if}
        {#if data.cardsAmount >= 2}
        <div
          class="relative flex flex-col bg-clip-border rounded-xl bg-transparent text-gray-700 shadow-md relative grid min-h-[30rem] items-end overflow-hidden rounded-xl"
        >
          <img
            src={data.imgTwo}
            alt="bg"
            class="absolute inset-0 h-full w-full object-cover object-center"
          />
          <div class="absolute inset-0 bg-black/70"></div>
          <div class="p-20 relative flex flex-col o">
            <h4
              class="block antialiased tracking-normal font-sans text-2xl font-semibold leading-snug text-center text-white"
            >
              {data.titleTwo}
            </h4>
            <p
              class="block antialiased font-sans text-base font-light leading-relaxed text-center text-white my-2 font-normal"
            >
              {data.textTwo}
            </p>
          </div>
        </div>
        {/if}
        {#if data.cardsAmount >= 3}
        <div
          class="relative flex flex-col bg-clip-border rounded-xl bg-transparent text-gray-700 shadow-md relative grid min-h-[30rem] items-end overflow-hidden rounded-xl"
        >
          <img
            src={data.imgThree}
            alt="bg"
            class="absolute inset-0 h-full w-full object-cover object-center rotationimg"
          />
          <div class="absolute inset-0 bg-black/70"></div>
          <div class="p-20 relative flex flex-col b">
            <h4
              class="block antialiased tracking-normal font-sans text-2xl font-semibold leading-snug text-center text-white"
            >
              {data.titleThree}
            </h4>
            <p
              class="block antialiased font-sans text-base font-light leading-relaxed text-center text-white my-2 font-normal"
            >
              {data.textThree}
            </p>
          </div>
        </div>
        {/if}
        {#if data.cardsAmount == 4}
        <div
          class="relative flex flex-col bg-clip-border rounded-xl bg-transparent text-gray-700 shadow-md relative grid min-h-[30rem] items-end overflow-hidden rounded-xl"
        >
          <img
            src={data.imgFour}
            alt="bg"
            class="absolute inset-0 h-full w-full object-cover object-center rotationimg"
          />
          <div class="absolute inset-0 bg-black/70"></div>
          <div class="p-20 relative flex flex-col b">
            <h4
              class="block antialiased tracking-normal font-sans text-2xl font-semibold leading-snug text-center text-white"
            >
              {data.titleFour}
            </h4>
            <p
              class="block antialiased font-sans text-base font-light leading-relaxed text-center text-white my-2 font-normal"
            >
              {data.textFour}
            </p>
          </div>
        </div>
        {/if}
      </div>
    </section>
  </div>
{/await}

<style>
  .spaceyt {
    margin-left: 140px;
    margin-top: 40px;
  }

  .m {
    margin-bottom: 110px;
  }

  .o {
    margin-bottom: 90px;
  }

  .b {
    margin-bottom: 60px;
  }

  .rotationimg {
    transform: scaleX(-1);
  }

  .animate-charcter {
    text-transform: uppercase;
    background-image: linear-gradient(
      -225deg,
      #231557 0%,
      #44107a 29%,
      #ffffff 67%,
      #fff800 100%
    );
    background-size: auto auto;
    background-clip: border-box;
    background-size: 200% auto;
    color: #fff;
    background-clip: text;
    text-fill-color: transparent;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: textclip 2s linear infinite;
    display: inline-block;
    font-size: 55px;
  }

  @keyframes textclip {
    to {
      background-position: 200% center;
    }
  }
</style>
