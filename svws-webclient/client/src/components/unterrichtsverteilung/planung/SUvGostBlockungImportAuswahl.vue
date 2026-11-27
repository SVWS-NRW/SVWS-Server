<template>
	<div class="grid gap-4">
		<output v-if="loadingImportquelle" class="text-sm text-ui-secondary">Importquellen werden geladen …</output>
		<svws-ui-notification v-if="quellenError" type="error">{{ quellenError }}</svws-ui-notification>
		<svws-ui-select v-model="selectedAbiturjahrgang"
			:items="abiturjahrgangItems"
			:item-text="jg => `${jg.bezeichnung} (Abi ${jg.abiturjahr})`"
			title="Abiturjahrgang"
			required :disabled />
		<svws-ui-select v-model="selectedHalbjahr"
			:items="halbjahrItems"
			:item-text="hj => hj.kuerzel"
			title="Halbjahr"
			required :disabled />
		<svws-ui-select v-model="selectedBlockung"
			:items="blockungItems"
			:item-text="b => b.name"
			title="Blockung"
			required
			:disabled="disabled || loadingBlockungen || blockungItems.length === 0" />
		<svws-ui-select v-model="selectedErgebnis"
			:items="ergebnisItems"
			:item-text="e => `Ergebnis ${e.id}${e.istAktiv ? ' (aktiv)' : ''}`"
			title="Ergebnis"
			required
			:disabled="disabled || loadingErgebnisse || ergebnisItems.length === 0" />
		<svws-ui-select v-model="selectedAbschnitt"
			:items="schuljahresabschnittItems"
			:item-text="schuljahresabschnittText"
			title="Schuljahresabschnitt der anzulegenden Lerngruppen"
			required :disabled />
	</div>
</template>

<script setup lang="ts">
	import { computed, onScopeDispose, ref, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
	import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
	import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";

	import { schuljahresabschnittText } from '../UvPresenter';

	export interface UvGostBlockungImportQuelle {
		idBlockung: number;
		ergebnis: GostBlockungsergebnis;
		abschnitt: Schuljahresabschnitt;
		text: string;
	}

	defineProps<{
		disabled: boolean;
	}>();

	const quelle = defineModel<UvGostBlockungImportQuelle>();
	const state = useUvState();
	const abschnittState = useAbschnittState();
	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);
	const selectedAbschnitt = ref<Schuljahresabschnitt | undefined>(abschnittState.auswahl);
	const halbjahrItems = GostHalbjahr.values();

	const quellenError = ref('');
	const loadingQuellen = ref(false);
	const loadingBlockungen = ref(false);
	const loadingErgebnisse = ref(false);
	const loadingImportquelle = computed(() => loadingQuellen.value || loadingBlockungen.value || loadingErgebnisse.value);
	// Gost-Blockung
	const abiturjahrgangItems = ref<GostJahrgang[]>([]);
	const selectedAbiturjahrgang = ref<GostJahrgang | undefined>(undefined);
	const selectedHalbjahr = ref<GostHalbjahr | undefined>(undefined);
	const selectedBlockung = ref<GostBlockungListeneintrag | undefined>(undefined);
	const selectedErgebnis = ref<GostBlockungsergebnis | undefined>(undefined);

	const blockungItems = ref<GostBlockungListeneintrag[]>([]);
	const ergebnisItems = ref<GostBlockungsergebnis[]>([]);


	watch([selectedAbiturjahrgang, selectedHalbjahr, selectedBlockung, selectedErgebnis, selectedAbschnitt, loadingImportquelle], () => {
		const jahrgang = selectedAbiturjahrgang.value;
		const halbjahr = selectedHalbjahr.value;
		const blockung = selectedBlockung.value;
		const ergebnis = selectedErgebnis.value;
		const abschnitt = selectedAbschnitt.value;
		if (loadingImportquelle.value || jahrgang === undefined || halbjahr === undefined || blockung === undefined || ergebnis === undefined || abschnitt === undefined) {
			quelle.value = undefined;
			return;
		}
		quelle.value = {
			idBlockung: blockung.id,
			ergebnis,
			abschnitt,
			text: `${jahrgang.bezeichnung} · ${halbjahr.kuerzel} · ${blockung.name} · Ergebnis ${ergebnis.id}`,
		};
	});

	let geschlossen = false;
	onScopeDispose(() => {
		geschlossen = true;
	});

	async function ladeAbiturjahrgaenge(): Promise<void> {
		loadingQuellen.value = true;
		try {
			const items = await state.planung.getGostAbiturjahrgaenge();
			if (!geschlossen) {
				abiturjahrgangItems.value = items;
			}
		} catch {
			if (!geschlossen) {
				abiturjahrgangItems.value = [];
				quellenError.value = 'Die Abiturjahrgänge konnten nicht geladen werden. Den Import erneut öffnen oder Kurse aus einem Schuljahresabschnitt wählen.';
			}
		} finally {
			if (!geschlossen) {
				loadingQuellen.value = false;
			}
		}
	}

	void ladeAbiturjahrgaenge();

	// Wenn Abiturjahrgang sich ändert: Halbjahr zurücksetzen
	watch(selectedAbiturjahrgang, () => {
		selectedHalbjahr.value = undefined;
		selectedBlockung.value = undefined;
		blockungItems.value = [];
		selectedErgebnis.value = undefined;
		ergebnisItems.value = [];
	});

	// Veraltete Antworten dürfen neuere Auswahlen nicht überschreiben.
	watch(selectedHalbjahr, async (halbjahr, _oldValue, onCleanup) => {
		const cancelled = ref(false);
		onCleanup(() => {
			cancelled.value = true;
		});
		blockungItems.value = [];
		selectedBlockung.value = undefined;
		ergebnisItems.value = [];
		selectedErgebnis.value = undefined;
		loadingBlockungen.value = false;
		const jahrgang = selectedAbiturjahrgang.value;
		if (jahrgang === undefined || halbjahr === undefined) {
			return;
		}
		loadingBlockungen.value = true;
		quellenError.value = '';
		try {
			const items = await state.planung.getGostBlockungen(jahrgang.abiturjahr, halbjahr.id);
			if (!cancelled.value) {
				blockungItems.value = items;
			}
		} catch {
			if (!cancelled.value) {
				quellenError.value = 'Die Blockungen konnten nicht geladen werden. Bitte das Halbjahr erneut auswählen.';
			}
		} finally {
			if (!cancelled.value) {
				loadingBlockungen.value = false;
			}
		}
	});

	watch(selectedBlockung, async (blockung, _oldValue, onCleanup) => {
		const cancelled = ref(false);
		onCleanup(() => {
			cancelled.value = true;
		});
		ergebnisItems.value = [];
		selectedErgebnis.value = undefined;
		loadingErgebnisse.value = false;
		if (blockung === undefined) {
			return;
		}
		loadingErgebnisse.value = true;
		quellenError.value = '';
		try {
			const items = await state.planung.getGostBlockungsergebnisse(blockung.id);
			if (!cancelled.value) {
				ergebnisItems.value = items;
			}
		} catch {
			if (!cancelled.value) {
				quellenError.value = 'Die Blockungsergebnisse konnten nicht geladen werden. Bitte die Blockung erneut auswählen.';
			}
		} finally {
			if (!cancelled.value) {
				loadingErgebnisse.value = false;
			}
		}
	});

</script>
