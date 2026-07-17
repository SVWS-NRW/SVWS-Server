<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						:max-len="100" :disabled />
					<svws-ui-text-input placeholder="Bezeichnung" span="full"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="100" required :disabled />
					<ui-select label="Fachklasse" class="col-span-full"
						v-model="model.fachklasse.value"
						:manager="fachklassenManager"
						:disabled required />
					<svws-ui-spacing />
					Die Lernfelder sind zur Zeit nur in Schild3 einsehbar und editiertbar.
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
				<svws-ui-button @click="addFachklasse" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">


	import { computed, ref, watch } from "vue";
	import type { FachklassenNeuProps } from "~/components/schule/kataloge/fachklassen/FachklassenNeuProps";
	import { BenutzerKompetenz, Fachklasse, FachklasseEintrag, type FachklasseKatalogEintrag, HashSet, Schulgliederung } from "@core";
	import { FachklassenModelProxy } from "~/components/schule/kataloge/fachklassen/modelproxy/FachklassenModelProxy";
	import { SelectManager, useBenutzerState, useSchuleState } from "@ui";

	const props = defineProps<FachklassenNeuProps>();
	const schuleState = useSchuleState();
	const benutzerState = useBenutzerState();

	const isLoading = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzUpdate.value);
	const initialData = ref<FachklasseEintrag>(Object.assign(new FachklasseEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const model = new FachklassenModelProxy(() => initialData.value, props.manager);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	const fachklassen = computed<Iterable<FachklasseKatalogEintrag>>(() => {
		const bkIndizes = new HashSet<number>();
		const schulgliederungen = Schulgliederung.getEintraegeBySchuljahrAndSchulform(schuleState.abschnitt.schuljahr, schuleState.schulform);
		for (const s of schulgliederungen) {
			if (s.bkIndex !== null) {
				bkIndizes.add(s.bkIndex);
			}
		}
		return Fachklasse.getBySchuljahrAndBKIndizes(schuleState.abschnitt.schuljahr, bkIndizes);
	});

	const fachklassenManager = new SelectManager<FachklasseKatalogEintrag>({
		options: fachklassen,
		optionDisplayText: f => f.kuerzel,
		selectionDisplayText: f => f.kuerzel,
	});

	async function addFachklasse(): Promise<void> {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, schluesselSchulgliederung, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
