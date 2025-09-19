<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" :max-len="50" :min-len="1" v-model="data.bezeichnung" :disabled :valid="fieldIsValid('bezeichnung')" />
				<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet. </p>
				</div>
				<div v-if="bezeichnungIsTooLong" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled :min="0" :max="32000" />
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled>
					Sichtbar
				</svws-ui-checkbox>
				<div class="mt-7 flex flex-row gap-4 justify end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="add" :disabled="!formIsValid || !hatKompetenzAdd">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { SportbefreiungenNeuProps } from "~/components/schule/schulbezogen/sportbefreiungen/SSportbefreiungenNeuProps";
	import { BenutzerKompetenz, Sportbefreiung } from "@core";
	import { computed, ref, watch } from "vue";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SportbefreiungenNeuProps>();
	const data = ref<Sportbefreiung>(Object.assign(new Sportbefreiung(), { istSichtbar: true, sortierung: 1 }))
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzAdd.value);
	const bezeichnungIsTooLong = computed(() => (data.value.bezeichnung?.length ?? 0) > 50);

	function fieldIsValid(field: keyof Sportbefreiung | null) : (v: string | null) => boolean {
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
			const validateField = fieldIsValid(field as keyof Sportbefreiung);
			const fieldValue = data.value[field as keyof Sportbefreiung] as string | null;
			return validateField(fieldValue);
		})
	})

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 50))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), "bezeichnung");
	}

	async function add() {
		if (isLoading.value)
			return;

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, ...partialData } = data.value;
		await props.addSportbefreiung(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

</script>
