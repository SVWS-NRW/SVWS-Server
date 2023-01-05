<template>
	<div v-if="visible" class="app-container">
		<s-card-kurs-basisdaten :item="item" :data="data" :list-jahrgaenge="listJahrgaenge" :map-jahrgaenge="mapJahrgaenge" 
			:list-lehrer="listLehrer" :map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
	import { DataKurs } from "~/apps/kurse/DataKurs";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeKurseDaten } from "~/router/apps/kurse/RouteKurseDaten";

	const { item, data, listJahrgaenge, mapJahrgaenge, listLehrer, mapLehrer } = defineProps<{ 
		id?: number;
		item?: KursListeEintrag;
		data: DataKurs;
		schule: DataSchuleStammdaten;
		listJahrgaenge: ListJahrgaenge;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
		routename: string;
	}>();


	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeKurseDaten.hidden) && (item !== undefined);
	});

</script>
