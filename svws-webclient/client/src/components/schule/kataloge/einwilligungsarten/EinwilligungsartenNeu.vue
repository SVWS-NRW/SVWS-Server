<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" span="2"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="250" required :disabled="!hatKompetenzUpdate" />
					<ui-select label="Einwilligungsschlüssel" class="col-span-full"
						v-model="selectedEinwilligungsschluessel"
						:manager="einwilligungsschluesselCoreTypeManager"
						searchable :disabled="!hatKompetenzUpdate" />
					<svws-ui-textarea-input placeholder="Beschreibung" span="full"
						v-model="data.beschreibung"
						@change="value => data.beschreibung = value"
						:disabled="!hatKompetenzUpdate" />
					<ui-select label="Personenart" class="col-span-full"
						v-model="selectedPersonTyp"
						:manager="personTypManager"
						:disabled="!hatKompetenzUpdate" :removable="false" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!hatKompetenzUpdate">
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
	import { ArrayList, BenutzerKompetenz, Einwilligungsart, Einwilligungsschluessel, PersonTyp } from "@core";
	import type { EinwilligungsschluesselKatalogEintrag, List } from "@core";
	import { mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<EinwilligungsartenNeuProps>();
	const data = ref<Einwilligungsart>(Object.assign(new Einwilligungsart(), { istSichtbar: true, idPersonTyp: PersonTyp.SCHUELER.id }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const selectedEinwilligungsschluessel = computed<EinwilligungsschluesselKatalogEintrag | null>({
		get: () => Einwilligungsschluessel.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.schluessel ?? ''),
		set: (v: EinwilligungsschluesselKatalogEintrag | null) => data.value.schluessel = v?.schluessel ?? null,
	});

	const personTypManager = new SelectManager({	options: [PersonTyp.SCHUELER, PersonTyp.LEHRER], optionDisplayText: v => v.bezeichnung, selectionDisplayText: v => v.bezeichnung });
	const selectedPersonTyp = computed<PersonTyp>({
		get: () => PersonTyp.getByID(data.value.idPersonTyp) ?? PersonTyp.SCHUELER,
		set: (value: PersonTyp) => data.value.idPersonTyp = value.id,
	});

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Einwilligungsart));
	});

	const fieldIsValid = (field: keyof Einwilligungsart): boolean => {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'sortierung':
				return numberIsValid(data.value.sortierung, true, 0, 32000);
			case 'idPersonTyp':
				return idPersonTypeIsValid(data.value.idPersonTyp);
			default:
				return true;
		}
	};

	function bezeichnungIsValid(value: string | null): boolean {
		if (!mandatoryInputIsValid(value, 250)) {
			return false;
		}
		if (data.value.idPersonTyp === -1) {
			return true;
		}
		for (const einwilligungsart of props.manager().liste.list()) {
			if (einwilligungsart.idPersonTyp === data.value.idPersonTyp && einwilligungsart.bezeichnung.toLowerCase() === value.toLowerCase()) {
				return false;
			}
		}
		return true;
	}

	function idPersonTypeIsValid(idPersonTyp: number) {
		return (idPersonTyp === PersonTyp.LEHRER.id) || (idPersonTyp === PersonTyp.SCHUELER.id);
	}


	async function addEinwilligungsart(): Promise<void> {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel(): Promise<void> {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

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
			if ((einwilligungsart.idPersonTyp === data.value.idPersonTyp)
				&& (einwilligungsart.schluessel === einwilligungsschluessel.schluessel)) {
				return true;
			}
		}
		return false;
	}

	const einwilligungsschluesselCoreTypeManager = new CoreTypeSelectManager({
		filters: [einwilligungsschluesselFilter],
		clazz: Einwilligungsschluessel.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	watch(() => data.value.idPersonTyp, async () => {
		einwilligungsschluesselCoreTypeManager.updateFilteredOptions();
	}, { immediate: true });

</script>
