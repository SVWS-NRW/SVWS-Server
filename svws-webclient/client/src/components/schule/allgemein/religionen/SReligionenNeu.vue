<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-select label="ASD-Statistik-Religion" v-model="selectedReligion" :items="props.manager().getAvailableReligionenForCreate()" :disabled statistics
					:item-text="religionText" required :valid="fieldIsValid('kuerzel')" />
				<svws-ui-text-input v-model="data.bezeichnung" placeholder="Bezeichnung" :disabled :min-len="1" :max-len="30" required
					:valid="fieldIsValid('bezeichnung')" />
				<div v-if="!isUniqueInList(data.bezeichnung, props.manager().liste.list(), 'bezeichnung')" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet. </p>
				</div>
				<div v-if="data.bezeichnung.length > 30" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-text-input v-model="data.bezeichnungZeugnis" placeholder="Zeugnisbezeichnung" :disabled :max-len="50"
					:valid="fieldIsValid('bezeichnungZeugnis')" />
				<div v-if="bezeichnungZeugnisTooLong" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung verwendet zu viele Zeichen. </p>
				</div>
				<svws-ui-input-number placeholder="Sortierung" v-model="data.sortierung" :disabled :min="0" :max="32000" />
				<svws-ui-checkbox v-model="data.istSichtbar" :disabled>Sichtbar</svws-ui-checkbox>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addReligion()" :disabled="!formIsValid || !hatKompetenzUpdate">Speichern</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { ReligionenNeuProps } from "./SReligionenNeuProps";
	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Religion, ReligionEintrag } from "@core";
	import { isUniqueInList, mandatoryInputIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<ReligionenNeuProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);
	const schuljahr = computed<number>(() => props.manager().getSchuljahr());
	const isLoading = ref<boolean>(false);
	const data = ref<ReligionEintrag>(new ReligionEintrag());

	const selectedReligion = computed({
		get: () => Religion.data().getWertBySchluessel(data.value.kuerzel ?? ''),
		set: (v: Religion | null) => data.value.kuerzel = v?.daten(props.manager().getSchuljahr())?.schluessel ?? '',
	});

	function fieldIsValid(field: keyof ReligionEintrag | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'kuerzel':
					return data.value.kuerzel !== null;
				case 'bezeichnung':
					return bezeichnungIsValid(data.value.bezeichnung);
				case 'bezeichnungZeugnis':
					return optionalInputIsValid(data.value.bezeichnungZeugnis, 50);
				default:
					return true;
			}
		}
	}

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 30))
			return false;

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung');
	}

	const bezeichnungZeugnisTooLong = computed(() => {
		if (data.value.bezeichnungZeugnis === null)
			return false;

		return data.value.bezeichnungZeugnis.length > 50;
	});

	const formIsValid = computed(() => {
		// alle Felder auf validity prüfen
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof ReligionEintrag);
			const fieldValue = data.value[field as keyof ReligionEintrag] as string | null;
			return validateField(fieldValue);
		})
	});

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	function religionText(r: Religion) {
		return (r.daten(schuljahr.value)?.kuerzel ?? '—') + ' : ' + (r.daten(schuljahr.value)?.text ?? '—');
	}

	async function addReligion() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	watch(() => data.value, async () => {
		if (isLoading.value)
			return;

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });


</script>
