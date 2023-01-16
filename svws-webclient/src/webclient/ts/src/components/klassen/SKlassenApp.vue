<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ item.value?.kuerzel?.toString() }}</span>
				<svws-ui-badge type="light">{{ "ID: " + item.value?.id }}</svws-ui-badge>
			</div>
			<div class="separate-items--custom">
				<span v-for="(l, i) in inputKlassenlehrer" :key="i" class="opacity-50"> {{ l.kuerzel }} </span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeKlassen.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-group-line />
	</div>
</template>

<script setup lang="ts">

	import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKlassen } from "~/router/apps/RouteKlassen";

	const { item, mapLehrer } = defineProps<{
		item: ShallowRef<KlassenListeEintrag | undefined>,
		schule: DataSchuleStammdaten;
		listLehrer: ListLehrer,
		mapLehrer: Map<Number, LehrerListeEintrag>,
	}>();

	const selectedRoute = routeKlassen.childRouteSelector;
	const children_hidden = routeKlassen.children_hidden();

	const inputKlassenlehrer: ComputedRef<LehrerListeEintrag[]> = computed(() =>
		(item.value?.klassenLehrer?.toArray() as Number[] || []).map(id => mapLehrer.get(id) || undefined).filter(l => l !== undefined) as LehrerListeEintrag[]
	);

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKlassen.hidden) && (item.value !== undefined);
	});

</script>
