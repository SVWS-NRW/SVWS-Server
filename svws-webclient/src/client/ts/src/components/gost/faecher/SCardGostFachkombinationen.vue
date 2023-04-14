<template>
	<svws-ui-content-card :title="title">
		<template #actions>
			<svws-ui-button size="small" @click="add_kurskombi">Hinzufügen</svws-ui-button>
		</template>
		<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate col-span-4">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fachkombination
							</div>
						</div>
					</div>
					<div role="columnheader" class="data-table__th data-table__thead__th data-table__th__align-center col-span-7">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Gültig in
							</div>
						</div>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Kursart
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
						class="data-table__th data-table__thead__th data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Kursart
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
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
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
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
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
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__padding-sm" />
				</div>
			</template>
			<template #body>
				<div role="row" class="data-table__tr" v-for="row in rows" :key="row.id">
					<s-row-gost-fachkombination :kombination="row" :faecher-manager="faecherManager" :patch-fachkombination="patchFachkombination" :remove-fachkombination="removeFachkombination" />
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { List, GostJahrgangFachkombination, GostFaecherManager } from "@svws-nrw/svws-core";
	import { ArrayList, GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		faecherManager: GostFaecherManager;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		typ: GostLaufbahnplanungFachkombinationTyp;
		mapFachkombinationen: () => Map<number, GostJahrgangFachkombination>;
	}>();

	const title: ComputedRef<string> = computed(() => {
		switch(props.typ) {
			case GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH:
				return "Geforderte Fachkombinationen";
			case GostLaufbahnplanungFachkombinationTyp.VERBOTEN:
				return "Unzulässige Fachkombinationen";
			default:
				return "Fehler: Typ für die Fachkombination unbekannt";
		}
	});

	const rows: ComputedRef<List<GostJahrgangFachkombination>> = computed(() => {
		const result = new ArrayList<GostJahrgangFachkombination>();
		for (const kombi of props.mapFachkombinationen().values())
			if (GostLaufbahnplanungFachkombinationTyp.fromValue(kombi.typ) === props.typ)
				result.add(kombi);
		return result;
	});

	async function add_kurskombi() {
		void props.addFachkombination(props.typ);
	}

	const cols: Array<DataTableColumn> = [
		{ key: "Fach", label: "Fach", span: 1 },
		{ key: "Kursart", label: "Kursart", span: 1},
		{ key: "Fach", label: "Fach", align: 'center', span: 1, minWidth: 2.5 },
		{ key: "Kursart", label: "Kursart", align: 'center', span: 1, minWidth: 2.5 },
		{ key: "EF.1", label: "EF.1", align: 'center', span: 0.5, minWidth: 2.5 },
		{ key: "EF.2", label: "EF.2", align: 'center', span: 0.5, minWidth: 2.5 },
		{ key: "Q1.1", label: "Q1.1", align: 'center', span: 0.5, minWidth: 2.5 },
		{ key: "Q1.2", label: "Q1.2", align: 'center', span: 0.5, minWidth: 2.5 },
		{ key: "Q2.1", label: "Q2.1", align: 'center', span: 0.5, minWidth: 2.5 },
		{ key: "Q2.2", label: "Q2.2", align: 'center', span: 0.5, minWidth: 2.5 },
		{ key: "Trash", label: "Trash", align: 'center', span: 0.25, minWidth: 2 },
	];

</script>
