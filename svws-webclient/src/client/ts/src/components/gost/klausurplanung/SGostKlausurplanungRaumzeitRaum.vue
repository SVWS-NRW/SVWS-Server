<template>
	<div @dragover="checkDropZone($event)" @drop="onDrop(raum)" class="border">
		<svws-ui-content-card title="Kausurraum">
			<template #actions>
				<svws-ui-button type="danger" size="small" @click="loescheKlausurraum(raum.id, raummanager())"><i-ri-delete-bin-line /></svws-ui-button>
			</template>
			<svws-ui-multi-select title="Raum auswählen"
				v-model="stundenplanRaumSelected"
				@update:model-value="patchKlausurraum(raum.id, { idStundenplanRaum: stundenplanRaumSelected?.id }, raummanager())"
				:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
				:items="raummanager().stundenplanraumVerfuegbarGetMenge(stundenplanmanager.raumGetMengeAsList())" />
			<div>
				Belegung:
				<span v-if="raum.idStundenplanRaum !== null" :class="anzahlSuS() > stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse ? 'text-red-700' : 'text-green-600'">{{ anzahlSuS() }} / {{ stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse }}</span>
				<span v-else>{{ anzahlSuS() }}</span>
			</div>
			<svws-ui-content-card title="Klausuren im Raum">
				<table>
					<div v-if="klausurenImRaum().size() === 0">Keine Klausuren im Raum</div>
					<tr v-for="klausur of klausurenImRaum()" :key="klausur.id" :data="klausur" :draggable="true" @dragstart="onDrag(klausur)"	@dragend="onDrag(undefined)">
						<td>{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</td>
						<td>{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</td>
						<td class="text-center">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</td>
						<td class="text-center">{{ klausur.dauer }}</td>
						<td>&nbsp;</td>
						<td>
							<svws-ui-text-input :model-value="klausur.startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(klausur.startzeit) : ''" :placeholder="klausur.startzeit === null ? 'Startzeit wie Termin' : 'Individuelle Startzeit'" @change="zeit => patchKlausurbeginn(zeit, klausur.id)" />
						</td>
					</tr>
				</table>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { type StundenplanRaum, type StundenplanManager, type GostKlausurraumManager, GostKursklausur, type GostSchuelerklausur, type GostFaecherManager, type GostKursklausurManager, type KursManager, type LehrerListeEintrag, StundenplanKlassenunterricht, StundenplanKurs } from '@core';
	import type { GostKlausurraum } from '@core';
	import { computed } from 'vue';
	import { DateUtils} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';

	const props = defineProps<{
		stundenplanmanager: StundenplanManager;
		raum: GostKlausurraum;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		raummanager: () => GostKlausurraumManager;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager) => Promise<boolean>;
		loescheKlausurraum: (id: number, manager: GostKlausurraumManager) => Promise<boolean>;
		patchKlausur: (id: number, klausur: Partial<GostKursklausur | GostSchuelerklausur>) => Promise<void>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
	}>();

	const klausurenImRaum = () => props.raummanager().kursklausurGetMengeByRaumid(props.raum.id, props.kursklausurmanager());

	const anzahlSuS = () => props.raummanager().schuelerklausurGetMengeByRaumid(props.raum.id, props.kursklausurmanager()).size();

	const stundenplanRaumSelected = computed({
		get: () : StundenplanRaum | undefined => props.raum.idStundenplanRaum === null ? undefined : props.stundenplanmanager.raumGetByIdOrException(props.raum.idStundenplanRaum),
		set: (value: StundenplanRaum | undefined): void => {
			props.raum.idStundenplanRaum = value === undefined ? null : value.id;
		}
	});

	function isDropZone() : boolean {
		if ((props.dragData() === undefined) || (props.dragData() instanceof GostKursklausur) && props.raummanager().containsKlausurraumKursklausur(props.raum.id, props.dragData()!.id))
			return false;
		return true;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	async function patchKlausurbeginn(event: string | number, id: number) {
		if (typeof event === 'number')
			return;
		try {
			const startzeit = event.trim().length > 0 ? DateUtils.gibMinutenOfZeitAsString(event) : null;
			await props.patchKlausur!(id, {id, startzeit});
		} catch(e) {
			// Do nothing
		}
	}

</script>
