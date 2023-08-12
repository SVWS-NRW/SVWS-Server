<template>
	<svws-ui-sub-nav>
		<stundenplan-auswahl :stundenplan="stundenplan" :map-stundenplaene="mapStundenplaene" :goto-stundenplan="gotoStundenplan" :goto-wochentyp="gotoWochentyp"
			:goto-kalenderwoche="gotoKalenderwoche" :manager="manager" :wochentyp="wochentyp" :kalenderwoche="kalenderwoche" />
		<svws-ui-modal-hilfe class="ml-auto"> <hilfe-klassen-stundenplan /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>
	<div class="w-full h-full p-8">
		<template v-if="stundenplan === undefined">
			Derzeit liegt kein Stundenplan für diesen Lernabschnitt vor.
		</template>
		<template v-else>
			<router-view :key="$route.hash" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "@comp";

	defineProps<StundenplanAuswahlProps>();

</script>