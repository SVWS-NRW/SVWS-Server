<template>
	<div v-if="visible" class="app-container">
		<s-card-klasse-basisdaten :data="data" />
		<s-card-klasse-klassenleitungen :data="data" :map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">

	import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";

	const { id, item, data, mapLehrer, routename } = defineProps<{ 
		id?: number; 
		item?: KlassenListeEintrag, 
		data: DataKlasse, 
		mapLehrer: Map<Number, LehrerListeEintrag>,
		routename: string
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		//return this.$app.afaecher.visible; //TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
