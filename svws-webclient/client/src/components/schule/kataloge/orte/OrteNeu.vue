<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="PLZ" class="contentFocusField"
						v-model="data.plz"
						:valid="() => fieldIsValid('plz')" :min-len="1" :max-len="10" :disabled="!hatKompetenzAdd" required />
					<svws-ui-text-input placeholder="Ortsname"
						v-model="data.ortsname"
						:valid="() => fieldIsValid('ortsname')" :min-len="1" :max-len="50" :disabled="!hatKompetenzAdd" required />
					<svws-ui-text-input placeholder="Kreis"
						v-model="data.kreis"
						:valid="() => fieldIsValid('kreis')" :max-len="3" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Land"
						v-model="data.kuerzelBundesland"
						:valid="() => fieldIsValid('kuerzelBundesland')" :max-len="2" :disabled="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
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
				<svws-ui-button @click="addOrt" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { OrteNeuProps } from "~/components/schule/kataloge/orte/OrteNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, OrtKatalogEintrag } from "@core";
	import { mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<OrteNeuProps>();
	const data = ref<OrtKatalogEintrag>(Object.assign(new OrtKatalogEintrag(), { istSichtbar: true, sortierung: 32000 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	// --- validate ---
	function ortsnameIsValid(value: string | null): boolean {
		if (!mandatoryInputIsValid(value, 50)) {
			return false;
		}

		for (const ort of props.manager().liste.list()) {
			if ((ort.plz === data.value.plz)
				&& (ort.ortsname !== null)
				&& (ort.ortsname.toLowerCase() === value.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	function sortierungIsValid(sortierung: number): sortierung is number {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof OrtKatalogEintrag));
	});

	function plzIsValid(plz: string | null): plz is string {
		return mandatoryInputIsValid(plz, 10);
	}

	const fieldIsValid = (field: keyof OrtKatalogEintrag): boolean => {
		switch (field) {
			case 'plz':
				return plzIsValid(data.value.plz);
			case 'ortsname':
				return ortsnameIsValid(data.value.ortsname);
			case 'kreis':
				return optionalInputIsValid(data.value.kreis, 3);
			case 'kuerzelBundesland':
				return optionalInputIsValid(data.value.kuerzelBundesland, 2);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	// --- util ---
	async function addOrt() {
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

