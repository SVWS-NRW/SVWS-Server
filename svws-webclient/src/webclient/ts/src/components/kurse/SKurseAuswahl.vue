<template>
	<svws-ui-secondary-menu>
		<template #headline>Kurse</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :model-value:clicked="auswahl" @update:clicked="setKurs" :items="rows"
					:columns="cols" clickable :footer="false" unique-key="id" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { KursListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed } from "vue";
	import { KurseAuswahlProps } from "./SKurseAuswahlProps";

	const props = defineProps<KurseAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "lehrer_name", label: "Fachlehrer", sortable: true },
		{ key: "jahrgang", label: "Jahrgang", sortable: true }
	];

	/**
	 * Bestimmt für die übergebene Lehrer-ID dessen Kürzel
	 *
	 * @param idLehrer   die ID des Lehrers
	 */
	function getLehrerKuerzel(idLehrer: number | null) : string {
		if (idLehrer === null)
			return "";
		const lehrer = props.mapLehrer.get(idLehrer);
		if (lehrer === undefined)
			return "";
		return lehrer.kuerzel;
	}

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

	const rows = computed(() =>
		[...props.listKurse].map((e: KursListeEintrag) => ({
			...e,
			lehrer_name: getLehrerKuerzel(e.lehrer),
			jahrgang: getJahrgangsKuerzel(e.idJahrgaenge)
		}))
	);
</script>
