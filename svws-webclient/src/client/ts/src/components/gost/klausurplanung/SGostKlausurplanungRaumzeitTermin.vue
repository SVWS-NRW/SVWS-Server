<template>
	<svws-ui-content-card title="Daten zur Klausur">
		<table class="border">
			<tr>
				<th>Datum</th>
				<td>{{ termin.datum }}</td>
			</tr>
			<tr>
				<th>Startzeit</th>
				<td>{{ termin.startzeit }}</td>
			</tr>

			<tr>
				<th>Anzahl Klausurschreiber:innen</th>
				<td>{{ kursklausurmanager().gibSchuelerIDsZuTermin(termin.id)?.size() }}</td>
			</tr>
		</table>
	</svws-ui-content-card>

	<svws-ui-content-card title="Klausuren ohne Raum" v-if="klausurenOhneRaum.size() > 0">
		<table>
			<s-gost-klausurplanung-klausur v-for="klausur of klausurenOhneRaum" :key="klausur.id"
				:klausur="klausur"
				:termin="termin"
				:kursklausurmanager="kursklausurmanager"
				:kursmanager="kursmanager"
				:map-lehrer="mapLehrer" />
		</table>
	</svws-ui-content-card>
	<svws-ui-content-card class="f-full" title="Klausurräume">
		<template #actions>
			<svws-ui-button size="small" type="secondary" @click="erzeugeNeuenRaum()">Erstelle Klausurraum <i-ri-add-circle-line class="-mr-1" /></svws-ui-button>
		</template>
		<div class="flex flex-col flex-wrap gap-4 w-full">
			<s-gost-klausurplanung-raumzeit-raum v-for="raum in raummanager?.getKlausurraeume()"
				:key="raum.id"
				:stundenplanmanager="stundenplanmanager"
				:raum="raum"
				:raummanager="(raummanager as GostKlausurraumManager)"
				:patch-klausurraum="patchKlausurraum"
				:setze-raum-zu-schuelerklausuren="setzeRaumZuSchuelerklausuren"
				:patch-kursklausur="patchKursklausur"
				:faecher-manager="faecherManager"
				:kursklausurmanager="kursklausurmanager"
				:kursmanager="kursmanager"
				:map-lehrer="mapLehrer" />
		</div>
	</svws-ui-content-card>
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
		patchKursklausur: (id: number, klausur: Partial<GostKursklausur>) => Promise<boolean>;
	}>();

	const klausurenOhneRaum = computed(() => props.raummanager.getKursklausurenInRaum(-1, props.kursklausurmanager()));

	const erzeugeNeuenRaum = async () => {
		let nR = new GostKlausurraum();
		nR.idTermin = props.termin.id;
		nR = await props.erzeugeKlausurraum(nR);
		props.raummanager.addKlausurraum(nR);
	}

</script>
