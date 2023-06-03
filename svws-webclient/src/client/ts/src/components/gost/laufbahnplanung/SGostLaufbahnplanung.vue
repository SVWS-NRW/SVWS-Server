<template>
	<div class="page--content mt-8">
		<s-card-gost-laufbahnplanung :list-belegpruefungs-ergebnisse="listBelegpruefungsErgebnisse" :config="config"
			:set-gost-belegpruefungs-art="setGostBelegpruefungsArt" :gost-belegpruefungs-art="gostBelegpruefungsArt"
			:goto-laufbahnplanung="gotoLaufbahnplanung" :get-pdf-wahlbogen="getPdfWahlbogen" :abiturjahr="abiturjahr" :api-status="apiStatus" />
	</div>
</template>

<script setup lang="ts">

	import type { GostLaufbahnplanungProps } from "./SGostLaufbahnplanungProps";

	defineProps<GostLaufbahnplanungProps>();

</script>
