<template>
	<svws-ui-content-card>
		<svws-ui-table :columns="cols" :items="props.manager().leistungGetMengeAsListSortedByFach()" has-background class="-mt-1">
			<template #body="{rows}">
				<template v-for="row in rows" :key="row.source.id">
					<div v-if="row.source.id !== null" class="svws-ui-tr" role="row" :style="{ '--background-color': props.manager().fachFarbeGetByLeistungsIdOrException(row.source.id) }">
						<div class="svws-ui-td" role="cell">
							<span>{{ props.manager().fachGetByLeistungIdOrException(row.source.id).bezeichnung }}</span>
							<!--
								<svws-ui-select title="—" :items="props.manager().fachGetMenge()" :item-text="fach => ((fach === null) || (fach.bezeichnung === null)) ? '—' : fach.bezeichnung"
									:model-value="manager().fachGetByLeistungIdOrException(row.source.id)"
									@update:model-value="value => patchLeistung({ fachID: ((value === null) || (value === undefined)) ? -1 : value.id }, row.source.id)"
									class="w-full" headless />
								-->
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="props.manager().kursGetMengeFilteredByLeistung(row.source.id)" :item-text="kurs => (kurs === null) ? '—' : kurs.kuerzel"
								:model-value="manager().kursGetByLeistungIdOrNull(row.source.id)"
								@update:model-value="value => patchLeistung({ kursID: ((value === null) || (value === undefined)) ? null : value.id }, row.source.id)"
								class="w-full" headless />
						</div>
						<div class="svws-ui-td" role="cell">
							<!-- TODO In Gesamtschulen kann bei Klassenunterricht neben PUK noch E oder G als Kursart vorkommen -->
							<template v-if="(manager().kursGetByLeistungIdOrNull(row.source.id) === null) || ZulaessigeKursart.getByAllgemeinerKursart(manager().kursGetByLeistungIdOrNull(row.source.id)!.kursartAllg).size() === 1">
								<span>{{ row.source.kursart }}</span>
							</template>
							<template v-else>
								<svws-ui-select title="—" :items="ZulaessigeKursart.getByAllgemeinerKursart(manager().kursGetByLeistungIdOrNull(row.source.id)!.kursartAllg)" :item-text="zk => zk.daten.kuerzel"
									:model-value="ZulaessigeKursart.getByASDKursart(row.source.kursart)"
									@update:model-value="value => patchLeistung({ kursart: ((value === null) || (value === undefined)) ? null : value.daten.kuerzel }, row.source.id)"
									class="w-full" headless />
							</template>
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="props.manager().lehrerGetMenge()" :item-text="lehrer => (lehrer === null) ? '—' : lehrer.kuerzel + ' (' + lehrer.nachname + ', ' + lehrer.vorname + ')'"
								:model-value="manager().lehrerGetByLeistungIdOrNull(row.source.id)"
								@update:model-value="value => patchLeistung({ lehrerID: ((value === null) || (value === undefined)) ? null : value.id }, row.source.id)"
								class="w-full" headless />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="Note.values()" :item-text="(item: Note) => item?.kuerzel"
								:model-value="Note.fromKuerzel(row.source.noteQuartal)"
								@update:model-value="value => patchLeistung({ noteQuartal: ((value === null) || (value === undefined)) ? null : value.kuerzel }, row.source.id)"
								headless class="w-full" />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" :items="Note.values()" :item-text="(item: Note) => item?.kuerzel"
								:model-value="Note.fromKuerzel(row.source.note)"
								@update:model-value="value => patchLeistung({ note: ((value === null) || (value === undefined)) ? null : value.kuerzel }, row.source.id)"
								headless class="w-full" />
						</div>
					</div>
				</template>
			</template>
		</svws-ui-table>
		<svws-ui-spacing :size="2" />
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
			<svws-ui-input-number placeholder="Unendschuldigt" :min="0"
				:model-value="manager().lernabschnittGet().fehlstundenUnentschuldigt"
				@change="fehlstundenUnentschuldigt => patch({ fehlstundenUnentschuldigt: fehlstundenUnentschuldigt ?? undefined })" />
		</svws-ui-input-wrapper>
		<!-- <svws-ui-todo title="Fehlzeiten" class="mt-10">
			Hier könnte demnächst die Übersicht über die Fehlzeiten implementiert werden.
		</svws-ui-todo> -->
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { Note, ZulaessigeKursart } from "@core";
	import type { SchuelerLernabschnittLeistungenProps } from "./SSchuelerLernabschnittLeistungenProps";

	const props = defineProps<SchuelerLernabschnittLeistungenProps>();

	const cols = [
		{ key: "fachID", label: "Fach", span: 0.75, sortable: false, minWidth: 14 },
		{ key: "kursID", label: "Kurs", span: 0.75, sortable: false, minWidth: 14 },
		{ key: "kursart", label: "Kursart", span: 0.25, sortable: false, minWidth: 5 },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: false, minWidth: 20 },
		{ key: "noteQuartal", label: "Quartalsnote", tooltip: "Quartalsnote", span: 0.25, sortable: false },
		{ key: "note", label: "Note", span: 0.25, sortable: false },
	];

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

