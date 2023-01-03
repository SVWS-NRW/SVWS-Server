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

	const { id, item, data, listLehrer, mapLehrer, listJahrgaenge, mapJahrgaenge, routename } = defineProps<{ 
		id?: number; 
		item?: KlassenListeEintrag, 
		data: DataKlasse, 
		listLehrer: ListLehrer,
		mapLehrer: Map<Number, LehrerListeEintrag>,
		listJahrgaenge: ListJahrgaenge,
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>,
		routename: string
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.afaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
