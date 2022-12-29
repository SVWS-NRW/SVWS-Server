<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header :badge="id" badge-variant="light" badge-size="normal">
				<span> {{ bezeichnung }} </span>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="routeSchuleBenutzergruppe.children_records" :hidden="routeSchuleBenutzergruppe.children_hidden" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{ id?: number; item?: BenutzergruppeListeEintrag, routename: string }>();

	const selectedRoute = routeSchuleBenutzergruppe.getChildRouteSelector();

	const id: ComputedRef<string> = computed(() => "ID: " + props.id);
	const bezeichnung: ComputedRef<string> = computed(() => props.item?.bezeichnung.toString() || "");

</script>
