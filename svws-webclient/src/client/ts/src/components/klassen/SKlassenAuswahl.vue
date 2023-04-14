<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="rowsFiltered" :columns="cols">
				<template #cell(schueler)="{value}"> {{ value.size() }} </template>
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

	import type { DataTableColumn } from "@ui";
	import type { Ref } from "vue";
	import { computed, ref } from "vue";
	import type { KlassenAuswahlProps } from "./SKlassenAuswahlProps";

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
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "schueler", label: "Schüler", span: 0.5 }
	];

</script>
