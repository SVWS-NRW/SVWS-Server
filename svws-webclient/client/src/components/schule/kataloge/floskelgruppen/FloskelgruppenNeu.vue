<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
					v-model="model.proxy.kuerzel"
					:validation="() => model.getFehler('kuerzel')"
					:max-len="10" required />
				<svws-ui-text-input placeholder="Bezeichnung"
					v-model="model.proxy.bezeichnung"
					:validation="() => model.getFehler('bezeichnung')"
					:max-len="50" required />
				<ui-select label="Floskelgruppenart"
					v-model="model.selectedFloskelgruppenart.value"
					:validation="() => model.getFehler('idFloskelgruppenart')"
					:manager="floskelgruppenartManager"
					:removable="false" required />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addFloskelgruppe" :disabled="!formIsValid || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { Floskelgruppenart } from "@core/asd/types/schule/Floskelgruppenart";
	import { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import type { FloskelgruppenNeuProps } from "./FloskelgruppenNeuProps";
	import { computed, ref, watch } from "vue";
	import { FloskelgruppeModelProxy } from "~/components/schule/kataloge/floskelgruppen/modelproxy/FloskelgruppeModelProxy";

	const props = defineProps<FloskelgruppenNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const initialData = ref<Floskelgruppe>(new Floskelgruppe());
	const model = new FloskelgruppeModelProxy(
		() => initialData.value,
		() => props.manager().liste.list(),
		schuleState.abschnitt.schuljahr
	);
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	const floskelgruppenartManager = new CoreTypeSelectManager({
		clazz: Floskelgruppenart.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});


	// --- util ---

	async function addFloskelgruppe() {
		if (isLoading.value) {
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
