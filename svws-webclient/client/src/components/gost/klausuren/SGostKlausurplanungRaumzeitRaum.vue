<template>
	<s-gost-klausurplanung-card class="min-w-120 max-w-120 h-fit"
		:drop-state
		:drop-allowed="isDropZone()"
		:danger="raumHatFehler()"
		:warning="raum.idTermin !== terminSelected.id"
		@drop="onDrop(raum)">
		<div class="flex h-full flex-col p-3">
			<div class="svws-raum-title flex justify-between">
				<svws-ui-select :title="raum.idStundenplanRaum ? 'Raum' : 'Raum auswählen...'"
					:model-value="raum.idStundenplanRaum === null ? undefined : state.manager.stundenplanraumGetByKlausurraum(raum)"
					:disabled="!hatKompetenzUpdate"
					headless
					no-items-text="Keine Räume im Stundenplan gefunden"
					class="grow"
					@update:model-value="(value) => void state.patchKlausurraum(raum.id, { idStundenplanRaum: ((value !== undefined) && (value !== null)) ? value.id : null })"
					:item-text="(item) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
					:items="raeumeVerfuegbar" />
				<span class="inline-flex items-center shrink-0">
					<svws-ui-tooltip class="text-ui-danger font-bold text-headline-md" v-if="raumHatFehler()">
						<template #content>
							<template v-if="!raum.idStundenplanRaum">Keine Raumnummer zugeordnet</template>
							<template v-else-if="anzahlSuS() > state.manager.stundenplanraumGetByKlausurraum(raum).groesse">Derzeitige Raumbelegung überschreitet die Raumkapazität</template>
						</template>
						<span class="icon icon-ui-danger i-ri-alert-fill" />
					</svws-ui-tooltip>
					<template v-if="multijahrgang()">
						<span class="border rounded-md p-1 text-button" v-if="raum.idTermin === terminSelected.id">{{ GostHalbjahr.fromIDorException(termin().halbjahr).jahrgang }}</span>
						<svws-ui-button v-else type="secondary" class="p-1" @click="gotoTermin(termin().abiturjahrgang, GostHalbjahr.fromIDorException(termin().halbjahr), termin().id)" :title="`Zur Raumplanung des Jahrgangs`" size="small">{{ GostHalbjahr.fromIDorException(termin().halbjahr).jahrgang }}</svws-ui-button>
					</template>
				</span>
			</div>
			<svws-ui-table :items="[]" :columns="cols" :no-data="(klausurenImRaum().size() + nachschreiberImRaum().size()) === 0" no-data-text="Noch keine Klausuren zugewiesen." class="mt-4">
				<template #header><span /></template>
				<template #body>
					<s-gost-klausurplanung-kursklausur-zeile v-for="klausur of klausurenImRaum()" :key="klausur.id"
						:klausur
						:badge-termin="state.manager.terminOrNullByKursklausur(klausur)!"
						:row-style="raumTableRowStyle"
						:draggable="hatKompetenzUpdate"
						:on-drag>
						<template #schreiber>
							<div>
								<span v-if="state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() !== state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur)" class="font-bold">{{ state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() }}/</span>
								<span :class="state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() !== state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur) ? 'line-through' : ''">{{ state.manager.kursAnzahlKlausurschreiberByKursklausur(klausur) }}/</span>
								<span>{{ state.manager.kursAnzahlSchuelerGesamtByKursklausur(klausur) }}</span>
							</div>
						</template>
						<template #zusatz>
							<div class="svws-ui-td">
								<svws-ui-text-input :model-value="klausur.startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(klausur.startzeit) : ''" headless :placeholder="klausurStartzeit(klausur) + ' Uhr'" @change="zeit => patchKlausurbeginn(zeit, klausur, false)" />
							</div>
						</template>
					</s-gost-klausurplanung-kursklausur-zeile>
					<div v-if="(klausurenImRaum().size() > 0) && (nachschreiberImRaum().size() > 0)">Nachschreiber:</div>
					<s-gost-klausurplanung-kursklausur-zeile v-for="klausur of nachschreiberImRaum()" :key="klausur.id"
						:klausur
						:badge-termin="state.manager.terminOrNullByKursklausur(klausur)!"
						:row-style="raumTableRowStyle"
						:draggable="hatKompetenzUpdate"
						:on-drag>
						<template #schreiber>
							{{ state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() }}
						</template>
						<template #zusatz>
							<div class="svws-ui-td">
								<svws-ui-text-input :model-value="state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).getFirst().startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).getFirst().startzeit!) : ''" headless :placeholder="DateUtils.getStringOfUhrzeitFromMinuten(termin().startzeit!) + ' Uhr'" @change="zeit => patchKlausurbeginn(zeit, klausur, true)" />
							</div>
						</template>
					</s-gost-klausurplanung-kursklausur-zeile>
				</template>
			</svws-ui-table>
			<div class="mt-3">
				<svws-ui-textarea-input class="text-sm" :headless="(raum.bemerkung === null) || (raum.bemerkung.trim().length === 0)" :rows="1" resizeable="none" autoresize placeholder="Bemerkungen zum Raum" :disabled="!hatKompetenzUpdate" :model-value="raum.bemerkung" @change="bemerkung => state.patchKlausurraum(raum.id, {bemerkung})" @drop.prevent
					@dragover.prevent />
			</div>
			<span class="mt-auto -mb-3 flex w-full items-center justify-between gap-1 text-sm">
				<div class="py-3" :class="{'opacity-50': klausurenImRaum().size() === 0}">
					<span class="font-bold">
						<span v-if="raum.idStundenplanRaum !== null" :class="anzahlSuS() > state.manager.stundenplanraumGetByKlausurraum(raum).groesse ? 'text-ui-danger' : ''">{{ anzahlSuS() }}/{{ state.manager.stundenplanraumGetByKlausurraum(raum).groesse }} belegt, </span>
						<span v-else>{{ anzahlSuS() }} Plätze, </span>
					</span>
					<span>{{ anzahlRaumstunden }} Raumstunden benötigt</span>
				</div>
				<svws-ui-button type="trash" :disabled="!hatKompetenzUpdate" size="small" @click="state.loescheKlausurraum(raum.id)" />
			</span>
		</div>
	</s-gost-klausurplanung-card>
