<template>
	<svws-ui-sub-nav>
		<stundenplan-auswahl :stundenplan="stundenplan" :map-stundenplaene="mapStundenplaene" :goto-stundenplan="gotoStundenplan" :goto-wochentyp="gotoWochentyp"
			:goto-kalenderwoche="gotoKalenderwoche" :manager="manager" :wochentyp="wochentyp" :kalenderwoche="kalenderwoche" />
		<svws-ui-modal-hilfe class="ml-auto"> <hilfe-schueler-stundenplan /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>
	<div class="w-full pl-9 pr-9 pt-8 pb-16">
		<div v-if="stundenplan === undefined">
			Derzeit liegt kein Stundenplan für diesen Lernabschnitt vor.
		</div>
		<div v-else class="flex flex-col">
			<div class="mt-2"> <router-view :key="$route.hash" /> </div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "@comp";

	defineProps<StundenplanAuswahlProps>();

</script>