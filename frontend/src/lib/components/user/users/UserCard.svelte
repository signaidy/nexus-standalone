<script lang="ts">
  import {cn} from "$lib/utils";
  import { enhance } from "$app/forms";

  import { Button } from "$lib/components/ui/button";
  import { Label } from "$lib/components/ui/label";
  import { Input } from "$lib/components/ui/input";

  import { Loader2 } from "lucide-svelte";

  export let user: User;
  export let form;

  let editing = false;
  let loading = false;
</script>

{#if editing === false}
  <div class="flex flex-col gap-y-2">
    <div class="text-sm font-bold">User #{user.id} - {user.firstName + " " + user.lastName}</div>
    <div class="flex flex-wrap border rounded-lg gap-x-3 w-fit">
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Email</div>
        <div class="text-muted-foreground">{user.email}</div>
        <div class="font-medium">Age</div>
        <div class="text-muted-foreground">{user.age}</div>
      </div>
      <div class="flex flex-col  text-sm gap-y-1 border-r p-3">
        <div class="font-medium">First Name</div>
        <div class="text-muted-foreground">{user.firstName}</div>
        <div class="font-medium">Last Name</div>
        <div class="text-muted-foreground">{user.lastName}</div>
      </div>
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Origin Country</div>
        <div class="text-muted-foreground">{user.country}</div>
        <div class="font-medium">Passport Number</div>
        <div class="text-muted-foreground">{user.passport}</div>
      </div>
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Role</div>
        <div class="text-muted-foreground">
            {#if user.role === 'ROLE_USER'}
                user
            {:else if user.role === 'ROLE_ADMIN'}
                admin
            {:else if user.role === 'ROLE_EMPLOYEE'}
                employee
            {/if}
        </div>
        <div class="font-medium">Discount Percentage</div>
        <div class="text-muted-foreground">{user.percentage === null ? "0" : user.percentage}%</div>
      </div>
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Entry Date</div>
        <div class="text-muted-foreground">{user.createdAt}</div>
      </div>
      <div class="pr-3 py-3">
        <Button on:click={() => (editing = true)}>Edit</Button>
      </div>
    </div>
  </div>
{:else}
  <form
    method="POST"
    action="?/updateUser"
    class="flex border rounded-lg p-3 w-fit flex-col gap-y-3"
    use:enhance={() => {
      loading = true;

      return async ({ update }) => {
        await update();
        editing = false;
        loading = false;
      };
    }}
  >
    <input type="hidden" name="userId" value={user.id} />
    <div class="text-sm font-bold">User #{user.id} - {user.firstName + " " + user.lastName}</div>

    {#if form?.error}
      <div class="text-sm text-red-500 font-medium">
        Error: {form.error}
      </div>
    {/if}
    <div class="flex gap-x-3">
      <div class="flex flex-col gap-y-3">
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="firstName">First Name</Label>
          <Input id="firstName" name="firstName" value={user.firstName} />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="lastName">Last Name</Label>
          <Input id="lastName" name="lastName" value={user.lastName} />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="originCountry">Origin Country</Label>
          <Input
            id="originCountry"
            name="originCountry"
            value={user.country}
          />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
          <div class="font-medium">Entry Date</div>
          <div class="text-muted-foreground">{user.createdAt}</div>
        </div>
      </div>
      <div class="flex flex-col gap-y-3">
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="passportNumber">Passport Number</Label>
          <Input
            id="passportNumber"
            name="passportNumber"
            value={user.passport}
          />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="role">Role</Label>
          <select
            name="role"
            id="role"
            class="rounded-md border border-input px-3 py-2 bg-background text-sm h-10 placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <option value="ROLE_USER">User</option>
            <option value="ROLE_ADMIN">Admin</option>
            <option value="ROLE_EMPLOYEE">Enterprise</option>
          </select>
        </div>
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="age">Age</Label>
          <Input id="age" name="age" value={user.age} type="number" />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
          <Label for="percentage">Discount Percentage</Label>
          <Input
            id="percentage"
            name="percentage"
            type="number"
            value={user.percentage === null ? "0" : user.percentage}
          />
        </div>
      </div>
    </div>
    <div class="flex gap-x-3">
      <Button on:click={() => (editing = false)} disabled={loading}>Discard Changes</Button>
      <Button type="submit" disabled={loading}>Confirm Changes<Loader2 class={cn("ml-3 h-4 w-4 animate-spin", !loading && "hidden")}/></Button>
    </div>
  </form>
{/if}
