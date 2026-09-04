<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Förderschwerpunkt ASD-Kürzel"
						v-model="model.foerderschwerpunkt.value"
						:validation="() => model.getFehler('kuerzelStatistik')"
						:manager="foerderschwerpunktKuerzelManager"
						statistics required :removable="false" />
					<ui-select label="Förderschwerpunkt ASD-Text"
						v-model="model.foerderschwerpunkt.value"
						:validation="() => model.getFehler('kuerzelStatistik')"
						:manager="foerderschwerpunktTextManager"
						searchable :removable="false" statistics required />
					<svws-ui-text-input placeholder="Interne Bezeichnung" span="2"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						:max-len="50" required />
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
					<svws-ui-checkbox v-model="model.proxy.istSichtbar">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addFoerderschwerpunkt" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { Foerderschwerpunkt } from "@core/asd/types/schule/Foerderschwerpunkt";
	import { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { computed, ref, watch } from "vue";
	import type { FoerderschwerpunkteNeuProps } from "~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteNeuProps";
	import { FoerderschwerpunkteModelProxy } from "~/components/schule/kataloge/foerderschwerpunkte/modelproxy/FoerderschwerpunkteModelProxy";

	const props = defineProps<FoerderschwerpunkteNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const isLoading = ref<boolean>(false);
	const initialData = ref<FoerderschwerpunktEintrag>(Object.assign(new FoerderschwerpunktEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const model = new FoerderschwerpunkteModelProxy(() => initialData.value, () => props.manager(), schuleState.abschnitt.schuljahr);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);

	const foerderschwerpunktKuerzelManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const foerderschwerpunktTextManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	async function addFoerderschwerpunkt(): Promise<void> {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel(): Promise<void> {
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
