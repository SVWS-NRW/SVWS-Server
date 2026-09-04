<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						:min-len="1" :max-len="20" required />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:min-len="1" :max-len="100" required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="model.proxy.kurzbezeichnung"
						:validation="() => model.getFehler('kurzbezeichnung')"
						:max-len="2" />
					<ui-select label="Folgejahrgang"
						v-model="model.folgejahrgang.value"
						:manager="folgeJahrgangManager"
						searchable />
					<ui-select label="Schulgliederung ASD-Kürzel"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungKuerzelSelectManager"
						searchable statistics />
					<ui-select label="Schulgliederung ASD-Text"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungTextSelectManager"
						searchable statistics />
					<ui-select label="Jahrgang ASD-Kürzel"
						:manager="jahrgangKuerzelSelectManager"
						v-model="model.asdJahrgang.value"
						:validation="() => model.getFehler('idJahrgang')"
						searchable statistics required :removable="false" />
					<ui-select label="Jahrgang ASD-Text"
						:manager="jahrgangTextSelectManager"
						v-model="model.asdJahrgang.value"
						:validation="() => model.getFehler('idJahrgang')"
						statistics required :removable="false" />
					<svws-ui-input-number placeholder="Anzahl der Restabschnitte"
						v-model="model.proxy.anzahlRestabschnitte"
						:validation="() => model.getFehler('anzahlRestabschnitte')"
						:min="0" :max="40" />
					<ui-select label="Bildungsstufe"
						:manager="bildungsstufeSelectManager"
						v-model="model.bildungsstufe.value" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000"
						:disabled
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar"
						:validation="() => model.getFehler('istSichtbar')">
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
	import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
	import { Bildungsstufe } from "@core/asd/types/schule/Bildungsstufe";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { JahrgaengeNeuProps } from "./JahrgaengeNeuProps";
	import { computed, ref, watch } from "vue";
	import { JahrgangModelProxy } from "~/components/schule/kataloge/jahrgaenge/modelproxy/JahrgangModelProxy";

	const props = defineProps<JahrgaengeNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const abschnittState = useAbschnittState();

	const initialData = ref<JahrgangsDaten>(Object.assign(new JahrgangsDaten(), { istSichtbar: true, sortierung: 32000, anzahlRestabschnitte: 0 }));
	const model = new JahrgangModelProxy(() => initialData.value, () => props.manager().liste.list(), abschnittState.auswahl.schuljahr);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const isLoading = ref<boolean>(false);
	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().liste.list()]);
	const folgeJahrgangManager = new SelectManager({
		options: jahrgaenge,
		optionDisplayText: v => v.bezeichnung ?? "",
		selectionDisplayText: v => v.bezeichnung ?? "",
	});

	const schulgliederungTextSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const schulgliederungKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const jahrgangTextSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const jahrgangKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Jahrgaenge.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const bildungsstufeSelectManager = new CoreTypeSelectManager({
		clazz: Bildungsstufe.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const isValid = computed<boolean>(() => model.getAlleFehler().isEmpty());

	// --- util ---
	async function addJahrgangsdaten() {
		if ((isLoading.value === true) || (!isValid.value)) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
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
