<template>
	<div v-if="visible" class="app-container">
		<s-card-kurs-basisdaten :data="data" :list-jahrgaenge="listJahrgaenge" :map-jahrgaenge="mapJahrgaenge"
			:list-lehrer="listLehrer" :map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
	import { DataKurs } from "~/apps/kurse/DataKurs";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKurseDaten } from "~/router/apps/kurse/RouteKurseDaten";

	const props = defineProps<{
		data: DataKurs;
		schule: DataSchuleStammdaten;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();


	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKurseDaten.hidden());
	});

</script>
