<template>
	<div v-if="visible" class="app-container">
		<s-card-klasse-basisdaten :data="data" :list-jahrgaenge="listJahrgaenge" :map-jahrgaenge="mapJahrgaenge" />
		<s-card-klasse-klassenleitungen :data="data" :list-lehrer="listLehrer" :map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";

	const { item, data, listLehrer, mapLehrer, listJahrgaenge, mapJahrgaenge } = defineProps<{ 
		id?: number; 
		item?: KlassenListeEintrag, 
		data: DataKlasse, 
		schule: DataSchuleStammdaten;
		listLehrer: ListLehrer,
		mapLehrer: Map<Number, LehrerListeEintrag>,
		listJahrgaenge: ListJahrgaenge,
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>,
		routename: string
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKlassenDaten.hidden) && (item !== undefined);
	});

</script>
