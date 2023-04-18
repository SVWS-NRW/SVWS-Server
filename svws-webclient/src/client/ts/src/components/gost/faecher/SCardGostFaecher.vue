<template>
	<svws-ui-content-card>
		<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate col-span-4" />
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Leitfächer
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-6 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Wählbar
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								im Abitur
							</div>
						</div>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Kürzel
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Neu
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								WStd.
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								1.
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								2.
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q1.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q1.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								LK
							</div>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<s-row-gost-faecher v-for="fach in faecher" :key="fach.id" :fach="fach" :abiturjahr="abiturjahr" :map-leitfaecher="mapLeitfaecher" :patch-fach="patchFach" />
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	import type { GostFach, GostFaecherManager, LinkedCollection} from "@svws-nrw/svws-core";
	import { Fachgruppe, ZulaessigesFach } from "@svws-nrw/svws-core";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		faecherManager: GostFaecherManager;
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<boolean>;
		abiturjahr: number;
	}>();

	const faecher: ComputedRef<LinkedCollection<GostFach>> = computed(() => {
		return props.faecherManager.faecher();
	});

	const mapLeitfaecher: ComputedRef<Map<number, GostFach>> = computed(() => {
		const result = new Map<number, GostFach>();
		for (const fach of faecher.value) {
			const fg = ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe();
			if ((fg !== Fachgruppe.FG_VX) && (fg !== Fachgruppe.FG_PX))
				result.set(fach.id, fach);
		}
		return result;
	});

	const cols: Array<DataTableColumn> = [
		{ key: "Kuerzel", label: "Kürzel", span: 0.4, minWidth: 3 },
		{ key: "Fach", label: "Fach", span: 0.75, minWidth: 6},
		{ key: "Neu", label: "Neu", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "WStd.", label: "WStd.", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "1.", label: "1.", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "2.", label: "2.", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "EF.1", label: "EF.1", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "EF.2", label: "EF.2", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "Q1.1", label: "Q1.1", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "Q1.2", label: "Q1.2", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "Q2.1", label: "Q2.1", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "Q2.2", label: "Q2.2", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 3 },
		{ key: "LK", label: "LK", align: 'center', span: 0.25, minWidth: 3 }
	];

</script>
