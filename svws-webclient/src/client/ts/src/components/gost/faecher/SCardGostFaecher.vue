<template>
	<svws-ui-content-card title="Angebotene Fächer im Jahrgang" class="table--with-background">
		<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
			<template #header>
				<svws-ui-table-row compact thead>
					<svws-ui-table-cell thead align="center" separate class="col-span-4" />
					<svws-ui-table-cell thead align="center" separate class="col-span-2">
						Leitfächer
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" separate class="col-span-6">
						Wählbar
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" separate class="col-span-2">
						im Abitur
					</svws-ui-table-cell>
				</svws-ui-table-row>
				<svws-ui-table-row compact thead>
					<svws-ui-table-cell thead>
						Kürzel
					</svws-ui-table-cell>
					<svws-ui-table-cell thead>
						Fach
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						Neu
					</svws-ui-table-cell>
					<svws-ui-table-cell tooltip="Wochenstunden" thead align="center" separate>
						WS
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						1.
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm separate>
						2.
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						EF.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm separate>
						EF.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						Q1.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm separate>
						Q1.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						Q2.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm separate>
						Q2.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						GK
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" padding-sm>
						LK
					</svws-ui-table-cell>
				</svws-ui-table-row>
			</template>
			<template #body>
				<s-row-gost-faecher v-for="fach in faecher" :key="fach.id" :fach-id="fach.id" :abiturjahr="abiturjahr" :patch-fach="patchFach" :faecher-manager="faecherManager" />
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	import type { GostFach, GostFaecherManager, LinkedCollection} from "@core";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		abiturjahr: number;
	}>();

	const faecher: ComputedRef<LinkedCollection<GostFach>> = computed(() => {
		return props.faecherManager().faecher();
	});

	const cols: Array<DataTableColumn> = [
		{ key: "Kuerzel", label: "Kürzel", span: 0.25, minWidth: 5 },
		{ key: "Fach", label: "Fach", span: 1, minWidth: 12},
		{ key: "Neu", label: "Neu", align: 'center', span: 0.2, minWidth: 2.5 },
		{ key: "WStd.", label: "WS", tooltip: "Wochenstunden", align: 'center', span: 0.2, minWidth: 2.5 },
		{ key: "1.", label: "1.", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "2.", label: "2.", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "EF.1", label: "EF.1", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "EF.2", label: "EF.2", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "Q1.1", label: "Q1.1", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "Q1.2", label: "Q1.2", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "Q2.1", label: "Q2.1", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "Q2.2", label: "Q2.2", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "LK", label: "LK", align: 'center', span: 0.25, minWidth: 2.5 }
	];

</script>
