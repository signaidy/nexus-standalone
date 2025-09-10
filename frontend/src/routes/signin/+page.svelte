<script lang="ts">
  import { enhance } from "$app/forms";

  import { Label } from "$lib/components/ui/label";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import { Loader2 } from "lucide-svelte";

  export let form;

  let loading = false;
</script>

<div
  class="min-h-[calc(100vh-5rem)] bg-[url('$lib/assets/home-background.jpg')] bg-cover bg-fixed"
>
  <div class="flex container p-8 justify-center items-center h-full">
    <form
      method="POST"
      use:enhance={() => {
        loading = true;

        return async ({ update }) => {
          await update();
          loading = false;
        };
      }}
      class="flex flex-col gap-y-5 rounded-lg p-8 bg-background"
    >
      <div class="flex flex-col">
        <div class="flex items-center">
          <div class="font-bold text-xl">Sign In</div>
          <Loader2
            class="shrink-0 ml-3 h-4 w-4 animate-spin {!loading && 'hidden'}"
          />
        </div>
        <Button variant="link" class="p-0 self-baseline" href="/login">
          Already have an account? Log In
        </Button>
        {#if form?.error}
          <div class="text-sm text-red-500 font-medium">
            Error: {form.error}
          </div>
        {/if}
      </div>
      <div class="grid grid-cols-2">
        <div class="flex flex-col gap-y-5 border-r pr-5">
          <div class="flex flex-col gap-y-2">
            <Label for="email">Email</Label>
            <Input id="email" name="email" type="email" required />
          </div>
          <div class="flex flex-col gap-y-2">
            <Label for="password">Password</Label>
            <Input
              id="password"
              name="password"
              type="password"
              autocomplete="new-password"
              required
            />
          </div>
          <div class="flex flex-col gap-y-2">
            <Label for="confirmedPassword">Confirm Password</Label>
            <Input
              id="confirmedPassword"
              name="confirmedPassword"
              type="password"
              autocomplete="new-password"
              required
            />
          </div>
          <div class="flex flex-col gap-y-2">
            <Label for="age">Age</Label>
            <Input id="age" name="age" type="number" required />
          </div>
        </div>
        <div class="flex flex-col gap-y-5 pl-5">
          <div class="flex flex-col gap-y-2">
            <Label for="firstName">Names</Label>
            <Input id="firstName" name="firstName" required />
          </div>
          <div class="flex flex-col gap-y-2">
            <Label for="lastName">Last Names</Label>
            <Input id="lastName" name="lastName" required />
          </div>
          <div class="flex flex-col gap-y-2">
            <Label for="originCountry">Country of Origin</Label>
            <Input id="originCountry" name="originCountry" required />
          </div>
          <div class="flex flex-col gap-y-2">
            <Label for="passportNumber">Passport Number</Label>
            <Input
              id="passportNumber"
              name="passportNumber"
              type="number"
              required
            />
          </div>
        </div>
      </div>
      <div class="flex flex-col gap-y-2">
        <Label for="captcha">🤖?</Label>
        <!-- <Input id="captcha" /> -->
      </div>
      <Button type="submit" disabled={loading}>Sign In</Button>
    </form>
  </div>
</div>
