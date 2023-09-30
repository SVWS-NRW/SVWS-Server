<template>
	<svws-ui-content-card v-if="data">
		<svws-ui-table :columns="cols" :items="props.data?.leistungsdaten" hasBackground>
			<template #body="{rows}">
				<template v-for="row in rows" :key="row.source.id">
					<div class="svws-ui-tr" role="row" :style="{ '--background-color': bgColor(row.source.fachID) }">
						<div class="svws-ui-td" role="cell">
							<span>{{ getFach(row.source.fachID).bezeichnung }}</span>
						</div>
						<div class="svws-ui-td" role="cell">
							<span>{{ row.source.kursID == null ? "" : mapKurse.get(row.source.kursID)?.kuerzel }}</span>
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select title="—" v-model="getLehrer(row.source).value" :items="mapLehrer.values()" :item-text="getLehrerText" headless class="w-full" />
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
	import { type SchuelerLeistungsdaten, Note, type FaecherListeEintrag, type LehrerListeEintrag, ZulaessigesFach, DeveloperNotificationException } from "@core";
	import type { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const cols = [
		{ key: "fachID", label: "Fach", span: 0.75, sortable: true, minWidth: 14 },
		{ key: "kursID", label: "Kurs", span: 0.75, sortable: true, minWidth: 14 },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: true, minWidth: 20 },
		{ key: "note", label: "Note", span: 0.25, sortable: true },
	];

	function getLehrer(daten: SchuelerLeistungsdaten) : WritableComputedRef<LehrerListeEintrag | undefined> {
		return computed({
			get: () => daten.lehrerID === null ? undefined : props.mapLehrer.get(daten.lehrerID),
			set: (value) => void props.patchLeistung({ lehrerID: value === undefined ? null : value.id }, daten.id)
		});
	}

	function getNote(daten: SchuelerLeistungsdaten) : WritableComputedRef<Note> {
		return computed({
			get: () => Note.fromKuerzel(daten.note),
			set: (value) => void props.patchLeistung({ note: value.kuerzel }, daten.id)
		});
	}

	function getFach(idFach: number): FaecherListeEintrag {
		const f = props.mapFaecher.get(idFach);
		if (f === undefined)
			throw new DeveloperNotificationException("Kann das zugehörige Fach zu der ID nicht bestimmen.");
		return f;
	}

	function getLehrerText(lehrer: LehrerListeEintrag | null): string {
		if (lehrer === null)
			return "—";
		return lehrer.kuerzel + " (" + lehrer.nachname + ", " + lehrer.vorname + ")";
	}

	function bgColor(idFach: number) : string {
		return ZulaessigesFach.getByKuerzelASD(getFach(idFach).kuerzel).getHMTLFarbeRGB();
	}

</script>


