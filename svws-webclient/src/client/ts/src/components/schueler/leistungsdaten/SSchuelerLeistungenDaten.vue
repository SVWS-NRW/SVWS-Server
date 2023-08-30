<template>
	<svws-ui-content-card v-if="visible">
		<svws-ui-data-table :columns="cols" :items="props.data?.leistungsdaten">
			<template #cell(fachID)="{rowData}">
				<s-schueler-leistung-fach :fach="rowData.fachID" :map-faecher="mapFaecher" />
			</template>
			<template #cell(lehrerID)="{rowData}">
				<s-schueler-leistung-fachlehrer :fachlehrer="rowData.lehrerID" :map-lehrer="props.mapLehrer" />
			</template>
			<template #cell(note)="{rowData}">
				<s-schueler-leistung-note :data="props.data!" :note="rowData.note ?? ''" :patch-leistung="patchLeistung" />
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const cols = [
		{ key: "fachID", label: "Fach", span: 0.75, sortable: true, minWidth: 14 },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: true, minWidth: 20 },
		{ key: "note", label: "Note", span: 0.25, sortable: true },
	];

	const visible: ComputedRef<boolean> = computed(() => props.data !== undefined);

</script>

<style scoped lang="postcss">
	.data-table {
		@apply overflow-visible;
	}
</style>

