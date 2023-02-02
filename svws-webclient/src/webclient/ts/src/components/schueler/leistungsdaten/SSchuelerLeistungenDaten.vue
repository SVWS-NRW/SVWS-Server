<template>
	<div v-if="visible">
		<!-- TODO v-model="selected" - siehe auch unten-->
		<svws-ui-table :columns="cols" :data="liste" :footer="false">
			<template #cell-fach="{row}">
				<s-schueler-leistung-fach :data="props.data" :leistungsdaten="row" :map-faecher="props.mapFaecher" />
			</template>
			<template #cell-lehrer="{row}">
				<s-schueler-leistung-fachlehrer :data="props.data" :leistungsdaten="row" :map-lehrer="props.mapLehrer" />
			</template>
			<template #cell-note="{row}">
				<s-schueler-leistung-note :data="props.data" :leistungsdaten="row" />
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag, LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittListeEintrag, SchuelerLernabschnittsdaten, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ComputedRef } from "vue";
	import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";

	const props = defineProps<{
		lernabschnitt?: SchuelerLernabschnittListeEintrag;
		data: DataSchuelerAbschnittsdaten;
		mapFaecher: Map<number, FaecherListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const lernabschnittsdaten: ComputedRef<SchuelerLernabschnittsdaten> = computed(() => props.data.daten || new SchuelerLernabschnittsdaten());
	const liste: ComputedRef<SchuelerLeistungsdaten[]> = computed(() =>
		lernabschnittsdaten.value.leistungsdaten.toArray() as SchuelerLeistungsdaten[] || []
	);

	// TODO const selected: WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> = routeSchuelerLeistungenDaten.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "fach", label: "Fach", span: 1, sortable: false },
		{ key: "lehrer", label: "Lehrer", span: 1, sortable: false },
		{ key: "note", label: "Note", span: 1, sortable: false },
	];

	const visible: ComputedRef<boolean> = computed<boolean>(() => {
		return (props.lernabschnitt !== undefined);
	});

</script>
