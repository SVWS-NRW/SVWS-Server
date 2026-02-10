<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
					v-model="data.bezeichnung"
					:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="100" :disabled="!hatKompetenzAdd" required />
				<svws-ui-spacing />
				<svws-ui-input-number placeholder="Sortierung"
					v-model="data.sortierung"
					:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!hatKompetenzAdd">
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addBeschaeftigungsart" :disabled="!formIsValid || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { BeschaeftigungsartenNeuProps } from "~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenNeuProps";
	import { BenutzerKompetenz, Beschaeftigungsart } from "@core";
	import { computed, ref, watch } from "vue";
	import { isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<BeschaeftigungsartenNeuProps>();
	const data = ref<Beschaeftigungsart>(Object.assign(new Beschaeftigungsart(), { sortierung: 32000, istSichtbar: true }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	// ---validate---
	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Beschaeftigungsart));
	});


	const fieldIsValid = (field: keyof Beschaeftigungsart | null): boolean => {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	function bezeichnungIsValid(value: string | null) {
		return mandatoryInputIsValid(value, 100)
			&& isUniqueInList(value, props.manager().liste.list(), "bezeichnung");
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	async function addBeschaeftigungsart() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
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
