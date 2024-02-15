<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-button type="secondary" @click="print"><i-ri-printer-line />Drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-schueler-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--flex h-full">
		<template v-if="stundenplan === undefined">
			<div class="svws-ui-empty">
				<i-ri-calendar-event-line />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan="stundenplan" :map-stundenplaene="mapStundenplaene" :goto-stundenplan="gotoStundenplan"
				:goto-wochentyp="gotoWochentyp" :goto-kalenderwoche="gotoKalenderwoche" :manager="manager" :wochentyp="wochentyp"
				:kalenderwoche="kalenderwoche" :ganzer-stundenplan="ganzerStundenplan" :set-ganzer-stundenplan="setGanzerStundenplan" />
			<router-view :key="$route.hash" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "@comp";
	import { onMounted, ref } from "vue";

	defineProps<StundenplanAuswahlProps>();

	const print = () => {
		window.print();
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
