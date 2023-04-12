<template>
	<template v-if="visible">
		<svws-ui-data-table :columns="cols" :items="props.data?.leistungsdaten">
			<template #cell(fachID)="{rowData}">
				<s-schueler-leistung-fach :fach="rowData.fachID" :map-faecher="mapFaecher" />
			</template>
			<template #cell(lehrerID)="{rowData}">
				<s-schueler-leistung-fachlehrer :fachlehrer="rowData.lehrerID" :map-lehrer="props.mapLehrer" />
			</template>
			<template #cell(note)="{rowData}">
				<s-schueler-leistung-note :data="props.data!" :note="rowData.note" :patch-leistung="patchLeistung" />
			</template>
		</svws-ui-data-table>
	</template>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const cols: DataTableColumn[] = [
		{ key: "fachID", label: "Fach", span: 1, sortable: false },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: false },
		{ key: "note", label: "Note", span: 1, sortable: false },
	];

	const visible: ComputedRef<boolean> = computed(() => props.data !== undefined);

</script>

<style scoped lang="postcss">
	.data-table {
		@apply overflow-visible;
	}
</style>

