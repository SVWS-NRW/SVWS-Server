<template>
	<div v-if="visible">
		<!-- TODO v-model="selected" - siehe auch unten-->
		<svws-ui-table :columns="cols" :data="liste" :footer="false">
			<template #cell-fach="{row}">
				<s-schueler-leistung-fach :data="props.data!" :leistungsdaten="row" :map-faecher="props.mapFaecher" />
			</template>
			<template #cell-lehrer="{row}">
				<s-schueler-leistung-fachlehrer :data="props.data!" :leistungsdaten="row" :map-lehrer="props.mapLehrer" />
			</template>
			<template #cell-note="{row}">
				<s-schueler-leistung-note :data="props.data!" :leistungsdaten="row" :patch-leistung="patchLeistung" />
			</template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">

	import { SchuelerLeistungsdaten } from "@svws-nrw/svws-core-ts";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ComputedRef } from "vue";
	import { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const liste: ComputedRef<SchuelerLeistungsdaten[]> = computed(() =>
		props.data?.leistungsdaten.toArray() as SchuelerLeistungsdaten[] || []
	);

	const cols: DataTableColumn[] = [
		{ key: "fach", label: "Fach", span: 1, sortable: false },
		{ key: "lehrer", label: "Lehrer", span: 1, sortable: false },
		{ key: "note", label: "Note", span: 1, sortable: false },
	];

	const visible: ComputedRef<boolean> = computed(() => props.data !== undefined);

</script>
