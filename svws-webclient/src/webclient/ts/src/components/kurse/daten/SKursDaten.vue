<template>
	<div class="app-container">
		<s-card-kurs-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" :map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { DataKurs } from "~/apps/kurse/DataKurs";

	defineProps<{
		data: DataKurs;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

</script>
