<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="data.proxy.kuerzel"
						:validation="() => data.getFehler('kuerzel')"
						skip-default-validation
						:min-len="1" :max-len="20" required />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="data.proxy.bezeichnung"
						:validation="() => data.getFehler('bezeichnung')"
						skip-default-validation
						:min-len="1" :max-len="100" required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="data.proxy.kurzbezeichnung"
						:validation="() => data.getFehler('kurzbezeichnung')"
						skip-default-validation
						:max-len="2" />
					<ui-select label="Folgejahrgang"
						v-model="data.folgejahrgang.value"
						:manager="folgeJahrgangManager"
						skip-default-validation />
					<ui-select label="Schulgliederung ASD-Kürzel"
						v-model="data.schulgliederung.value"
						:manager="schulgliederungKuerzelSelectManager"
						skip-default-validation
						searchable statistics />
					<ui-select label="Schulgliederung ASD-Text"
						v-model="data.schulgliederung.value"
						:manager="schulgliederungTextSelectManager"
						skip-default-validation
						searchable statistics />
					<ui-select label="Jahrgang ASD-Kürzel"
						:manager="jahrgangKuerzelSelectManager"
						v-model="data.statistikJahrgang.value"
						:validation="() => data.getFehler('kuerzelStatistik')"
						skip-default-validation
						searchable statistics required :removable="false" />
					<ui-select label="Jahrgang ASD-Text"
						:manager="jahrgangTextSelectManager"
						v-model="data.statistikJahrgang.value"
						:validation="() => data.getFehler('kuerzelStatistik')"
						skip-default-validation
						statistics searchable required :removable="false" />
					<svws-ui-input-number placeholder="Anzahl der Restabschnitte"
						v-model="data.proxy.anzahlRestabschnitte"
						:validation="() => data.getFehler('anzahlRestabschnitte')"
						skip-default-validation
						:min="0" :max="40" />
					<ui-select label="Bildungsstufe"
						:manager="bildungsstufeSelectManager"
						v-model="data.bildungsstufe.value"
						skip-default-validation />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.proxy.sortierung"
						:validation="() => data.getFehler('sortierung')"
						skip-default-validation
						:min="0" :max="32000" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.proxy.istSichtbar"
						:validation="() => data.getFehler('istSichtbar')">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addJahrgangsdaten" :disabled="!isValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { JahrgaengeNeuProps } from "./JahrgaengeNeuProps";
	import { computed, ref, watch } from "vue";
	import { Bildungsstufe, Jahrgaenge, JahrgangsDaten, Schulgliederung } from "@core";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { JahrgaengeModelProxy } from "~/components/schule/kataloge/jahrgaenge/modelproxy/JahrgaengeModelProxy";

	const props = defineProps<JahrgaengeNeuProps>();
	const initialData = ref<JahrgangsDaten>(Object.assign(new JahrgangsDaten(), { istSichtbar: true, sortierung: 32000, anzahlRestabschnitte: 0 }));
	const data = new JahrgaengeModelProxy(() => initialData.value, () => props.manager().liste.list(), props.schuljahr);
	const isLoading = ref<boolean>(false);
	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()]);
	const folgeJahrgangManager = new SelectManager({
		options: jahrgaenge,
		optionDisplayText: v => v.bezeichnung ?? "",
		selectionDisplayText: v => v.bezeichnung ?? "",
	});

	const schulgliederungTextSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulgliederungKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const jahrgangTextSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const jahrgangKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const bildungsstufeSelectManager = new CoreTypeSelectManager({
		clazz: Bildungsstufe.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const isValid = computed<boolean>(() => data.getAlleFehler().isEmpty());

	// --- util ---
	async function addJahrgangsdaten() {
		if ((isLoading.value === true) || (!isValid.value)) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.proxy, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
