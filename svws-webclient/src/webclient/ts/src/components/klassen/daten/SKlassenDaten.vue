<template>
	<div v-if="visible" class="app-container">
		<s-card-klasse-basisdaten :data="data" :list-jahrgaenge="listJahrgaenge" :map-jahrgaenge="mapJahrgaenge" />
		<s-card-klasse-klassenleitungen :data="data" :list-lehrer="listLehrer" :map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
	import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";

	const props = defineProps<{
		item: ShallowRef<KlassenListeEintrag | undefined>,
		data: DataKlasse,
		listLehrer: ListLehrer,
		mapLehrer: Map<number, LehrerListeEintrag>,
		listJahrgaenge: ListJahrgaenge,
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>,
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKlassenDaten.hidden()) && (props.item.value !== undefined);
	});

</script>