</template>


<script setup lang="ts">

	import type { GostKlausurraum, GostKlausurtermin } from '@core';
	import { BenutzerKompetenz, DateUtils, GostHalbjahr, GostKursklausur } from '@core';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import { useBenutzerState, useGostKlausurplanungState, type DataTableColumn } from "@ui";
	import { computed } from 'vue';
	import { klausurplanungDropState } from "./SGostKlausurplanungDragUtils";

	const props = defineProps<{
		raum: GostKlausurraum;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (event: DragEvent, data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		multijahrgang: () => boolean;
		terminSelected: GostKlausurtermin;
		gotoTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
		// terminStartzeit?: string;
	}>();

	const benutzerState = useBenutzerState();
	const state = useGostKlausurplanungState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const raumHatFehler = () => ((props.raum.idStundenplanRaum !== null) && (anzahlSuS() > state.manager.stundenplanraumGetByKlausurraum(props.raum).groesse)) || (props.raum.idStundenplanRaum === null);

	const klausurenImRaum = () => state.manager.kursklausurGetMengeByRaum(props.raum, false);
	const nachschreiberImRaum = () => state.manager.nachschreiberGetMengeByRaum(props.raum);

	const anzahlSuS = () => state.manager.schuelerklausurterminGetMengeByRaum(props.raum).size();

	const termin = () => state.manager.terminGetByIdOrException(props.raum.idTermin);

	const klausurStartzeit = (klausur: GostKursklausur) => {
		const startzeit = state.manager.startzeitByKlausurraumAndKursklausurOrNull(props.raum, klausur);
		return startzeit === null ? undefined : DateUtils.getStringOfUhrzeitFromMinuten(startzeit);
	};

	const anzahlRaumstunden = computed(() => {
		return state.manager.raumstundeGetMengeByRaum(props.raum).size();
	});

	const raeumeVerfuegbar = computed(() => {
		const raeume = state.manager.stundenplanraumVerfuegbarGetMengeByTermin(termin(), props.multijahrgang());
		if (props.raum.idStundenplanRaum !== null) {
			raeume.add(0, state.manager.stundenplanraumGetByKlausurraum(props.raum));
		}
		return raeume;
	});

	function isDropZone(): boolean {
		if ((props.dragData() === undefined) || ((props.dragData() instanceof GostKursklausur) && state.manager.containsKlausurraumKursklausur(props.raum, props.dragData() as GostKursklausur))) {
			return false;
		}
		return true;
	}

	const dropState = computed(() => klausurplanungDropState({
		hasDragData: props.dragData() !== undefined,
		canDrop: isDropZone(),
	}));

	async function patchKlausurbeginn(event: string | null, klausur: GostKursklausur, nk: boolean) {
		if (event === null) {
			return;
		}
		const startzeit = event.trim().length > 0 ? DateUtils.gibMinutenOfZeitAsString(event) : null;
		if (nk === true) {
			const nachschreiberSkts = state.manager.schuelerklausurterminGetMengeByRaumAndKursklausur(props.raum, klausur);
			for (const nachSkt of nachschreiberSkts) {
				await state.patchKlausur(nachSkt, { startzeit });
			}
		} else {
			await state.patchKlausur(klausur, { startzeit });
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "dragHandle", label: " ", fixedWidth: 1 },
		{ key: "jgst", label: "Jgst.", fixedWidth: 2 },
		{ key: "kurs", label: "Kurs", span: 1.5 },
		{ key: "kuerzel", label: "Lehrkraft", span: 0.75 },
		{ key: "schriftlich", label: "Schriftlich", span: 0.5, minWidth: 4.75 },
		{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.25, minWidth: 4 },
		{ key: "startzeit", label: "Startzeit", span: 1.25, minWidth: 4 },
	];

	const raumTableRowStyle = "grid-template-columns: 1.5rem 2rem minmax(4rem, 1.5fr) minmax(4rem, 0.75fr) minmax(4.75rem, 0.5fr) minmax(4rem, 0.25fr) minmax(4rem, 1.25fr);";

</script>
