<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header>
			<div class="mt-6 mb-2 flex gap-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Klasse"><i-ri-search-line /></svws-ui-text-input>
				<svws-ui-toggle v-model="sichtbar">Sichtbar</svws-ui-toggle>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" @update:clicked="setKlasse" clickable :columns="cols" :items="rowsFiltered" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ref, Ref } from "vue";
	import { KlassenAuswahlProps } from "./SKlassenAuswahlProps";

	const props = defineProps<KlassenAuswahlProps>();

	const sichtbar: Ref<boolean> = ref(true);
	const search: Ref<string> = ref("");

	const rowsFiltered = computed(() => {
		const res = [];
		for (const k of props.listKlassen)
			if (k.kuerzel?.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) && k.istSichtbar === sichtbar.value)
				res.push(k);
		return res;
	})

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	];

</script>
