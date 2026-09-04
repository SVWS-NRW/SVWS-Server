<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						v-model="data.proxy.bezeichnung"
						:validation="() => data.getFehler('bezeichnung')"
						:max-len="30" :disabled required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.proxy.sortierung"
						:validation="() => data.getFehler('sortierung')"
						:disabled :min="0"
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.proxy.istSichtbar"
						:disabled>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addBeschaeftigungsart" :disabled="!isValid || !hatKompetenzUpdate">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { BeschaeftigungsartenNeuProps } from "~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenNeuProps";
	import { computed, ref, watch } from "vue";
	import { BeschaeftigungsartModelProxy } from "~/components/schule/kataloge/beschaeftigungsarten/modelproxy/BeschaeftigungsartModelProxy";
	import { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<BeschaeftigungsartenNeuProps>();
	const benutzerState = useBenutzerState();

	const initialData = ref<Beschaeftigungsart>(Object.assign(new Beschaeftigungsart(), { istSichtbar: true, sortierung: 32000 }));
	const data = new BeschaeftigungsartModelProxy(() => initialData.value, () => props.manager().liste.list());
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);
	const isLoading = ref<boolean>(false);
	const isValid = computed<boolean>(() => data.getAlleFehler().isEmpty());


	async function addBeschaeftigungsart() {
		if (isLoading.value) {
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
