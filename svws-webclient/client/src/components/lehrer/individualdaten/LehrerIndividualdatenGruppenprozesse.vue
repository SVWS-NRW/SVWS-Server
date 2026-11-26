<template>
	<gruppenprozesse-aktionen :gruppenprozesse="lehrerAuswahlState.gruppenprozesse" />
	<div class="page page-grid-cards" />
</template>
<script setup lang="ts">

	import { computed } from "vue";

	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	import GruppenprozesseAktionen from "~/components/gruppenprozesse/GruppenprozesseAktionen.vue";
	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	const benutzerState = useBenutzerState();
	const lehrerAuswahlState = useLehrerAuswahlState();

	const hatKompetenzAnsehen = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_ANSEHEN));
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));

</script>
<style scoped>

</style>
