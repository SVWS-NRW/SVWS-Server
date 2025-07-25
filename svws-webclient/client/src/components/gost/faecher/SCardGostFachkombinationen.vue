<template>
	<svws-ui-content-card>
		<template #title>
			<svws-ui-tooltip>
				<span class="content-card--headline">{{ title }}</span>
				<template #content>
					{{ typ !== GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH ? 'Fach 1 und Fach 2 schließen sich gegenseitig aus in den angegebenen Kursarten' : 'Fach 1 erfordert Fach 2 in den angegebenen Kursarten' }}
				</template>
			</svws-ui-tooltip>
		</template>
		<svws-ui-table :items :no-data="items.size() === 0" :columns has-background>
			<template #row="{ row: kombination }">
				<s-row-gost-fachkombination :typ :kombination :faecher-manager :patch-fachkombination :remove-fachkombination :hat-update-kompetenz />
			</template>
			<template #actions v-if="hatUpdateKompetenz">
				<svws-ui-button size="small" type="icon" @click="addFachkombination(typ)"><span class="icon i-ri-add-line" /> </svws-ui-button>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { List, GostJahrgangFachkombination, GostFaecherManager } from "@core";
	import { ArrayList, GostLaufbahnplanungFachkombinationTyp } from "@core";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<void>;
		addFachkombination: (typ: GostLaufbahnplanungFachkombinationTyp) => Promise<GostJahrgangFachkombination | undefined>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		typ: GostLaufbahnplanungFachkombinationTyp;
		mapFachkombinationen: () => Map<number, GostJahrgangFachkombination>;
		hatUpdateKompetenz: boolean;
	}>();

	const title = computed<string>(() => {
		switch(props.typ) {
			case GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH:
				return "Geforderte Kombinationen";
			case GostLaufbahnplanungFachkombinationTyp.VERBOTEN:
				return "Unzulässige Kombinationen";
			default:
				return "Fehler: Typ für die Fachkombination unbekannt";
		}
	});

	const items = computed<List<GostJahrgangFachkombination>>(() => {
		const result = new ArrayList<GostJahrgangFachkombination>();
		for (const kombi of props.mapFachkombinationen().values())
			if (GostLaufbahnplanungFachkombinationTyp.fromValue(kombi.typ) === props.typ)
				result.add(kombi);
		return result;
	});

	const columns = computed<DataTableColumn[]>(() => [
		{ key: "Fach1", label: "Fach 1", span: 1, minWidth: 5 },
		{ key: "Kursart1", label: "Kursart 1", span: 1, minWidth: 5},
		{ key: "Empty", label: (props.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH) ? "\u21d2" : "\u21d4 ", span: 0.25, minWidth: 1},
		{ key: "Fach2", label: "Fach 2", span: 1, minWidth: 5 },
		{ key: "Kursart2", label: "Kursart 2", span: 1, minWidth: 5 },
		{ key: "EF.1", label: "EF.1", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "EF.2", label: "EF.2", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q1.1", label: "Q1.1", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q1.2", label: "Q1.2", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q2.1", label: "Q2.1", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Q2.2", label: "Q2.2", align: 'center', span: 0.5, minWidth: 3 },
		{ key: "Hinweistext", label: " ", align: 'center', fixedWidth: 2 },
		{ key: "Trash", label: " ", align: 'center', span: 0.25, fixedWidth: 2 },
	]);

</script>
