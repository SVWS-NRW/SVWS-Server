<template>
	<svws-ui-content-card title="Daten zur Klausur">
		<table class="border">
			<tr>
				<th>Datum</th>
				<td>{{ termin.datum }}</td>
			</tr>
			<tr>
				<th>Startzeit</th>
				<td>{{ DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit!) }} Uhr</td>
			</tr>

			<tr>
				<th>Anzahl Klausurschreiber:innen</th>
				<td>{{ kursklausurmanager().schueleridsGetMengeByTerminid(termin.id)?.size() }}</td>
			</tr>
		</table>
	</svws-ui-content-card>

	<svws-ui-content-card class="f-full" title="Klausurräume">
		<template #actions>
			<svws-ui-button size="small" type="secondary" @click="erzeugeNeuenRaum()">Erstelle Klausurraum <i-ri-add-circle-line class="-mr-1" /></svws-ui-button>
		</template>
		<div class="flex flex-col flex-wrap gap-4 w-full">
			<s-gost-klausurplanung-raumzeit-raum v-for="raum in raummanager?.raumGetMengeAsList()"
				:key="raum.id"
				:stundenplanmanager="stundenplanmanager"
				:raum="raum"
				:raummanager="(raummanager as GostKlausurraumManager)"
				:patch-klausurraum="patchKlausurraum"
				:loesche-klausurraum="loescheKlausurraum"
				:patch-klausur-uhrzeit="patchKlausurUhrzeit"
				:faecher-manager="faecherManager"
				:kursklausurmanager="kursklausurmanager"
				:kursmanager="kursmanager"
				:map-lehrer="mapLehrer"
				:drag-data="() => dragData"
				:on-drag="onDrag"
				:on-drop="onDrop" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager, StundenplanManager, GostKlausurraumManager, GostKursklausur, GostSchuelerklausur } from '@core';
	import { GostKlausurraum, DateUtils} from '@core';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		stundenplanmanager: StundenplanManager;
		raummanager: GostKlausurraumManager;
		erzeugeKlausurraum: (raum: GostKlausurraum) => Promise<GostKlausurraum>;
		loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausurUhrzeit: (klausur: Partial<GostKursklausur | GostSchuelerklausur>) => Promise<boolean>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;

	}>();

	const erzeugeNeuenRaum = async () => {
		let nR = new GostKlausurraum();
		nR.idTermin = props.termin.id;
		nR = await props.erzeugeKlausurraum(nR);
		props.raummanager.raumAdd(nR);
	}

</script>
