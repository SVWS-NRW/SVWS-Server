<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Förderschwerpunkt ASD-Kürzel"
						v-model="selectedFoerderschwerpunkt"
						:manager="foerderschwerpunktKuerzelManager"
						:valid="fieldIsValid('kuerzel')" statistics
						:disabled="!hatKompetenzAdd" searchable required :removable="false" />
					<ui-select label="Förderschwerpunkt ASD-Text"
						v-model="selectedFoerderschwerpunkt"
						:manager="foerderschwerpunktTextManager"
						:valid="fieldIsValid('kuerzelStatistik')" searchable :removable="false" statistics :disabled="!hatKompetenzAdd" required />
					<svws-ui-text-input placeholder="Interne Bezeichnung" span="2"
						v-model="data.kuerzel"
						:valid="() => fieldIsValid('kuerzel')" :min-len="1" :max-len="50" :disabled="!hatKompetenzAdd" required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzAdd" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>

			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addFoerderschwerpunkt" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { FoerderschwerpunkteNeuProps } from "~/components/schule/kataloge/foerderschwerpunkte/FoerderschwerpunkteNeuProps";
	import type { FoerderschwerpunktKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Foerderschwerpunkt, FoerderschwerpunktEintrag, JavaString } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<FoerderschwerpunkteNeuProps>();
	const data = ref<FoerderschwerpunktEintrag>(Object.assign(new FoerderschwerpunktEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const selectedFoerderschwerpunkt = computed<FoerderschwerpunktKatalogEintrag | null>({
		get: () => Foerderschwerpunkt.data().getEintragBySchuljahrUndSchluessel(props.schuljahr, data.value.kuerzelStatistik),
		set: (v: FoerderschwerpunktKatalogEintrag | null) => data.value.kuerzelStatistik = v?.schluessel ?? "",
	});

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof FoerderschwerpunktEintrag));
	});

	const foerderschwerpunktKuerzelManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

	const foerderschwerpunktTextManager = new CoreTypeSelectManager({
		clazz: Foerderschwerpunkt.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const fieldIsValid = (field: keyof FoerderschwerpunktEintrag): boolean => {
		switch (field) {
			case 'kuerzelStatistik':
				return foerderschwerpunktIsValid();
			case 'kuerzel':
				return kuerzelIsValid(data.value.kuerzel);
			case 'sortierung':
				return numberIsValid(data.value.sortierung, true, 0, 32000);
			default:
				return true;
		}
	};

	function foerderschwerpunktIsValid(): boolean {
		return !JavaString.isBlank(data.value.kuerzelStatistik);
	}

	function kuerzelIsValid(value: string | null): boolean {
		if (!mandatoryInputIsValid(value, 50)) {
			return false;
		}

		return isUniqueInList(value, props.manager().liste.list(), "kuerzel");
	}

	async function addFoerderschwerpunkt(): Promise<void> {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel(): Promise<void> {
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
