<template>
	<div class="content">
		<svws-ui-table :columns="cols" :items="undefined" has-background class="col-span-2 -mt-1">
			<template #header>
				<div role="row" class="svws-ui-tr">
					<div role="columnheader" class="col-span-5" aria-label="Fach" />
					<div role="columnheader" class="svws-ui-td svws-align-center col-span-2" aria-label="Noten"> Noten </div>
				</div>
				<div role="row" class="svws-ui-tr">
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="leistungen.size() === auswahl.size" :indeterminate="someSelected()" @update:model-value="updateAuswahl" headless />
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Fach"> Fach </div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Kurs"> Kurs </div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Kursart"> Kursart </div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Lehrer"> Lehrer </div>
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Quartalsnote"> Quartal </div>
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Halbjahresnote"> Halbjahr </div>
				</div>
			</template>
			<template #body="">
				<template v-for="leistung in leistungen" :key="leistung.id">
					<div v-if="leistung.id !== null" class="svws-ui-tr" role="row" :style="{ '--background-color': manager().fachFarbeGetByLeistungsIdOrException(leistung.id) }">
						<div class="svws-ui-td svws-align-center cursor-pointer" role="cell">
							<svws-ui-checkbox :model-value="auswahl.has(leistung)" @update:model-value="auswahl.has(leistung) ? auswahl.delete(leistung) : auswahl.add(leistung)" headless />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="props.manager().fachGetMenge()" :item-text="fach => ((fach === null) || (fach.bezeichnung === null)) ? '—' : fach.bezeichnung"
								:model-value="manager().fachGetByLeistungIdOrException(leistung.id)"
								@update:model-value="value => patchLeistung({ fachID: ((value === null) || (value === undefined)) ? -1 : value.id, kursID: null }, leistung.id)"
								class="w-full" headless />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="manager().kursGetMengeFilteredByLeistung(leistung.id)" :item-text="kurs => (kurs === null) ? '—' : kurs.kuerzel"
								:model-value="manager().kursGetByLeistungIdOrNull(leistung.id)"
								@update:model-value="value => patchLeistung(((value === null) || (value === undefined)) ? { kursID: null } : { kursID: value.id, lehrerID: value.lehrer }, leistung.id)"
								class="w-full" headless removable />
						</div>
						<div class="svws-ui-td" role="cell">
							<!-- TODO In Gesamtschulen kann bei Klassenunterricht neben PUK noch E oder G als Kursart vorkommen -->
							<template v-if="(manager().kursGetByLeistungIdOrNull(leistung.id) === null) || ZulaessigeKursart.getByAllgemeinerKursart(manager().kursGetByLeistungIdOrNull(leistung.id)!.kursartAllg).size() === 1">
								<span>{{ leistung.kursart }}</span>
							</template>
							<template v-else>
								<svws-ui-select title="—" :items="ZulaessigeKursart.getByAllgemeinerKursart(manager().kursGetByLeistungIdOrNull(leistung.id)!.kursartAllg)" :item-text="zk => zk.daten.kuerzel"
									:model-value="ZulaessigeKursart.getByASDKursart(leistung.kursart)"
									@update:model-value="value => patchLeistung({ kursart: ((value === null) || (value === undefined)) ? null : value.daten.kuerzel }, leistung.id)"
									class="w-full" headless />
							</template>
						</div>
						<div class="svws-ui-td svws-divider" role="cell">
							<svws-ui-select title="—" :items="manager().lehrerGetMenge()" :item-text="lehrer => (lehrer === null) ? '—' : lehrer.kuerzel + ' (' + lehrer.nachname + ', ' + lehrer.vorname + ')'"
								:model-value="manager().lehrerGetByLeistungIdOrNull(leistung.id)"
								@update:model-value="value => patchLeistung({ lehrerID: ((value === null) || (value === undefined)) ? null : value.id }, leistung.id)"
								class="w-full" headless />
						</div>
						<div class="svws-ui-td svws-divider" role="cell">
							<svws-ui-select title="—" :items="Note.values()" :item-text="(item: Note) => item?.kuerzel"
								:model-value="Note.fromKuerzel(leistung.noteQuartal)"
								@update:model-value="value => patchLeistung({ noteQuartal: ((value === null) || (value === undefined)) ? null : value.kuerzel }, leistung.id)"
								headless class="w-full" />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="Note.values()" :item-text="(item: Note) => item?.kuerzel"
								:model-value="Note.fromKuerzel(leistung.note)"
								@update:model-value="value => patchLeistung({ note: ((value === null) || (value === undefined)) ? null : value.kuerzel }, leistung.id)"
								headless class="w-full" />
						</div>
					</div>
				</template>
			</template>
			<template #footer>
				<div class="svws-ui-tr flex flex-row" role="row">
					<div class="svws-ui-td" role="cell" v-if="auswahl.size > 0">
						{{ auswahl.size }} ausgewählt
					</div>
					<div class="svws-ui-td flex-grow justify-end" role="cell">
						<svws-ui-button @click="deleteAuswahl()" type="trash" :disabled="auswahl.size === 0" />
						<svws-ui-button v-if="props.manager().fachGetMenge().size() > 0" @click="addLeistung(props.manager().fachGetMenge().get(0).id)" type="icon" title="Raum hinzufügen"> <i-ri-add-line /> </svws-ui-button>
					</div>
				</div>
			</template>
		</svws-ui-table>
		<svws-ui-spacing :size="2" />
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="2">
				<span class="font-bold col-span-full">Lernbereichsnoten</span>
				<svws-ui-select title="Gesellschaftswissenschaft" :items="getLernbereichsnoten()" :item-text="i => `${i.kuerzel}`" autocomplete
					v-model="lernbereichsnoteGSbzwAL" />
				<svws-ui-select title="Naturwissenschaft" :items="getLernbereichsnoten()" :item-text="i => `${i.kuerzel}`" autocomplete
					v-model="lernbereichsnoteNW" />
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" />
			<svws-ui-input-wrapper class="col-span-full items-center" :grid="4">
				<span class="font-bold col-span-full">Fehlstunden (Summe)</span>
				<svws-ui-input-number placeholder="Maximal" :min="0"
					:model-value="manager().lernabschnittGet().fehlstundenGrenzwert"
					@change="fehlstundenGrenzwert => patch({ fehlstundenGrenzwert })" />
				<svws-ui-input-number placeholder="Gesamt" :min="0"
					:model-value="manager().lernabschnittGet().fehlstundenGesamt"
					@change="fehlstundenGesamt => patch({ fehlstundenGesamt: fehlstundenGesamt ?? undefined })" />
				<svws-ui-input-number placeholder="Unentschuldigt" :min="0"
					:model-value="manager().lernabschnittGet().fehlstundenUnentschuldigt"
					@change="fehlstundenUnentschuldigt => patch({ fehlstundenUnentschuldigt: fehlstundenUnentschuldigt ?? undefined })" />
			</svws-ui-input-wrapper>
			<!-- <svws-ui-todo title="Fehlzeiten" class="mt-10">
				Hier könnte demnächst die Übersicht über die Fehlzeiten implementiert werden.
			</svws-ui-todo> -->
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { Note, ZulaessigeKursart, type SchuelerLeistungsdaten, type List, ArrayList } from "@core";
	import type { SchuelerLernabschnittLeistungenProps } from "./SSchuelerLernabschnittLeistungenProps";

	const props = defineProps<SchuelerLernabschnittLeistungenProps>();

	const cols = [
		{ key: "auswahl", label: "Auswahl", fixedWidth: 1.5 },
		{ key: "fachID", label: "Fach", span: 3, sortable: false, minWidth: 10 },
		{ key: "kursID", label: "Kurs", span: 1, sortable: false, minWidth: 10 },
		{ key: "kursart", label: "Kursart", span: 1, sortable: false, fixedWidth: 6 },
		{ key: "lehrerID", label: "Lehrer", span: 2, sortable: false, minWidth: 10 },
		{ key: "noteQuartal", label: "Quartalsnote", tooltip: "Quartalsnote", sortable: false, fixedWidth: 5 },
		{ key: "note", label: "Note", sortable: false, fixedWidth: 5 },
	];

	const leistungen = computed<List<SchuelerLeistungsdaten>>(() => {
		return props.manager().leistungGetMengeAsListSortedByFach();
	});

	watch(leistungen, (newLeistungen, oldLeistungen) => {
		if (newLeistungen.size() === oldLeistungen.size()) {
			const tmpSetIDs = new Set<number>();
			for (const l of oldLeistungen)
				tmpSetIDs.add(l.id);
			let changed : boolean = false;
			for (const l of newLeistungen) {
				if (!tmpSetIDs.has(l.id)) {
					changed = true;
					break;
				}
			}
			if (!changed)
				return;
		}
		auswahl.value.clear();
	});

	const auswahl = ref<Set<SchuelerLeistungsdaten>>(new Set());

	function updateAuswahl() {
		const allSelected = (leistungen.value.size() === auswahl.value.size);
		if (allSelected) {
			auswahl.value.clear();
		} else {
			for (const leistung of leistungen.value)
				auswahl.value.add(leistung);
		}
	}

	function someSelected() : boolean {
		return (auswahl.value.size > 0) && (auswahl.value.size < leistungen.value.size());
	}

	const deleteAuswahl = async () => {
		if (auswahl.value.size === 0)
			return;
		const leistungenIDs = new ArrayList<number>();
		for (const leistung of auswahl.value)
			leistungenIDs.add(leistung.id);
		await props.deleteLeistungen(leistungenIDs);
	};

	function getLernbereichsnoten() : Note[] {
		return [ Note.KEINE, Note.SEHR_GUT, Note.GUT, Note.BEFRIEDIGEND, Note.AUSREICHEND, Note.MANGELHAFT, Note.UNGENUEGEND ];
	}

	const lernbereichsnoteGSbzwAL = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(props.manager().lernabschnittGet().noteLernbereichGSbzwAL);
			return note === null ? undefined : note;
		},
		set: (value) => void props.patch({ noteLernbereichGSbzwAL: value === undefined || value === Note.KEINE ? null : value.getNoteSekI() })
	});

	const lernbereichsnoteNW = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(props.manager().lernabschnittGet().noteLernbereichNW);
			return note === null ? undefined : note;
		},
		set: (value) => void props.patch({ noteLernbereichNW: value === undefined || value === Note.KEINE ? null : value.getNoteSekI() })
	});

</script>

<style lang="postcss" scoped>

	.content {
		@apply w-full h-full grid grid-cols-2;
	}

</style>

