<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="PLZ" class="contentFocusField"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						:max-len="10" :disabled required />
					<svws-ui-text-input placeholder="Ortsname"
						v-model="model.proxy.ortsname"
						:validation="() => model.getFehler('ortsname')"
						:max-len="50" :disabled required />
					<svws-ui-text-input placeholder="Kreis"
						v-model="model.proxy.kreis"
						:validation="() => model.getFehler('kreis')"
						:max-len="3" :disabled />
					<ui-select label="Land"
						:manager="laenderManager"
						v-model="model.bundesland.value"
						:disabled />
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
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addOrt" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { OrteNeuProps } from "~/components/schule/kataloge/orte/OrteNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Laender, OrtKatalogEintrag } from "@core";
	import { OrtModelProxy } from "~/components/schule/kataloge/orte/modelproxy/OrtModelProxy";
	import { CoreTypeSelectManager, useBenutzerState, useSchuleState } from "@ui";

	const props = defineProps<OrteNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const initialData = ref<OrtKatalogEintrag>(Object.assign(new OrtKatalogEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const model = new OrtModelProxy(() => initialData.value, () => props.manager().liste.list());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const schuljahr = schuleState.schuljahr;

	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	const laenderManager = new CoreTypeSelectManager({
		clazz: Laender.class,
		schuljahr: schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	// --- util ---
	async function addOrt() {
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

