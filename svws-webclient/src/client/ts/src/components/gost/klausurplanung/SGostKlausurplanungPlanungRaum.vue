<template>
	<svws-ui-drop-data @drop="setKlausurToRaum">
		<svws-ui-content-card title="Kausurraum">
			<svws-ui-multi-select title="Raum auswählen"
				v-model="stundenplanRaumSelected"
				@update:model-value="patchKlausurraum(raum.id, { idStundenplanRaum: stundenplanRaumSelected?.id }, raummanager)"
				:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
				:items="stundenplanmanager.getListRaum()" />
		</svws-ui-content-card>
		<svws-ui-content-card title="Klausuren im Raum">
			<table>
				<s-gost-klausurplanung-klausur v-for="klausur of klausurenImRaum" :key="klausur.id"
					:klausur="klausur"
					:kursklausurmanager="kursklausurmanager"
					:kursmanager="kursmanager"
					:map-lehrer="mapLehrer" />
			</table>
		</svws-ui-content-card>
	</svws-ui-drop-data>
</template>

<script setup lang="ts">
	import type { StundenplanRaum, StundenplanManager, GostKlausurraumManager, GostKursklausur, GostKlausurenCollectionSkrsKrs, GostSchuelerklausur, List, GostFaecherManager, GostKursklausurManager, KursManager, LehrerListeEintrag } from '@core';
	import type { GostKlausurraum } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		stundenplanmanager: StundenplanManager;
		raum: GostKlausurraum;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		raummanager: GostKlausurraumManager;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		setzeRaumZuSchuelerklausuren: (raum: GostKlausurraum, sks: List<GostSchuelerklausur>, manager: GostKlausurraumManager) => Promise<GostKlausurenCollectionSkrsKrs>;
	}>();

	const setKlausurToRaum = async (klausur : GostKursklausur) => {
		console.log(props.raummanager.getSchuelerklausurenByKursklausur(klausur.id));
		const collectionSkrsKrs = await props.setzeRaumZuSchuelerklausuren(props.raum, props.raummanager.getSchuelerklausurenByKursklausur(klausur.id), props.raummanager);
		console.log(collectionSkrsKrs);
		console.log(props.raummanager);
	};

	const klausurenImRaum = props.raummanager.getKursklausurenInRaum(props.raum.id, props.kursklausurmanager());

	const stundenplanRaumSelected = ref<StundenplanRaum | undefined>(props.raum.idStundenplanRaum === null ? undefined : props.stundenplanmanager.getRaum(props.raum.idStundenplanRaum));
	const getStundenplanraum = () => props.raum.idStundenplanRaum !== null ? props.stundenplanmanager.getRaum(props.raum.idStundenplanRaum) : null;

</script>
