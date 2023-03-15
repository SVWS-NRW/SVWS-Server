<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="rowsFiltered" :columns="cols">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Klasse" />
				</template>
				<template #filterSimple>
					<svws-ui-toggle v-model="sichtbar">Sichtbar</svws-ui-toggle>
				</template>
			</svws-ui-data-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { DataTableColumn } from "@ui";
	import { computed, ref, Ref } from "vue";
	import { KlassenAuswahlProps } from "./SKlassenAuswahlProps";

	const props = defineProps<KlassenAuswahlProps>();
	const sichtbar: Ref<boolean> = ref(true);
	const search: Ref<string> = ref("");

	const rowsFiltered = computed(() => {
		const res = [];
		for (const k of props.mapKatalogeintraege.values())
			if (k.kuerzel?.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) && k.istSichtbar === sichtbar.value)
				res.push(k);
		return res;
	})

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	];

</script>
