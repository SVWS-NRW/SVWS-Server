<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-text-input placeholder="Bezeichnung"
						class="contentFocusField"
						span="2"
						v-model="modelProxy.proxy.bezeichnung"
						:validation="() => modelProxy.getFehler('bezeichnung')"
						:max-len="50" :disabled required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="modelProxy.proxy.sortierung"
						:validation="() => modelProxy.getFehler('sortierung')"
						:min="0" :max="32000"
						:disabled
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="modelProxy.proxy.istSichtbar" :disabled>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button :disabled="!isValid || disabled" @click="addTeilleistungsart">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>

		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { TeilleistungsartenNeuProps } from './TeilleistungsartenNeuProps';
	import { TeilleistungsartenModelProxy } from "~/components/schule/kataloge/teilleistungsarten/modelproxy/TeilleistungsartenModelProxy";
	import { Teilleistungsart } from "@core/core/data/kataloge/Teilleistungsart";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<TeilleistungsartenNeuProps>();
	const benutzerState = useBenutzerState();

	const initialData = new Teilleistungsart();
	initialData.istSichtbar = true;
	initialData.sortierung = 32000;

	const modelProxy = new TeilleistungsartenModelProxy(() => initialData, () => props.manager().liste.list());

	const isLoading = ref<boolean>(false);

	const hatKompetenzUpdate = computed<boolean>(() => {
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	});

	const disabled = computed(() => !hatKompetenzUpdate.value);

	const isValid = computed<boolean>(() => modelProxy.getAlleFehler().isEmpty());

	// util
	async function addTeilleistungsart() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = modelProxy.proxy;

		await props.add(partialData);

		isLoading.value = false;
	}

	async function cancel(): Promise<void> {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	// watchers
	watch(() => modelProxy.proxy, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
