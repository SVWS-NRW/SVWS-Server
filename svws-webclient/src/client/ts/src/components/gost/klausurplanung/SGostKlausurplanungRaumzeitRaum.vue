<template>
	<div @dragover="checkDropZone($event)" @drop="onDrop(raum)" class="border">
		<svws-ui-content-card title="Kausurraum">
			<template #actions>
				<svws-ui-button type="danger" size="small" @click="loescheKlausurraum(raum.id, raummanager)"><i-ri-delete-bin-line /></svws-ui-button>
			</template>
			<svws-ui-multi-select title="Raum auswählen"
				v-model="stundenplanRaumSelected"
				@update:model-value="patchKlausurraum(raum.id, { idStundenplanRaum: stundenplanRaumSelected?.id }, raummanager)"
				:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
				:items="raummanager.stundenplanraumVerfuegbarGetMenge(stundenplanmanager.raumGetMengeAsList())" />
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
						:draggable="true"
						@dragstart="onDrag(klausur)"
						@dragend="onDrag(undefined)"
						:termin="kursklausurmanager().terminGetByIdOrException(raum.idTermin)"
						:kursklausurmanager="kursklausurmanager"
						:kursmanager="kursmanager"
						:map-lehrer="mapLehrer"
						:patch-klausur-uhrzeit="patchKlausurUhrzeit" />
				</table>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { type StundenplanRaum, type StundenplanManager, type GostKlausurraumManager, type GostKursklausur, type GostKlausurenCollectionSkrsKrs, type GostSchuelerklausur, type List, type GostFaecherManager, type GostKursklausurManager, type KursManager, type LehrerListeEintrag, StundenplanKlassenunterricht, StundenplanKurs } from '@core';
	import type { GostKlausurraum } from '@core';
	import { computed, ref } from 'vue';
	import type { GostKlausurplanungRaumzeitDragData, GostKlausurplanungRaumzeitDropZone } from './SGostKlausurplanungRaumzeitProps';

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
		patchKlausurUhrzeit: (klausur: Partial<GostKursklausur | GostSchuelerklausur>) => Promise<boolean>;
		dragData: () => GostKlausurplanungRaumzeitDragData;
		onDrag: (data: GostKlausurplanungRaumzeitDragData) => void;
		onDrop: (zone: GostKlausurplanungRaumzeitDropZone) => void;
	}>();

	const klausurenImRaum = computed(() => props.raummanager.kursklausurGetMengeByRaumid(props.raum.id, props.kursklausurmanager()));
	const anzahlSuS = computed(() => props.raummanager.kursklausurGetMengeByRaumid(props.raum.id, props.kursklausurmanager()).size());

	const stundenplanRaumSelected = computed({
		get: () : StundenplanRaum | undefined => props.raum.idStundenplanRaum === null ? undefined : props.stundenplanmanager.raumGetByIdOrException(props.raum.idStundenplanRaum),
		set: (value: StundenplanRaum | undefined): void => {
			props.raum.idStundenplanRaum = value === undefined ? null : value.id;
		}
	});

	function isDropZone() : boolean {
		if ((props.dragData() === undefined) || (props.dragData() instanceof StundenplanKurs) || (props.dragData() instanceof StundenplanKlassenunterricht))
			return false;
		return true;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

</script>
