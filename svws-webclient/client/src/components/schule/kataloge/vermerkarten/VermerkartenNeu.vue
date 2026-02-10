<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="2"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="30" :disabled="!hatKompetenzAdd" required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :disabled="!hatKompetenzAdd" />
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
				<svws-ui-button @click="addVermerkart" :disabled="!formIsValid || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { VermerkartenNeuProps } from "./VermerkartenNeuProps";
	import { BenutzerKompetenz, VermerkartEintrag } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<VermerkartenNeuProps>();
	const data = ref<VermerkartEintrag>(Object.assign(new VermerkartEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof VermerkartEintrag));
	});

	const fieldIsValid = (field: keyof VermerkartEintrag): boolean => {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung);
			case 'sortierung':
				return numberIsValid(data.value.sortierung, true, 0, 32000);
			default:
				return true;
		}
	};

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 30)) {
			return false;
		}
		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	async function addVermerkart() {
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
