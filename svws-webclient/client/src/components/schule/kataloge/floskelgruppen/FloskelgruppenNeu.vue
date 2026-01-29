<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
					v-model="data.kuerzel"
					:valid="() => fieldIsValid('kuerzel')"
					:min-len="1" :max-len="10" :disabled required />
				<svws-ui-text-input placeholder="Bezeichnung"
					v-model="data.bezeichnung"
					:valid="() => fieldIsValid('bezeichnung')"
					:min-len="1" :max-len="50" :disabled required />
				<ui-select label="Floskelgruppenart"
					v-model="selectedFloskelgruppenart"
					:manager="floskelgruppenartManager"
					:removable="false" searchable required />
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

	import type { FloskelgruppenNeuProps } from "./FloskelgruppenNeuProps";
	import { computed, ref, watch } from "vue";
	import { Floskelgruppenart, type FloskelgruppenartKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Floskelgruppe } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<FloskelgruppenNeuProps>();
	const data = ref<Floskelgruppe>(new Floskelgruppe());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);

	const floskelgruppenartManager = new CoreTypeSelectManager({
		clazz: Floskelgruppenart.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedFloskelgruppenart = computed<FloskelgruppenartKatalogEintrag | null>({
		get: (): FloskelgruppenartKatalogEintrag | null => Floskelgruppenart.data().getWertByIDOrNull(data.value.idFloskelgruppenart ?? -1)?.daten(props.schuljahr) ?? null,
		set: (value: FloskelgruppenartKatalogEintrag | null) => data.value.idFloskelgruppenart = value?.id ?? null,
	});

	// ---validate---

	const fieldIsValid = (field: keyof Floskelgruppe): boolean => {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'kuerzel':
				return kuerzelIsValid(data.value.kuerzel);
			default:
				return true;
		}
	};

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Floskelgruppe));
	});

	function kuerzelIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 10)
			&& isUniqueInList(value, props.manager().liste.list(), "kuerzel"));
	}

	function bezeichnungIsValid(value: string | null): boolean {
		return (mandatoryInputIsValid(value, 50)
			&& isUniqueInList(value, props.manager().liste.list(), "bezeichnung"));
	}

	// --- util ---

	async function addFloskelgruppe() {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		console.log(partialData);
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
