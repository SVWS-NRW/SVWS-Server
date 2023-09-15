<template>
	<svws-ui-content-card :title="title+'e Kombinationen'">
		<svws-ui-table :items="[]" :no-data="rows.size() === 0" :columns="cols">
			<template #header>
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="columnheader">
						Fach
					</div>
					<div class="svws-ui-td svws-divider" role="columnheader">
						Kursart
					</div>
					<div class="svws-ui-td" role="columnheader">
						Fach
					</div>
					<div class="svws-ui-td svws-divider" role="columnheader">
						Kursart
					</div>
					<div class="svws-ui-td svws-align-center" role="columnheader">
						EF.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider" role="columnheader">
						EF.2
					</div>
					<div class="svws-ui-td svws-align-center" role="columnheader">
						Q1.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider" role="columnheader">
						Q1.2
					</div>
					<div class="svws-ui-td svws-align-center" role="columnheader">
						Q2.1
					</div>
					<div class="svws-ui-td svws-align-center svws-divider" role="columnheader">
						Q2.2
					</div>
					<div class="svws-ui-td svws-align-center" role="columnheader">
						<!--Text-->
					</div>
					<div class="svws-ui-td svws-align-center" role="columnheader" />
				</div>
			</template>
			<template #body>
				<div v-for="row in rows" :key="row.hashCode()" class="svws-ui-tr" role="columnheader">
					<s-row-gost-fachkombination :kombination="row" :faecher-manager="faecherManager" :patch-fachkombination="patchFachkombination" :remove-fachkombination="removeFachkombination" />
				</div>
			</template>
			<template #actions>
				<svws-ui-button size="small" type="icon" @click="add_kurskombi"><i-ri-add-line /> </svws-ui-button>
			</template>
		</svws-ui-table>
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
		{ key: "Fach", label: "Fach", span: 1, minWidth: 5 },
		{ key: "Kursart", label: "Kursart", span: 1, minWidth: 5},
		{ key: "Fach", label: "Fach", align: 'center', span: 1, minWidth: 5 },
		{ key: "Kursart", label: "Kursart", align: 'center', span: 1, minWidth: 5 },
		{ key: "EF.1", label: "EF.1", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "EF.2", label: "EF.2", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q1.1", label: "Q1.1", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q1.2", label: "Q1.2", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q2.1", label: "Q2.1", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q2.2", label: "Q2.2", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Hinweistext", label: "Hinweistext", align: 'center', fixedWidth: 2 },
		{ key: "Trash", label: "Trash", align: 'center', span: 0.25, fixedWidth: 2 },
	];

</script>
