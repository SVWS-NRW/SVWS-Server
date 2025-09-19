<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input :valid="fieldIsValid('bezeichnung')" v-model="data.bezeichnung" placeholder="Bezeichnung" :disabled
					required :min-len="1" :max-len="250" />
				<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet. </p>
				</div>
				<div v-if="data.bezeichnung.length > 250" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<div />
				<svws-ui-text-input :valid="schluesselIsValid" v-model="data.schluessel" placeholder="Schlüssel" :disabled :max-len="20" />
				<div v-if="!isUniqueInList(data.schluessel, props.manager().liste.list(), 'schluessel')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Dieser Schlüssel wird bereits verwendet. </p>
				</div>
				<div v-if="data.schluessel.length > 20" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Dieser Schlüssel verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-textarea-input v-model="data.beschreibung" placeholder="Beschreibung" class="col-span-full" :disabled />
				<svws-ui-select title="Personenart" class="col-span-full" :items="[PersonTyp.LEHRER, PersonTyp.SCHUELER]" v-model="selectedPersonTyp"
					:item-text="item => item.bezeichnung" :disabled />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addEinwilligungsart" :disabled="!formIsValid || !hatKompetenzUpdate">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuleEinwilligungsartenNeuProps } from "~/components/schule/schulbezogen/einwilligungsarten/SEinwilligungsartenNeuProps";
	import { computed, ref, watch} from "vue";
	import { BenutzerKompetenz, Einwilligungsart, PersonTyp} from "@core";
	import { isUniqueInList, mandatoryInputIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuleEinwilligungsartenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);

	const data = ref(new Einwilligungsart());
	const isLoading = ref<boolean>(false);

	const selectedPersonTyp = computed<PersonTyp>({
		get: () => PersonTyp.SCHUELER,
		set: (value) => data.value.personTyp = value.id,
	});

	function fieldIsValid(field: keyof Einwilligungsart | null) : (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				case 'schluessel':
					return schluesselIsValid(data.value.schluessel);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof Einwilligungsart);
			const fieldValue = data.value[field as keyof Einwilligungsart] as string | null;
			return validateField(fieldValue);
		})
	})

	function bezeichnungIsValid(value: string | null): boolean {
		if (!mandatoryInputIsValid(value, 250))
			return false;
		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	function schluesselIsValid(value: string | null): boolean {
		if (!optionalInputIsValid(value, 20))
			return false;
		return isUniqueInList(value, props.manager().liste.list(), 'schluessel');
	}

	async function addEinwilligungsart() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, anzahlEinwilligungen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});


</script>
