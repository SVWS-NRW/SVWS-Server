<template>
	<svws-ui-content-card>
		<svws-ui-table :columns="cols" :items="props.manager().leistungGetMengeAsListSortedByFach()" has-background>
			<template #body="{rows}">
				<template v-for="row in rows" :key="row.source.id">
					<div class="svws-ui-tr" role="row" :style="{ '--background-color': props.manager().fachFarbeGetByLeistungsIdOrException(row.source.id) }">
						<div class="svws-ui-td" role="cell">
							<span>{{ props.manager().fachGetByLeistungIdOrException(row.source.id).bezeichnung }}</span>
						</div>
						<div class="svws-ui-td" role="cell">
							<span>{{ row.source.kursID == null ? "" : props.manager().kursGetByLeistungIdOrException(row.source.id).kuerzel }}</span>
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" v-model="getLehrer(row.source).value" :items="props.manager().lehrerGetMenge()" :item-text="getLehrerText" headless class="w-full" />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" v-model="getNote(row.source).value" :items="Note.values()" :item-text="(item: Note) => item?.kuerzel" headless class="w-full" />
						</div>
					</div>
				</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, type WritableComputedRef } from "vue";
	import { type SchuelerLeistungsdaten, Note, type LehrerListeEintrag } from "@core";
	import type { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const cols = [
		{ key: "fachID", label: "Fach", span: 0.75, sortable: false, minWidth: 14 },
		{ key: "kursID", label: "Kurs", span: 0.75, sortable: false, minWidth: 14 },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: false, minWidth: 20 },
		{ key: "note", label: "Note", span: 0.25, sortable: false },
	];

	function getLehrer(daten: SchuelerLeistungsdaten) : WritableComputedRef<LehrerListeEintrag | undefined> {
		return computed({
			get: () => daten.lehrerID === null ? undefined : props.manager().lehrerGetByLeistungIdOrException(daten.id),
			set: (value) => void props.patchLeistung({ lehrerID: value === undefined ? null : value.id }, daten.id)
		});
	}

	function getNote(daten: SchuelerLeistungsdaten) : WritableComputedRef<Note> {
		return computed({
			get: () => Note.fromKuerzel(daten.note),
			set: (value) => void props.patchLeistung({ note: value.kuerzel }, daten.id)
		});
	}

	function getLehrerText(lehrer: LehrerListeEintrag | null): string {
		if (lehrer === null)
			return "—";
		return lehrer.kuerzel + " (" + lehrer.nachname + ", " + lehrer.vorname + ")";
	}

</script>


