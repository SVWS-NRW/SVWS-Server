<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Ortsteil"
						v-model="data.ortsteil"
						:valid="() => fieldIsValid('ortsteil')" :min-len="1" :max-len="30" required :disabled="!hatKompetenzUpdate" />
					<ui-select label="Ort"
						v-model="selectedOrt"
						:manager="ortManager"
						:disabled="!hatKompetenzUpdate" searchable required :removable="false" />
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
				<svws-ui-button @click="addOrtsteil" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, type OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";
	import type { OrtsteileNeuProps } from "~/components/schule/kataloge/ortsteile/OrtsteileNeuProps";
	import { SelectManager } from "@ui";

	const props = defineProps<OrtsteileNeuProps>();
	const data = ref<OrtsteilKatalogEintrag>(Object.assign(new OrtsteilKatalogEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const orteById = computed<Map<number, OrtKatalogEintrag>>(() => props.manager().orteById);
	const orte = computed(() => orteById.value.values());
	const ortManager = new SelectManager({
		options: orte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

	const selectedOrt = computed<OrtKatalogEintrag | null>({
		get: () => orteById.value.get(data.value.ort_id ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => setOrtData(v),
	});

	// notwendig, damit plz und ortsname nach hinzufuegen in der Auswahlliste angezeigt werden
	function setOrtData(v: OrtKatalogEintrag | null) {
		const ort = orteById.value.get(v?.id ?? -1) ?? null;
		if (ort !== null) {
			data.value.ort_id = ort.id;
			data.value.bezeichnungOrt = ort.ortsname;
			data.value.plzOrt = ort.plz;
		}
	}

	function ortsteilIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 30)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "ortsteil");
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof OrtsteilKatalogEintrag));
	});

	const fieldIsValid = (field: keyof OrtsteilKatalogEintrag): boolean => {
		switch (field) {
			case 'ortsteil':
				return ortsteilIsValid(data.value.ortsteil);
			case 'sortierung':
				return numberIsValid(data.value.sortierung, true, 0, 32000);
			case 'ort_id':
				return data.value.ort_id !== null;
			default:
				return true;
		}
	};

	async function addOrtsteil(): Promise<void> {
		if (isLoading.value) {
			return;
		}
		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, referenziertInAnderenTabellen, istAenderbar, ...partialData } = data.value;
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
