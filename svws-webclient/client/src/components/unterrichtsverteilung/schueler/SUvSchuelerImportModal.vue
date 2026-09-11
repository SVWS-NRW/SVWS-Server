<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="small">
		<template #modalTitle>Schüler importieren</template>
		<template #modalContent>
			<div class="grid grid-cols-1 gap-4">
				<svws-ui-select v-model="selectedAbschnitt"
					:items="schuljahresabschnittItems"
					:item-text="abschnittText"
					title="Schuljahresabschnitt"
					required />
				<svws-ui-select v-model="selectedJahrgangszuweisung"
					:items="jahrgangszuweisungItems"
					:item-text="eintrag => eintrag.text"
					title="Jahrgänge"
					required />
				<svws-ui-select v-model="selectedKlassenzuweisung"
					:items="klassenzuweisungItems"
					:item-text="eintrag => eintrag.text"
					title="Klassenzuweisungen"
					required />
				<svws-ui-checkbox v-if="selectedJahrgangszuweisung.value === 'folgejahrgang'" v-model="versetzungsvermerkeBeruecksichtigen">
					Versetzungsvermerke berücksichtigen
				</svws-ui-checkbox>
				<svws-ui-checkbox v-if="selectedKlassenzuweisung.value !== 'keine'" v-model="createMissingKlassen">
					Fehlende Klassen anlegen
				</svws-ui-checkbox>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="doImport" :disabled="selectedAbschnitt === undefined"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";

	const show = ref<boolean>(false);
	const abschnittState = useAbschnittState();
	const state = useUvState();
	const selectedAbschnitt = ref<Schuljahresabschnitt | undefined>(undefined);
	type Jahrgangszuweisung = "beibehalten" | "folgejahrgang";
	type Klassenzuweisung = "keine" | "beibehalten";

	const selectedJahrgangszuweisung = ref<{ value: Jahrgangszuweisung; text: string }>({ value: "beibehalten", text: "beibehalten" });
	const selectedKlassenzuweisung = ref<{ value: Klassenzuweisung; text: string }>({ value: "beibehalten", text: "beibehalten" });
	const versetzungsvermerkeBeruecksichtigen = ref<boolean>(true);
	const createMissingKlassen = ref<boolean>(true);
	const jahrgangszuweisungItems = [
		{ value: "beibehalten" as const, text: "beibehalten" },
		{ value: "folgejahrgang" as const, text: "Folgejahrgang" },
	];
	const klassenzuweisungItems = [
		{ value: "keine" as const, text: "keine" },
		{ value: "beibehalten" as const, text: "beibehalten" },
	];

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const abschnittText = (sja: Schuljahresabschnitt) => `${sja.schuljahr}/${sja.schuljahr + 1} - ${sja.abschnitt}. Abschnitt`;

	const openModal = () => {
		selectedAbschnitt.value = abschnittState.auswahl;
		selectedJahrgangszuweisung.value = jahrgangszuweisungItems[0];
		selectedKlassenzuweisung.value = klassenzuweisungItems[1];
		versetzungsvermerkeBeruecksichtigen.value = true;
		createMissingKlassen.value = true;
		show.value = true;
	};

	async function doImport() {
		if (selectedAbschnitt.value === undefined) {
			return;
		}
		await state.importSchueler({
			idSchuljahresabschnitt: selectedAbschnitt.value.id,
			folgejahrgang: selectedJahrgangszuweisung.value.value === "folgejahrgang",
			klassenzuweisungenUebernehmen: selectedKlassenzuweisung.value.value === "beibehalten",
			versetzungsvermerkeBeruecksichtigen: versetzungsvermerkeBeruecksichtigen.value,
			createMissingKlassen: (selectedKlassenzuweisung.value.value === "keine") ? false : createMissingKlassen.value,
		});
		show.value = false;
	}

</script>
