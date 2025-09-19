<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<svws-ui-text-input :valid="fieldIsValid('bezeichnung')" v-model="data.bezeichnung" placeholder="Bezeichnung"
						required :min-len="1" :max-len="30" :disabled />
					<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Diese Bezeichnung wird bereits verwendet. </p>
					</div>
					<div v-if="data.bezeichnung.length > 30" class="flex my-auto">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
					</div>
					<svws-ui-checkbox v-model="data.istSichtbar" :disabled="!bezeichnungIsValid || !hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
						<svws-ui-button @click="addVermerkart" :disabled="!formIsValid || !hatKompetenzUpdate">Speichern</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";
	import type { SchuleVermerkartenNeuProps } from "./SVermerkartenNeuProps";
	import { BenutzerKompetenz, VermerkartEintrag } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuleVermerkartenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);

	const data = ref(new VermerkartEintrag());
	const isLoading = ref<boolean>(false);

	function fieldIsValid(field: keyof VermerkartEintrag | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof VermerkartEintrag);
			const fieldValue = data.value[field as keyof VermerkartEintrag] as string | null;
			return validateField(fieldValue);
		})
	})

	function bezeichnungIsValid(value: string | null): boolean {
		if (!mandatoryInputIsValid(value, 30))
			return false;
		return isUniqueInList(value, props.manager().liste.list(), "bezeichnung");
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addVermerkart() {
		if (isLoading.value === true)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, anzahlVermerke, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
