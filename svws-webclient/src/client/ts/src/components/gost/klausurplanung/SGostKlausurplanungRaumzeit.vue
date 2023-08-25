<template>
	<svws-ui-sub-nav>
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" />
		<svws-ui-modal-hilfe class="ml-auto"> <s-gost-klausurplanung-raumzeit-hilfe /> </svws-ui-modal-hilfe>
	</svws-ui-sub-nav>

	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<svws-ui-content-card title="Zu planende Termine" class="flex flex-col">
			<ul class="flex flex-col gap-y-1">
				<li v-for="termin in termine" :key="termin.id">
					<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager"
						:jahrgangsdaten="jahrgangsdaten"
						:faecher-manager="faecherManager"
						:map-lehrer="mapLehrer"
						:termin="termin"
						:kursmanager="kursmanager"
						:class="calculatCssClasses(termin)"
						@click="chooseTermin(termin);$event.stopPropagation()" />
				</li>
			</ul>
		</svws-ui-content-card>
		<div v-if="selectedTermin === null">Bitte Termin durch Klick auswählen!</div>
		<div class="h-full" v-else>
			<s-gost-klausurplanung-raumzeit-termin :termin="selectedTermin"
				:kursklausurmanager="kursklausurmanager"
				:faecher-manager="faecherManager"
				:map-lehrer="mapLehrer"
				:kursmanager="kursmanager"
				:raummanager="(raummanager as GostKlausurraumManager)"
				:stundenplanmanager="stundenplanmanager"
				:erzeuge-klausurraum="erzeugeKlausurraum"
				:loesche-klausurraum="loescheKlausurraum"
				:patch-klausurraum="patchKlausurraum"
				:patch-klausur-uhrzeit="patchKlausurUhrzeit"
				:setze-raum-zu-schuelerklausuren="setzeRaumZuSchuelerklausuren" />
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurtermin, GostKlausurraumManager } from '@core';
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungRaumzeitProps } from './SGostKlausurplanungRaumzeitProps';

	const props = defineProps<GostKlausurplanungRaumzeitProps>();

	const raummanager = ref<GostKlausurraumManager | null>(null);

	const chooseTermin = async (termin: GostKlausurtermin) => {
		if (selectedTermin.value !== null) {
			selectedTermin.value = null;
			return;
		}
		raummanager.value = await props.erzeugeKlausurraummanager(termin);
		selectedTermin.value = termin;
	}

	const selectedTermin = ref<GostKlausurtermin | null>(null);

	const termine = computed(() => props.kursklausurmanager().getKlausurtermineMitDatumByQuartal(props.quartalsauswahl.value));

	const calculatCssClasses = (termin: GostKlausurtermin) => ({
		"bg-green-100": selectedTermin.value !== null && selectedTermin.value.id === termin.id,
	});

</script>
