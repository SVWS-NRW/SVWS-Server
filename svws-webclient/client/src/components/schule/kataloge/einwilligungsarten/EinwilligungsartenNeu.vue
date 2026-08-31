<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" span="2"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="250" required />
					<ui-select label="Einwilligungsschlüssel" class="col-span-full"
						v-model="model.einwilligungsschluessel.value"
						:manager="einwilligungsschluesselCoreTypeManager"
						searchable />
					<svws-ui-textarea-input placeholder="Beschreibung" span="full"
						v-model="model.proxy.beschreibung" />
					<ui-select label="Personenart" class="col-span-full"
						v-model="model.personTyp.value"
						:manager="personTypManager"
						:removable="false" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						:min="0"
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
				<svws-ui-button @click="addEinwilligungsart" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { EinwilligungsartenNeuProps } from "~/components/schule/kataloge/einwilligungsarten/EinwilligungsartenNeuProps";
	import { computed, ref, watch } from "vue";
	import type { EinwilligungsschluesselKatalogEintrag, List } from "@core";
	import { BenutzerKompetenz, ArrayList, Einwilligungsart, Einwilligungsschluessel, PersonTyp } from "@core";
	import { CoreTypeSelectManager, SelectManager, useBenutzerState, useSchuleState } from "@ui";
	import { EinwilligungsartModelProxy } from "~/components/schule/kataloge/einwilligungsarten/modelproxy/EinwilligungsartModelProxy";

	const props = defineProps<EinwilligungsartenNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const initialData = ref<Einwilligungsart>(Object.assign(new Einwilligungsart(), { istSichtbar: true, idPersonTyp: PersonTyp.SCHUELER.id }));
	const model = new EinwilligungsartModelProxy(() => initialData.value, props.manager, schuleState.abschnitt.schuljahr);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());

	const einwilligungsschluesselFilter = {
		key: "isNotUsed",
		apply: (options: List<EinwilligungsschluesselKatalogEintrag>) => {
			const filtered = new ArrayList<EinwilligungsschluesselKatalogEintrag>();
			for (const option of options) {
				if (!einwilligungsschluesselIsUsed(option)) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	function einwilligungsschluesselIsUsed(einwilligungsschluessel: EinwilligungsschluesselKatalogEintrag) {
		for (const einwilligungsart of props.manager().liste.list()) {
			if ((einwilligungsart.idPersonTyp === model.proxy.idPersonTyp)
				&& (einwilligungsart.schluessel === einwilligungsschluessel.schluessel)) {
				return true;
			}
		}
		return false;
	}

	const einwilligungsschluesselCoreTypeManager = new CoreTypeSelectManager({
		filters: [einwilligungsschluesselFilter],
		clazz: Einwilligungsschluessel.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const personTypManager = new SelectManager({
		options: [PersonTyp.SCHUELER, PersonTyp.LEHRER],
		optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	async function addEinwilligungsart(): Promise<void> {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel(): Promise<void> {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => model.proxy.idPersonTyp, async () => {
		einwilligungsschluesselCoreTypeManager.updateFilteredOptions();
	}, { immediate: true });


	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
