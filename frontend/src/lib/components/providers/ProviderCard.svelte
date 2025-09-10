<script lang="ts">
    import { Button } from "$lib/components/ui/button";
    import {cn} from "$lib/utils";
    import { enhance } from "$app/forms";
    import { Label } from "$lib/components/ui/label";
    import { Input } from "$lib/components/ui/input";

    import { Loader2, Trash2 } from "lucide-svelte";
    export let provider;
    export let form;
    let editing = false;
    let loading = false;
</script>
{#if editing === false}
<div class="flex flex-col gap-y-2">
    <div class="text-sm font-bold">User #{provider.id} - {provider.providerName}</div>
    <div class="flex flex-wrap border rounded-lg gap-x-3 w-fit">
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">URL</div>
        <div class="text-muted-foreground">{provider.providerUrl}</div>
        <div class="font-medium">Type</div>
        <div class="text-muted-foreground">{provider.type}</div>
      </div>
      <div class="flex flex-col text-sm gap-y-1 border-r p-3">
        <div class="font-medium">Gains</div>
        <div class="text-muted-foreground">
            {#if provider.type === 'AEROLINEA'}
                <!-- Display gains for flights as a percentage -->
                {(provider.gainsFlights * 100).toFixed(2)}%
            {:else if provider.type === 'HOTEL'}
                <!-- Display gains for hotels as a percentage -->
                {(provider.gainsHotel * 100).toFixed(2)}%
            {/if}
        </div>
        <div class="font-medium">Discount Percentage</div>
        <div class="text-muted-foreground">{provider.percentageDiscount === null ? "0" : provider.percentageDiscount}%</div>
      </div>
      <div class="pr-3 py-3">
        <Button on:click={() => (editing = true)}>Edit</Button>
      </div>
      <form action="?/deleteProvider" method="post">
        <input type="hidden" name="providerId" value={provider.id} />
        <div class="pr-3 py-3">
            <Button type="submit"><Trash2 /></Button>
        </div>
      </form>
    </div>
  </div>
  {:else}
    <form
    method="POST"
    action="?/updateProvider"
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
    <input type="hidden" name="providerId" value={provider.id} />
    <div class="text-sm font-bold">User #{provider.id} - {provider.providerName}</div>

    {#if form?.error}
        <div class="text-sm text-red-500 font-medium">
        Error: {form.error}
        </div>
    {/if}
    <div class="flex gap-x-3">
        <div class="flex flex-col gap-y-3">
        <div class="flex flex-col text-sm gap-y-1">
            <Label for="providerName">Business Name</Label>
            <Input id="providerName" name="providerName" value={provider.providerName} />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
            <Label for="providerUrl">URL</Label>
            <Input
            id="providerUrl"
            name="providerUrl"
            value={provider.providerUrl}
            />
        </div>
        <div class="flex flex-col text-sm gap-y-1">
            <Label for="type">Type</Label>
            <select
            name="type"
            id="type"
            class="rounded-md border border-input px-3 py-2 bg-background text-sm h-10 placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            >
            <option value="AEROLINEA">AEROLINEA</option>
            <option value="HOTEL">HOTELERIA</option>
            </select>
        </div>
        </div>
        <div class="flex flex-col gap-y-3">
        <div class="flex flex-col text-sm gap-y-1">
            <Label for="gains">Gains</Label>
            {#if provider.type == "AEROLINEA"}
                <Input id="gains" name="gains" value={provider.gainsFlights} type="number" step="0.01" min="0" max="1"/>
            {:else}
                <Input id="gains" name="gains" value={provider.gainsHotel} type="number" step="0.01" min="0" max="1"/>
            {/if}
        </div>
        <div class="flex flex-col text-sm gap-y-1">
            <Label for="percentage">Discount Percentage</Label>
            <Input
            id="percentage"
            name="percentage"
            type="number"
            step="0.01" min="0" max="1"
            value={provider.percentageDiscount === null ? "0" : provider.percentageDiscount}
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