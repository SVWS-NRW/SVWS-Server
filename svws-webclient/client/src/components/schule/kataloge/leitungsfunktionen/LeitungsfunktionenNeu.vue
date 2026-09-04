<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung" span="2"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
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

				<svws-ui-button :disabled="!formIsValid" @click="addLeitungsfunktion">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>

		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { LeitungsfunktionenNeuProps } from "~/components/schule/kataloge/leitungsfunktionen/LeitungsfunktionenNeuProps";
	import { computed, ref, watch } from "vue";
	import { LeitungsfunktionenModelProxy } from "~/components/schule/kataloge/leitungsfunktionen/modelproxy/LeitungsfunktionenModelProxy";
	import { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<LeitungsfunktionenNeuProps>();
	const benutzerState = useBenutzerState();

	const initialData = ref<Leitungsfunktion>(Object.assign(new Leitungsfunktion(), { istSichtbar: true }));
	const model = new LeitungsfunktionenModelProxy(() => initialData.value, props.manager);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);

	const isLoading = ref<boolean>(false);

	// util
	async function addLeitungsfunktion() {
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

	// watchers
	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });
</script>
