<template>
	<svws-ui-secondary-menu>
		<template #headline>Kurse</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header>
			<div class="mt-6 mb-2 flex gap-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Kurs"><i-ri-search-line /></svws-ui-text-input>
				<svws-ui-toggle v-model="sichtbar">Sichtbar</svws-ui-toggle>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" @update:clicked="setKurs" :items="rowsFiltered" :columns="cols" clickable :footer="false">
					<template #cell(lehrer)="{ value }">
						{{ mapLehrer.get(value)?.kuerzel ?? "" }}
					</template>
					<template #cell(idJahrgaenge)="{ value }">
						{{ getJahrgangsKuerzel(value) }}
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { Vector } from "@svws-nrw/svws-core";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { Ref, ref, computed } from "vue";
	import { KurseAuswahlProps } from "./SKurseAuswahlProps";

	const props = defineProps<KurseAuswahlProps>();

	const sichtbar: Ref<boolean> = ref(true);
	const search: Ref<string> = ref("");

	const rowsFiltered = computed(() => {
		const res = [];
		for (const k of props.listKurse)
			if (k.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) && k.istSichtbar === sichtbar.value)
				res.push(k);
		return res;
	})

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "lehrer", label: "Fachlehrer", sortable: true },
		{ key: "idJahrgaenge", label: "Jahrgänge", sortable: true }
	];

	/**
	 * Ermittel eine komma-separierte Liste der Kürzel der Jahrgänge mit den übergebenen IDs.
	 *
	 * @param jahrgaenge   die Liste von Jahrgangs-IDs
	 */
	function getJahrgangsKuerzel(jahrgaenge: Vector<number>) : string {
		// Prüfe zunächst, ob die Liste der Jahrgänge von dem Kurs einen Jahrgang der Map beinhaltet.
		let found = false;
		let result = "";
		for (const jg of jahrgaenge) {
			const jahrgang = props.mapJahrgaenge.get(jg);
			if ((jahrgang !== undefined) && (jahrgang.kuerzel !== null)) {
				if (found)
					result += ",";
				result += jahrgang.kuerzel;
				found = true;
			}
		}
		return result;
	}

</script>
