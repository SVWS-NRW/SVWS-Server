<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Schulgliederung" class="col-span-full"
						v-model="model.schulgliederung.value"
						:manager="schulgliederungManager"
						:disabled required />
					<ui-select label="Fachklasse" class="col-span-full"
						v-model="model.fachklasse.value"
						:manager="fachklassenManager"
						:disabled="fachklasseSelectDisabled" required />
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						:max-len="100"
						:disabled="disabled || !fachklasseSelected" />
					<svws-ui-text-input placeholder="Fachklassenschlüssel"
						:model-value="model.schluesselFachklasse.value"
						readonly
						:disabled="disabled || !fachklasseSelected" />
					<svws-ui-text-input placeholder="Bezeichnung" span="full"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="100" required
						:disabled="disabled || !fachklasseSelected" />
					<svws-ui-text-input placeholder="Bezeichnung (weibliche Form)" span="full"
						v-model="model.proxy.bezeichnungWeiblich"
						:validation="() => model.getFehler('bezeichnungWeiblich')"
						:max-len="100" required
						:disabled="disabled || !fachklasseSelected" />
					<div class="flex col-span-full">
						<svws-ui-text-input placeholder="Berufsebene 1"
							v-model="model.proxy.berufsebene1"
							:validation="() => model.getFehler('berufsebene1')"
							:max-len="255" required
							:disabled="disabled || !fachklasseSelected" />
						<svws-ui-text-input placeholder="Berufsebene 2"
							v-model="model.proxy.berufsebene2"
							:validation="() => model.getFehler('berufsebene2')"
							:max-len="255" required
							:disabled="disabled || !fachklasseSelected" />
						<svws-ui-text-input placeholder="Berufsebene 3"
							v-model="model.proxy.berufsebene3"
							:validation="() => model.getFehler('berufsebene3')"
							:max-len="255" required
							:disabled="disabled || !fachklasseSelected" />
					</div>
					<ui-select label="DQR-Niveau" class="col-span-full"
						v-model="model.dqrNiveau.value"
						:manager="dqrNiveauManager"
						:validation="() => model.getFehler('idDqrNiveau')"
						:disabled="disabled || !fachklasseSelected" />
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


	import type { FachklasseKatalogEintrag } from "@core/asd/data/schule/FachklasseKatalogEintrag";
	import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
	import { DQRNiveau } from "@core/asd/types/schule/DQRNiveau";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import { FachklasseEintrag } from "@core/core/data/schule/FachklasseEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { computed, ref, watch } from "vue";
	import type { FachklassenNeuProps } from "~/components/schule/kataloge/fachklassen/FachklassenNeuProps";
	import { FachklassenModelProxy } from "./modelproxy/FachklassenModelProxy";

	const props = defineProps<FachklassenNeuProps>();
	const schuleState = useSchuleState();
	const benutzerState = useBenutzerState();

	const isLoading = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzUpdate.value);
	const initialData = ref<FachklasseEintrag>(Object.assign(new FachklasseEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const model = new FachklassenModelProxy(() => initialData.value, props.manager, schuleState.abschnitt.schuljahr);
	const formIsValid = computed(() => model.getAlleBlockierendenFehler().isEmpty());
	const fachklasseSelectDisabled = computed<boolean>(() => disabled.value || model.proxy.idSchulgliederung === null);
	const fachklasseSelected = computed<boolean>(() => model.proxy.idFachklasse !== null);

	const fachklassenManager = new SelectManager<FachklasseKatalogEintrag>({
		options: model.fachklassen,
		optionDisplayText: f => f.kuerzel,
		selectionDisplayText: f => f.kuerzel,
	});

	const schulgliederungFilter = {
		key: "isBK",
		apply: (options: List<SchulgliederungKatalogEintrag>) => {
			const filtered = new ArrayList<SchulgliederungKatalogEintrag>();
			for (const option of options) {
				if (option.istBK) {
					filtered.add(option);
				}
			}
			return filtered;
		},
	};

	const schulgliederungManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		filters: [schulgliederungFilter],
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "kuerzelText",
	});

	const dqrNiveauManager = new CoreTypeSelectManager({
		clazz: DQRNiveau.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	async function addFachklasse(): Promise<void> {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = model.proxy;
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
