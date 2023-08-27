<template>
	<svws-ui-content-card :title="title+'e Kombinationen'">
		<template #actions>
			<svws-ui-button size="small" type="secondary" @click="add_kurskombi">Hinzufügen <i-ri-add-circle-line class="-mr-1" /></svws-ui-button>
		</template>
		<svws-ui-data-table :items="[]" :no-data="rows.size() === 0" :columns="cols">
			<template #header>
				<!--				<svws-ui-table-row thead compact>
					<svws-ui-table-cell thead align="center" separate class="col-span-4">
						{{ title }}
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" separate class="col-span-6">
						Gültig in
					</svws-ui-table-cell>
					<svws-ui-table-cell thead align="center" class="col-span-2">
						&lt;!&ndash;Hinweis&ndash;&gt;
					</svws-ui-table-cell>
				</svws-ui-table-row>-->
				<svws-ui-table-row thead>
					<svws-ui-table-cell thead>
						Fach
					</svws-ui-table-cell>
					<svws-ui-table-cell thead separate>
						Kursart
					</svws-ui-table-cell>
					<svws-ui-table-cell thead>
						Fach
					</svws-ui-table-cell>
					<svws-ui-table-cell thead separate>
						Kursart
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center">
						EF.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center" separate>
						EF.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center">
						Q1.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center" separate>
						Q1.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center">
						Q2.1
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center" separate>
						Q2.2
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center">
						Text
					</svws-ui-table-cell>
					<svws-ui-table-cell thead padding-sm align="center" />
				</svws-ui-table-row>
			</template>
			<template #body>
				<svws-ui-table-row v-for="row in rows" :key="row.hashCode()">
					<s-row-gost-fachkombination :kombination="row" :faecher-manager="faecherManager" :patch-fachkombination="patchFachkombination" :remove-fachkombination="removeFachkombination" />
				</svws-ui-table-row>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, GostJahrgangFachkombination, GostFaecherManager } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import { ArrayList, GostLaufbahnplanungFachkombinationTyp } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		typ: GostLaufbahnplanungFachkombinationTyp;
		mapFachkombinationen: () => Map<number, GostJahrgangFachkombination>;
	}>();

	const title: ComputedRef<string> = computed(() => {
		switch(props.typ) {
			case GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH:
				return "Gefordert";
			case GostLaufbahnplanungFachkombinationTyp.VERBOTEN:
				return "Unzulässig";
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

	const cols: DataTableColumn[] = [
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
		{ key: "Hinweistext", label: "Hinweistext", align: 'center', span: 0.5, minWidth: 2 },
		{ key: "Trash", label: "Trash", align: 'center', span: 0.25, minWidth: 2 },
	];

</script>
