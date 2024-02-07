<template>
	<svws-ui-content-card>
		<svws-ui-table :items="[]" :no-data="false" :columns="cols" has-background>
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td svws-divider col-span-4" role="columnheader">
						<span>Angebotene Fächer</span>
					</div>
					<div class="svws-ui-td svws-align-center svws-divider col-span-2" role="columnheader">
						Leitfächer
					</div>
					<div class="svws-ui-td svws-divider svws-align-center col-span-6" role="columnheader">
						Wählbar
					</div>
					<div class="svws-ui-td svws-align-center col-span-2" role="columnheader">
						Abitur
					</div>
				</div>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td">
						Kürzel
					</div>
					<div class="svws-ui-td">
						Bezeichnung
					</div>
					<div class="svws-ui-td svws-align-center">
						<span class="svws-no-padding">Neu</span>
					</div>
					<div class="svws-ui-td svws-align-center svws-divider">
						<svws-ui-tooltip>
							<span>WS</span>
							<template #content>
								Wochenstunden
							</template>
						</svws-ui-tooltip>
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						2
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						EF.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						EF.2
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						Q1.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						Q1.2
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						Q2.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding">
						Q2.2
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						GK
					</div>
					<div class="svws-ui-td svws-align-center svws-no-padding">
						LK
					</div>
				</div>
			</template>
			<template #body>
				<s-row-gost-faecher v-for="fach in faecherManager().faecher()" :key="fach.hashCode()" :fach-id="fach.id" :abiturjahr="abiturjahr" :patch-fach="patchFach" :faecher-manager="faecherManager" />
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostFach, GostFaecherManager } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFach: (data: Partial<GostFach>, fach_id: number) => Promise<void>;
		abiturjahr: number;
	}>();

	const cols: DataTableColumn[] = [
		{ key: "Kuerzel", label: "Kürzel", span: 0.25, minWidth: 5 },
		{ key: "Fach", label: "Fach", span: 1, minWidth: 12},
		{ key: "Neu", label: "Neu", align: 'center', span: 0.1, minWidth: 2.5 },
		{ key: "WStd.", label: "WS", tooltip: "Wochenstunden", align: 'center', span: 0.25, minWidth: 3.5 },
		{ key: "1.", label: "1.", align: 'center', span: 0.25, minWidth: 6 },
		{ key: "2.", label: "2.", align: 'center', span: 0.25, minWidth: 6 },
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
