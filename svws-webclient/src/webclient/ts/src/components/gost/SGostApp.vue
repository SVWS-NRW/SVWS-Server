<template>
	<div v-if="bezeichnung_abiturjahr" class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span class="inline-block mr-3">{{ bezeichnung_abiturjahr }}</span>
				<br>
				<span class="opacity-50">{{ jahrgang ? jahrgang : '–' }}</span>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="routeGost.children_records" :hidden="children_hidden" v-model="selectedRoute" class="router--gost-kursplanung">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<icon-graduation-cap />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { RouterView } from "vue-router";
	import { routeGost } from "~/router/apps/RouteGost";
	import { GostAppProps } from "./SGostAppProps";

	const props = defineProps<GostAppProps>();

	const selectedRoute = routeGost.getChildRouteSelector();
	const children_hidden = routeGost.children_hidden();


	const jahrgang: ComputedRef<string | undefined> = computed(() => {
		return props.auswahl?.jahrgang ?? undefined;
	});

	const bezeichnung_abiturjahr: ComputedRef<string | undefined> = computed(() => {
		return props.auswahl?.bezeichnung ?? undefined;
	});

</script>

<style lang="postcss">
.router--gost-kursplanung .router-tab-bar--panel {
	@apply h-px;
}

.router--gost-kursplanung .content-card--blockungsuebersicht {
	@apply gap-x-16;
}
</style>
