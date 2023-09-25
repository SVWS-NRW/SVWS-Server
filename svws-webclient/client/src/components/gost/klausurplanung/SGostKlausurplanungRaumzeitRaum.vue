<template>
	<div @dragover="checkDropZone($event)" @drop="onDrop(raum)" class="flex flex-col rounded-xl border border-black/10 bg-white shadow-lg shadow-black/5 dark:border-white/10 dark:bg-black">
		<div class="flex h-full flex-col p-3">
			<div class="svws-raum-title flex justify-between">
				<svws-ui-select :title="stundenplanRaumSelected ? 'Raum' : 'Raum auswählen...'"
					v-model="stundenplanRaumSelected"
					headless
					class="flex-grow"
					@update:model-value="patchKlausurraum(raum.id, { idStundenplanRaum: stundenplanRaumSelected?.id }, raummanager())"
					:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
					:items="raummanager().stundenplanraumVerfuegbarGetMenge(stundenplanmanager.raumGetMengeAsList())" />
				<span v-if="raum.idStundenplanRaum && anzahlSuS() > stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse" class="inline-flex items-center flex-shrink-0 text-error font-bold text-headline-md -my-1">
					<i-ri-alert-fill />
				</span>
			</div>
			<svws-ui-table :items="[]" :columns="cols" :no-data="klausurenImRaum().size() === 0" no-data-text="Noch keine Klausuren zugewiesen." class="mt-6">
				<template #header><span /></template>
				<template #body>
					<div v-for="klausur of klausurenImRaum()" :key="klausur.id" class="svws-ui-tr" role="row" :data="klausur" :draggable="true" @dragstart="onDrag(klausur)"	@dragend="onDrag(undefined)">
						<div class="svws-ui-td" role="cell">
							<i-ri-draggable class="i-ri-draggable -m-0.5 -ml-3" />
							<span class="svws-ui-badge" :style="`--background-color: ${getBgColor(props.kursmanager.get(klausur.idKurs)!.kuerzel.split('-')[0])};`">{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</span>
						</div>
						<div class="svws-ui-td" role="cell">{{ mapLehrer.get(props.kursmanager.get(klausur.idKurs)!.lehrer!)?.kuerzel }}</div>
						<div class="svws-ui-td" role="cell">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)!.schueler.size() }}</div>
						<div class="svws-ui-td" role="cell">{{ klausur.dauer }}</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-text-input :model-value="klausur.startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(klausur.startzeit) : ''" headless :placeholder="klausur.startzeit === null ? (terminStartzeit + ' Uhr' || 'Startzeit') : 'Individuelle Startzeit'" @change="zeit => patchKlausurbeginn(zeit, klausur.id)" />
						</div>
					</div>
				</template>
			</svws-ui-table>
			<span class="mt-auto -mb-3 flex w-full items-center justify-between gap-1 text-sm">
				<div class="py-3" :class="{'opacity-50': klausurenImRaum().size() === 0}">
					<span class="font-bold">
						<span v-if="raum.idStundenplanRaum !== null" :class="anzahlSuS() > stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse ? 'text-error' : ''">{{ anzahlSuS() }}/{{ stundenplanmanager.raumGetByIdOrException(raum.idStundenplanRaum).groesse }} belegt, </span>
						<span v-else>{{ anzahlSuS() }} Plätze, </span>
					</span>
					<span>{{ anzahlRaumstunden }} Raumstunden benötigt</span>
				</div>
				<svws-ui-button type="icon" size="small" class="-mr-1" @click="loescheKlausurraum(raum.id, raummanager())"><i-ri-delete-bin-line /></svws-ui-button>
			</span>
		</div>
	</div>
</template>

<script setup lang="ts">
	import {
		type StundenplanRaum,
		type StundenplanManager,
		type GostKlausurraumManager,
		GostKursklausur,
		type GostSchuelerklausur,
		type GostFaecherManager,
		type GostKursklausurManager,
		type KursManager,
		type LehrerListeEintrag,
		StundenplanKlassenunterricht,
		StundenplanKurs,
		ZulaessigesFach
	} from '@core';
	import type { GostKlausurenCollectionSkrsKrs, GostKlausurraum } from '@core';
	import { computed } from 'vue';
	import { DateUtils} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import type {
		DataTableColumn
	} from "@ui";

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
		patchKlausur: (id: number, klausur: Partial<GostKursklausur | GostSchuelerklausur>) => Promise<GostKlausurenCollectionSkrsKrs>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		terminStartzeit?: string;
	}>();

	const klausurenImRaum = () => props.raummanager().kursklausurGetMengeByRaumid(props.raum.id, props.kursklausurmanager());

	const anzahlSuS = () => props.raummanager().schuelerklausurGetMengeByRaumid(props.raum.id, props.kursklausurmanager()).size();

	const anzahlRaumstunden = computed(() => {
		return props.raummanager().klausurraumstundeGetMengeByRaumid(props.raum.id).size();
	});

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
			const result = await props.patchKlausur(id, {id, startzeit});
			props.raummanager().setzeRaumZuSchuelerklausuren(result);
		} catch(e) {
			// Do nothing
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "kurs", label: "Kurs" },
		{ key: "kuerzel", label: "Lehrkraft", span: 0.75 },
		{ key: "schriftlich", label: "Schriftlich", span: 0.5, minWidth: 4.75 },
		{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.25, minWidth: 4 },
		{ key: "startzeit", label: "Startzeit", span: 1.25, minWidth: 4 },
	];

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0); // TODO: Fachkuerzel für Kursklausur

</script>

<style lang="postcss">
.svws-raum-title .text-input--headless {
  @apply text-headline-md;

  &::placeholder {
    @apply font-bold;
  }
}
</style>
