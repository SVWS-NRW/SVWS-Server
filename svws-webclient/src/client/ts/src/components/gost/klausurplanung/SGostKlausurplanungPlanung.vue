<template>
	<svws-ui-content-card>
		<div class="flex h-full gap-4 mt-4">
			<div class="flex flex-col">
				<div class="text-headline-md">Zu Planen:</div>
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
			</div>
			<div class="flex flex-row flex-wrap gap-4 w-full">
				<div v-if="selectedTermin === null" class="text-headline">Bitte Termin durch Klick auswählen!</div>
				<div v-else>
					<svws-ui-button type="primary" @click="erzeugeNeuenRaum()">Erstelle Klausurraum</svws-ui-button>
					<s-gost-klausurplanung-planung-raum v-for="raum in raummanager?.getKlausurraeume()"
						:key="raum.id"
						:stundenplanmanager="stundenplanmanager"
						:raum="raum"
						:manager="(raummanager as GostKlausurraumManager)"
						:patch-klausurraum="patchKlausurraum" />
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, StundenplanRaum, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager, GostKlausurraumManager } from '@core';
	import { GostKlausurraum } from '@core';
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
	}>();

	const erzeugeNeuenRaum = async () => {
		if (selectedTermin.value === null)
			return null;
		let nR = new GostKlausurraum();
		nR.idTermin = selectedTermin.value.id;
		nR = await props.erzeugeKlausurraum(nR);
		raummanager.value?.addKlausurraum(nR);
	}

	const raummanager = ref<GostKlausurraumManager | null>(null);

	const chooseTermin = async (termin: GostKlausurtermin) => {
		if (selectedTermin.value !== null) {
			selectedTermin.value = null;
			return;
		}
		selectedTermin.value = termin;
		raummanager.value = await props.erzeugeKlausurraummanager(termin);
	}

	const selectedTermin = ref<GostKlausurtermin | null>(null);

	const termine = computed(() => props.kursklausurmanager().getKlausurtermine());

	const calculatCssClasses = (termin: GostKlausurtermin) => ({
		"bg-green-100": selectedTermin.value !== null && selectedTermin.value.id === termin.id,
	});

</script>
