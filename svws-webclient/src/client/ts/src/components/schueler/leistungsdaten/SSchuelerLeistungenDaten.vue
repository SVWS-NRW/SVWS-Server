<template>
	<div v-if="visible">
		<!-- TODO v-model="selected" - siehe auch unten-->
		<svws-ui-data-table :columns="cols" :items="props.data?.leistungsdaten" :footer="false">
			<template #cell(fachID)="{value}">
				<s-schueler-leistung-fach :fach="value" :map-faecher="mapFaecher" />
			</template>
			<template #cell(lehrerID)="{value}">
				<s-schueler-leistung-fachlehrer :fachlehrer="value" :map-lehrer="props.mapLehrer" />
			</template>
			<template #cell(note)="{value}">
				<s-schueler-leistung-note :data="props.data!" :note="value" :patch-leistung="patchLeistung" />
			</template>
		</svws-ui-data-table>
	</div>
</template>

<script setup lang="ts">

	import { DataTableColumn } from "@ui";
	import { computed, ComputedRef } from "vue";
	import { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();
	console.log(props.data?.leistungsdaten)

	const cols: DataTableColumn[] = [
		{ key: "fachID", label: "Fach", span: 1, sortable: false },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: false },
		{ key: "note", label: "Note", span: 1, sortable: false },
	];

	const visible: ComputedRef<boolean> = computed(() => props.data !== undefined);

</script>
