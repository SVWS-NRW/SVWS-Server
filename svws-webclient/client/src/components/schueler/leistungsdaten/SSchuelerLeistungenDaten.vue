<template>
	<svws-ui-content-card>
		<svws-ui-table :columns="cols" :items="props.manager().leistungGetMengeAsListSortedByFach()" has-background>
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
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Note } from "@core";
	import type { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const cols = [
		{ key: "fachID", label: "Fach", span: 0.75, sortable: false, minWidth: 14 },
		{ key: "kursID", label: "Kurs", span: 0.75, sortable: false, minWidth: 14 },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: false, minWidth: 20 },
		{ key: "noteQuartal", label: "Quartalsnote", span: 0.25, sortable: false },
		{ key: "note", label: "Note", span: 0.25, sortable: false },
	];

</script>


