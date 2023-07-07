<template>
	<div class="page--content">
		<s-card-gost-laufbahnfehler :list-belegpruefungs-ergebnisse="listBelegpruefungsErgebnisse" :config="config"
			:set-gost-belegpruefungs-art="setGostBelegpruefungsArt" :gost-belegpruefungs-art="gostBelegpruefungsArt"
			:goto-laufbahnplanung="gotoLaufbahnplanung" :get-pdf-wahlbogen="getPdfWahlbogen" :abiturjahr="abiturjahr" :api-status="apiStatus" />
	</div>
</template>

<script setup lang="ts">

	import type { GostLaufbahnfehlerProps } from "./SGostLaufbahnfehlerProps";

	defineProps<GostLaufbahnfehlerProps>();

</script>
