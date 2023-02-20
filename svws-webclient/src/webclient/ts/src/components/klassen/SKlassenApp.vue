<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ auswahl?.kuerzel ?? '' }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono">
					<i-ri-fingerprint-line />
					{{ auswahl?.id }}
				</svws-ui-badge>
			</div>
			<div>
				<div class="separate-items--custom">
					<span v-for="(l, i) in inputKlassenlehrer" :key="i" class="opacity-50"> {{ l.kuerzel }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKlassen.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-team-line />
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { routeKlassen } from "~/router/apps/RouteKlassen";
	import { KlassenAppProps } from "./SKlassenAppProps";

	const props = defineProps<KlassenAppProps>();

	const selectedRoute = routeKlassen.childRouteSelector;
	const children_hidden = routeKlassen.children_hidden();

	const inputKlassenlehrer: ComputedRef<LehrerListeEintrag[]> = computed(() =>
		(props.auswahl?.klassenLehrer?.toArray() as number[] || []).map(id => props.mapLehrer.get(id) || undefined).filter(l => l !== undefined) as LehrerListeEintrag[]
	);

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
