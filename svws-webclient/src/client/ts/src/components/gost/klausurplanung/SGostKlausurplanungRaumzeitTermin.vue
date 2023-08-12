<template>
	<svws-ui-content-card title="Klausuren ohne Raum" v-if="klausurenOhneRaum.size() > 0">
		<table>
			<s-gost-klausurplanung-klausur v-for="klausur of klausurenOhneRaum" :key="klausur.id"
				:klausur="klausur"
				:kursklausurmanager="kursklausurmanager"
				:kursmanager="kursmanager"
				:map-lehrer="mapLehrer" />
		</table>
	</svws-ui-content-card>
	<div class="flex flex-col flex-wrap gap-4 w-full">
		<svws-ui-button type="primary" @click="erzeugeNeuenRaum()">Erstelle Klausurraum</svws-ui-button>
		<s-gost-klausurplanung-raumzeit-raum v-for="raum in raummanager?.getKlausurraeume()"
			:key="raum.id"
			:stundenplanmanager="stundenplanmanager"
			:raum="raum"
			:raummanager="(raummanager as GostKlausurraumManager)"
			:patch-klausurraum="patchKlausurraum"
			:setze-raum-zu-schuelerklausuren="setzeRaumZuSchuelerklausuren"
			:faecher-manager="faecherManager"
			:kursklausurmanager="kursklausurmanager"
			:kursmanager="kursmanager"
			:map-lehrer="mapLehrer" />
	</div>
</template>

<script setup lang="ts">
	import type { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, StundenplanRaum, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager, GostKlausurraumManager, GostKursklausur, GostKlausurenCollectionSkrsKrs, GostSchuelerklausur, List } from '@core';
	import { GostKlausurraum } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		stundenplanmanager: StundenplanManager;
		raummanager: GostKlausurraumManager;
		erzeugeKlausurraum: (raum: GostKlausurraum) => Promise<GostKlausurraum>;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum, sks: List<GostSchuelerklausur>, manager: GostKlausurraumManager) => Promise<GostKlausurenCollectionSkrsKrs>;
	}>();

	//const klausurenOhneRaum = props.kursklausurmanager().getKursklausurenByTermin(props.termin.id);
	const klausurenOhneRaum = computed(() => props.raummanager.getKursklausurenInRaum(-1, props.kursklausurmanager()));

	const erzeugeNeuenRaum = async () => {
		let nR = new GostKlausurraum();
		nR.idTermin = props.termin.id;
		nR = await props.erzeugeKlausurraum(nR);
		props.raummanager.addKlausurraum(nR);
	}

</script>
