<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono">
					<i-ri-fingerprint-line />
					{{ id }}
				</svws-ui-badge>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeSchuleBenutzergruppe.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-community-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { routeSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";
	import { BenutzergruppeAppProps } from "./SBenutzergruppeAppProps";

	const props = defineProps<BenutzergruppeAppProps>();

	const selectedRoute = routeSchuleBenutzergruppe.childRouteSelector;
	const children_hidden = routeSchuleBenutzergruppe.children_hidden();

	const id: ComputedRef<string> = computed(() => "ID: " + props.auswahl()?.id ?? "?");
	const bezeichnung: ComputedRef<string> = computed(() => props.auswahl()?.bezeichnung ?? "-----");

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
