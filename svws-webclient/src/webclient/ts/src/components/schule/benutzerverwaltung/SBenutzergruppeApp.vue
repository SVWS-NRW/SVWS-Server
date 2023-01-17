<template>
	<div v-if="props.id !== undefined">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ bezeichnung }}</span>
				<svws-ui-badge title="ID" variant="light">{{ id }}</svws-ui-badge>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeSchuleBenutzergruppe.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-community-line/>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{ id?: number; item?: BenutzergruppeListeEintrag, routename: string }>();

	const selectedRoute = routeSchuleBenutzergruppe.getChildRouteSelector();
	const children_hidden = routeSchuleBenutzergruppe.children_hidden();

	const id: ComputedRef<string> = computed(() => "ID: " + props.id);
	const bezeichnung: ComputedRef<string> = computed(() => props.item?.bezeichnung.toString() || "");

</script>
