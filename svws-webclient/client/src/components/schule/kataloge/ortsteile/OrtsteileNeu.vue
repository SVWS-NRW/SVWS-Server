<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Ortsteil"
						v-model="model.proxy.ortsteil"
						:validation="() => model.getFehler('ortsteil')"
						:max-len="30" required />
					<ui-select label="Ort"
						v-model="model.ort.value"
						:validation="() => model.getFehler('idOrt')"
						:manager="ortManager"
						required :removable="false" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0" :max="32000"
						:disabled :removable="false" required />
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
				<svws-ui-button @click="addOrtsteil" :disabled="!isValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, OrtsteilKatalogEintrag } from "@core";
	import type { OrtsteileNeuProps } from "~/components/schule/kataloge/ortsteile/OrtsteileNeuProps";
	import { SelectManager, useBenutzerState } from "@ui";
	import { OrtsteilModelProxy } from "~/components/schule/kataloge/ortsteile/modelproxy/OrtsteilModelProxy";

	const props = defineProps<OrtsteileNeuProps>();
	const benutzerState = useBenutzerState();

	const isLoading = ref<boolean>(false);

	const initialData = ref<OrtsteilKatalogEintrag>(Object.assign(new OrtsteilKatalogEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const model = new OrtsteilModelProxy(() => initialData.value, props.manager);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);

	const ortManager = new SelectManager({
		options: model.filteredOrte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

	const isValid = computed<boolean>(() => model.getAlleFehler().isEmpty());

	async function addOrtsteil(): Promise<void> {
		if (isLoading.value) {
			return;
		}
		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, bezeichnungOrt, plzOrt, referenziertInAnderenTabellen, istAenderbar, ...partialData } = model.proxy;
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
