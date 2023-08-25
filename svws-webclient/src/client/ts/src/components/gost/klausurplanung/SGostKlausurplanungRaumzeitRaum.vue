<template>
	<svws-ui-drop-data @drop="setKlausurToRaum" :drop-allowed="false" class="border">
		<svws-ui-content-card title="Kausurraum">
			<template #actions>
				<svws-ui-button type="danger" size="small" @click="loescheKlausurraum(raum.id, raummanager)"><i-ri-delete-bin-line /></svws-ui-button>
			</template>
			<svws-ui-multi-select title="Raum auswählen"
				v-model="stundenplanRaumSelected"
				@update:model-value="patchKlausurraum(raum.id, { idStundenplanRaum: stundenplanRaumSelected?.id }, raummanager)"
				:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
				:items="raummanager.raumVerfuegbarGetMengeAsList(stundenplanmanager.raumGetMengeAsList())" />
			<div>
				Belegung:
				<span v-if="raum.idStundenplanRaum !== null" :class="anzahlSuS > stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse ? 'text-red-700' : 'text-green-600'">{{ anzahlSuS }} / {{ stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse }}</span>
				<span v-else>{{ anzahlSuS }}</span>
			</div>
			<svws-ui-content-card title="Klausuren im Raum">
				<table>
					<div v-if="klausurenImRaum.size() === 0">Keine Klausuren im Raum</div>
					<s-gost-klausurplanung-klausur v-for="klausur of klausurenImRaum" :key="klausur.id"
						:klausur="klausur"
						:termin="kursklausurmanager().getKlausurterminById(raum.idTermin)"
						:kursklausurmanager="kursklausurmanager"
						:kursmanager="kursmanager"
						:map-lehrer="mapLehrer"
						:patch-klausur-uhrzeit="patchKlausurUhrzeit" />
				</table>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</svws-ui-drop-data>
</template>

<script setup lang="ts">
	import type { StundenplanRaum, StundenplanManager, GostKlausurraumManager, GostKursklausur, GostKlausurenCollectionSkrsKrs, GostSchuelerklausur, List, GostFaecherManager, GostKursklausurManager, KursManager, LehrerListeEintrag } from '@core';
	import type { GostKlausurraum } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		stundenplanmanager: StundenplanManager;
		raum: GostKlausurraum;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		raummanager: GostKlausurraumManager;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
		setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum, sks: List<GostSchuelerklausur>, manager: GostKlausurraumManager) => Promise<GostKlausurenCollectionSkrsKrs>;
		patchKlausurUhrzeit: (klausur: Partial<GostKursklausur> | Partial<GostSchuelerklausur>) => Promise<boolean>;
	}>();

	const setKlausurToRaum = async (klausur : GostKursklausur) => {
		const collectionSkrsKrs = await props.setzeRaumZuSchuelerklausuren(props.raum, props.raummanager.getSchuelerklausurenByKursklausur(klausur.id), props.raummanager);
	};

	const klausurenImRaum = computed(() => props.raummanager.getKursklausurenInRaum(props.raum.id, props.kursklausurmanager()));
	const anzahlSuS = computed(() => props.raummanager.getSchuelerklausurenInRaum(props.raum.id, props.kursklausurmanager()).size());

	const getStundenplanraum = () => props.raum.idStundenplanRaum !== null ? props.stundenplanmanager.raumGetByIdOrException(props.raum.idStundenplanRaum) : null;

	const stundenplanRaumSelected = computed({
		get: () : StundenplanRaum | undefined => props.raum.idStundenplanRaum === null ? undefined : props.stundenplanmanager.raumGetByIdOrException(props.raum.idStundenplanRaum),
		set: (value: StundenplanRaum | undefined): void => {
			props.raum.idStundenplanRaum = value === undefined ? null : value.id;
		}
	});

</script>
