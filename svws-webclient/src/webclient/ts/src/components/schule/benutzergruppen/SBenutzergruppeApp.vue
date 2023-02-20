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

	import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";

	import { routeSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";

	const props = defineProps<{
		item: ShallowRef<BenutzergruppeListeEintrag | undefined>;
	}>();

	const selectedRoute = routeSchuleBenutzergruppe.childRouteSelector;
	const children_hidden = routeSchuleBenutzergruppe.children_hidden();

	const id: ComputedRef<string> = computed(() => "ID: " + props.item.value?.id);
	const bezeichnung: ComputedRef<string> = computed(() => props.item.value?.bezeichnung ?? "");

	const visible: ComputedRef<boolean> = computed(() => props.item !== undefined);

</script>
