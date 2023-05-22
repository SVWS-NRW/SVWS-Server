<template>
	<div class="app-container">
		<s-card-gost-laufbahnplanung :list-belegpruefungs-ergebnisse="listBelegpruefungsErgebnisse" :config="config" :set-gost-belegpruefungs-art="setGostBelegpruefungsArt" :gost-belegpruefungs-art="gostBelegpruefungsArt" :goto-laufbahnplanung="gotoLaufbahnplanung" />
	</div>
</template>

<script setup lang="ts">

	import type { GostLaufbahnplanungProps } from "./SGostLaufbahnplanungProps";

	defineProps<GostLaufbahnplanungProps>();

</script>
