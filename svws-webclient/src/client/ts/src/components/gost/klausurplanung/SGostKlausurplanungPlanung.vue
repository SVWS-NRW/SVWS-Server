<template>
	<div class="flex h-full gap-4 mt-4">
		<svws-ui-content-card title="Zu planende Termine" class="flex flex-col">
			<ul class="flex flex-col gap-y-1">
				<li v-for="termin in termine" :key="termin.id">
					<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager"
						:faecher-manager="faecherManager"
						:map-lehrer="mapLehrer"
						:termin="termin"
						:kursmanager="kursmanager"
						:class="calculatCssClasses(termin)"
						@click="chooseTermin(termin);$event.stopPropagation()" />
				</li>
			</ul>
		</svws-ui-content-card>
		<svws-ui-content-card title="&nbsp;">
			<div v-if="selectedTermin === null">Bitte Termin durch Klick auswählen!</div>
			<div v-else>
				<s-gost-klausurplanung-planung-termin :termin="selectedTermin"
					:kursklausurmanager="kursklausurmanager"
					:faecher-manager="faecherManager"
					:map-lehrer="mapLehrer"
					:kursmanager="kursmanager"
					:raummanager="(raummanager as GostKlausurraumManager)"
					:stundenplanmanager="stundenplanmanager"
					:erzeuge-klausurraum="erzeugeKlausurraum"
					:patch-klausurraum="patchKlausurraum"
					:setze-raum-zu-schuelerklausuren="setzeRaumZuSchuelerklausuren" />
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, StundenplanRaum, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager, GostKlausurraumManager, GostSchuelerklausur, List, GostKlausurenCollectionSkrsKrs } from '@core';
	import type { GostKlausurraum } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		stundenplanmanager: StundenplanManager;
		erzeugeKlausurraum: (raum: GostKlausurraum) => Promise<GostKlausurraum>;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		erzeugeKlausurraummanager: (termin: GostKlausurtermin) => Promise<GostKlausurraumManager>;
		setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum, sks: List<GostSchuelerklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
	}>();

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

	const termine = computed(() => props.kursklausurmanager().getKlausurtermineMitDatum());

	const calculatCssClasses = (termin: GostKlausurtermin) => ({
		"bg-green-100": selectedTermin.value !== null && selectedTermin.value.id === termin.id,
	});

</script>
