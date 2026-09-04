<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						:max-len="10" :disabled required />
					<svws-ui-textarea-input placeholder="Text"
						v-model="model.proxy.text"
						:validation="() => model.getFehler('text')"
						:disabled required @keydown.enter.prevent />
					<svws-ui-input-wrapper :grid="2">
						<ui-select label="Floskelgruppe"
							v-model="model.selectedFloskelgruppe.value"
							:manager="floskelgruppenManager"
							:disabled :removable="false" required />
						<ui-select v-if="model.hatFloskelgruppeArtFach.value" label="Fach"
							v-model="model.selectedFach.value"
							:manager="faecherManager"
							:disabled />
						<div v-else />
						<ui-select label="Jahrgang"
							v-model="model.selectedJahrgang.value"
							:manager="jahrgaengeManager"
							:disabled removable />
						<ui-select label="Niveau"
							v-model="model.selectedNiveau.value"
							:disabled :manager="niveauManager" />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Sortierung -->
			<svws-ui-content-card title="Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000" :disabled :removable="false" required />
					<svws-ui-spacing />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addFloskel" :disabled="!formIsValid || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { FloskelnNeuProps } from "./FloskelnNeuProps";
	import { FloskelModelProxy } from "~/components/schule/kataloge/floskeln/modelproxy/FloskelModelProxy";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { Floskel } from "@core/core/data/schule/Floskel";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<FloskelnNeuProps>();
	const benutzerState = useBenutzerState();

	const initialData = ref<Floskel>(Object.assign(new Floskel(), { sortierung: 32000 }));
	const model = new FloskelModelProxy(() => initialData.value, () => props.manager().liste.list(), props.manager);
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);

	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	// --- manager ---
	const floskelgruppenManager = new SelectManager({
		options: computed(() => props.manager().floskelgruppenById.values()),
		optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	const faecherManager = new SelectManager<FachDaten>({
		options: computed<FachDaten[]>(() => [...props.manager().faecherById.values()]),
		optionDisplayText: (f: FachDaten) => f.bezeichnung,
		selectionDisplayText: (f: FachDaten) => f.bezeichnung,
	});

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: computed<JahrgangsDaten[]>(() => [...props.manager().jahrgaengeById.values()]),
		optionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
		selectionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
	});

	const niveauManager = new SelectManager<number>({
		options: computed(() => props.manager().niveaus),
		optionDisplayText: String,
		selectionDisplayText: String,
	});

	// --- util ---
	async function addFloskel() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
