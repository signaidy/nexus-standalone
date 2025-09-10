<script lang="ts">
  import { enhance } from "$app/forms";
  import * as Dialog from "$lib/components/ui/dialog";
  import { Button } from "$lib/components/ui/button";
  import { Input } from "$lib/components/ui/input";
  import { ArrowRight, Star, Send } from "lucide-svelte";

  // function renderCommentary(commentary: Commentary): string {
  //   let padding = 8;
  //   let html = `<div class="flex flex-col gap-y-1 bg-muted p-1 rounded-lg">
  //                 <div class="font-bold text-sm">${
  //                   commentary.userName
  //                 }<span class="font-normal text-sm text-muted-foreground ml-1">${
  //                   commentary.creationDate.split(" ")[0]
  //                 }</span></div>
  //                 <div class="flex gap-x-4">
  //                   <div class="flex flex-col gap-y-1 border-r pr-4 border-black">
  //                 <div class="text-muted-foreground text-sm">ID: ${
  //                   commentary.id
  //                 }</div>
  //                 ${
  //                   commentary.parentComment === 0
  //                     ? ""
  //                     : `<div class="text-muted-foreground text-sm">Replying to ${commentary.parentComment}</div>`
  //                 }
  //                 </div>
  //                 <form
  //           method="POST"
  //           action="?/createCommentary"
  //           use:enhance={enhanceReplyForm}
  //           class="flex flex-col gap-y-1"
  //         >
  //                   <div class="text-xs font-medium">Reply</div>
  //                   <input type="hidden" name="parentId" value=${
  //                     commentary.id
  //                   } />
  //                   <input type="hidden" name="userId" value=${user.userId} />
  //                   <input type="hidden" name="flightId" value=${
  //                     hotel._id
  //                   } />
  //                   <div class="flex gap-x-2">
  //                     <input class="p-2 rounded-lg text-sm" name="content" placeholder="Leave a comment..." required />
  //                     <button data-button-root type="submit" class="p-3 flex items-center text-sm font-medium bg-primary text-white rounded-lg"
  //                   >Reply</button>
  //                     </div>
  //                   </form>
  //                 </div>
  //                 <div class="font-medium text-sm">${commentary.content}</div>`;
  //   if (commentary.children && commentary.children.length > 0) {
  //     html += `<div class="flex flex-col gap-y-1 border-l border-black" style="padding-left: ${padding}px">`;
  //     commentary.children.forEach((child) => {
  //       padding += 8;
  //       html += renderCommentary(child);
  //     });
  //     html += `</div>`;
  //   }
  //   html += `</div>`;
  //   return html;
  // }

  export let hotel;
  export let user: User;
  // export let form;
  let createLoading = false;
</script>

<Dialog.Root>
  <Dialog.Trigger class="self-start">
    <Button variant="link" class="group p-0">
      Hotel Details
      <ArrowRight
        class="shrink-0 ml-2 h-4 w-4 transition-transform group-hover:-rotate-45"
      />
    </Button>
  </Dialog.Trigger>
  <Dialog.Content>
    <Dialog.Header>
      <Dialog.Title>Hotel Information</Dialog.Title>
    </Dialog.Header>
    <div class="flex">
      <div class="flex flex-col gap-y-5 p-1 w-1/2 h-96">
        <form
          use:enhance={() => {
            return async ({ update }) => {
              await update();
            };
          }}
          method="POST"
          class="flex flex-col gap-y-2"
        >
          <div class="font-bold">Ratings</div>
          <!-- {#if form?.error}
            <div class="text-sm text-red-500 font-medium">
              Error: {form.error}
            </div>
          {/if} -->
          <div class="flex gap-x-1">
            {#each Array(5) as _, index}
              {#if index < hotel.reviews.average}
                <form
                  method="POST"
                  action="?/createRating"
                  class="cursor-pointer"
                  use:enhance={() => {
                    createLoading = true;
                    return async ({ update }) => {
                      await update();
                      createLoading = false;
                    };
                  }}
                >
                  <Button
                    type="submit"
                    variant="ghost"
                    class="p-2 text-base"
                    disabled={createLoading}
                  >
                    <Star class="h-5 w-5 fill-yellow-500 shrink-0" />
                  </Button>
                  <input type="hidden" name="userId" value={user.userId} />
                  <input
                    type="hidden"
                    name="flightId"
                    value={hotel._id}
                  />
                  <input type="hidden" name="rating" value={index + 1} />
                </form>
              {:else}
                <form
                  method="POST"
                  action="?/createRating"
                  class="cursor-pointer"
                  use:enhance={() => {
                    createLoading = true;
                    return async ({ update }) => {
                      await update();
                      createLoading = false;
                    };
                  }}
                >
                  <Button
                    type="submit"
                    variant="ghost"
                    class="p-2 text-base"
                    disabled={createLoading}
                  >
                    <Star class="h-5 w-5 shrink-0" />
                  </Button>
                  <input type="hidden" name="userId" value={user.userId} />
                  <input
                    type="hidden"
                    name="flightId"
                    value={hotel._id}
                  />
                  <input type="hidden" name="rating" value={index + 1} />
                </form>
              {/if}
            {/each}

            <Star class="h-5 w-5" />
            <Star class="h-5 w-5" />
            <Star class="h-5 w-5" />
            <Star class="h-5 w-5" />
            <Star class="h-5 w-5" />
          </div>
          <div class="font-medium text-muted-foreground text-sm">
            {hotel.reviews.average} average rating | ({hotel.reviews.count} reviews)
          </div>
        </form>
        <div class="flex flex-col gap-y-2 overflow-y-auto">
          <div class="font-bold">Hotel Description</div>
          <div class="text-sm">
            {hotel.description}
          </div>
        </div>
      </div>
      <div class="flex flex-col gap-y-5 p-1 w-1/2 h-96">
        <div class="flex flex-col gap-y-2">
          <div class="font-bold">Commentaries</div>
          <form
            method="POST"
            action="?/createCommentary"
            use:enhance={() => {
              createLoading = true;
              return async ({ update }) => {
                await update();
                createLoading = false;
              };
            }}
            class="flex gap-x-2"
          >
            <input type="hidden" name="parentId" value="null" />
            <input type="hidden" name="userId" value={user.userId} />
            <input type="hidden" name="flightId" value={hotel._id} />
            <Input name="content" placeholder="Leave a comment..." required />
            <Button type="submit" disabled={createLoading}
              ><Send class="h-4 w-4" /></Button
            >
          </form>
        </div>
        <!-- <div class="flex flex-col gap-y-10 overflow-y-auto"> -->
          <!-- {#each hotel.commentaries as commentary}
            {@html renderCommentary(commentary)}
          {/each} -->
        <!-- </div> -->
      </div>
    </div>
  </Dialog.Content>
</Dialog.Root>
