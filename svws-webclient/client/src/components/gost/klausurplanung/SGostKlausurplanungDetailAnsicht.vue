<template>
	<div class="page--content page--content--full relative flex-col">
		<s-gost-klausurplanung-detail-ansicht-termin v-for="termin in kursklausurmanager().terminMitDatumGetMenge()" :key="termin.id" :termin="termin" :erzeuge-klausurraummanager="erzeugeKlausurraummanager" :map-lehrer="mapLehrer" :stundenplanmanager="stundenplanmanager" />
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurplanungDetailAnsichtProps } from './SGostKlausurplanungDetailAnsichtProps';

	const props = defineProps<GostKlausurplanungDetailAnsichtProps>();

</script>
