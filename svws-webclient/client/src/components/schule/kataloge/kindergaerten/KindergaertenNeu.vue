<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<!-- Allgemein -->
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="full"
						v-model="data.bezeichnung"
						:valid="() => fieldIsValid('bezeichnung')" :min-len="1" :max-len="100" required :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Bemerkung" span="full"
						v-model="data.bemerkung"
						:valid="() => fieldIsValid('bemerkung')" :max-len="50" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="data.tel"
						:valid="() => fieldIsValid('tel')" :max-len="20" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="data.email"
						:valid="() => fieldIsValid('email')" :max-len="40" :disabled="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Adresse -->
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße" span="full"
						v-model="strasse"
						:valid="() => fieldIsValid('strassenname')" :max-len="55" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="PLZ"
						v-model="data.plz"
						:valid="() => fieldIsValid('plz')" :max-len="10" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Wohnort"
						v-model="data.ort"
						:valid="() => fieldIsValid('ort')" :max-len="30" :disabled="!hatKompetenzAdd" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Sonstige -->
			<svws-ui-input-wrapper :grid="2">
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
				<svws-ui-button @click="addKindergarten" :disabled="!hatKompetenzAdd || !formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { AdressenUtils, BenutzerKompetenz, Kindergarten } from "@core";
	import { computed, ref, watch } from "vue";
	import type { KindergaertenNeuProps } from "~/components/schule/kataloge/kindergaerten/KindergaertenNeuProps";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";

	const props = defineProps<KindergaertenNeuProps>();
	const data = ref<Kindergarten>(Object.assign(new Kindergarten(), { istSichtbar: true, sortierung: 32000 }));
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isLoading = ref<boolean>(false);

	const strasse = computed({
		get: () => AdressenUtils.combineStrasse(data.value.strassenname, data.value.hausNr, data.value.hausNrZusatz),
		set: (adresse: string) => {
			const [strassenname, hausNr, hausNrZusatz] = AdressenUtils.splitStrasse(adresse);
			data.value.strassenname = strassenname;
			data.value.hausNr = hausNr;
			data.value.hausNrZusatz = hausNrZusatz;
		},
	});

	// --- validate ---

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Kindergarten));
	});

	const fieldIsValid = (field: keyof Kindergarten): boolean => {
		switch (field) {
			case 'bezeichnung':
				return bezeichnungIsValid(data.value.bezeichnung, 100);
			case 'strassenname':
				return strasseIsValid();
			case 'ort':
				return optionalInputIsValid(data.value.ort, 30);
			case 'plz':
				return optionalInputIsValid(data.value.plz, 10);
			case 'tel':
				return phoneNumberIsValid(data.value.tel, 20);
			case 'email':
				return emailIsValid(data.value.email, 40);
			case 'bemerkung':
				return optionalInputIsValid(data.value.bemerkung, 50);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	function bezeichnungIsValid(bezeichnung: string | null, maxLength: number): boolean {
		return mandatoryInputIsValid(bezeichnung, maxLength)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung");
	}

	function strasseIsValid() {
		return optionalInputIsValid(data.value.strassenname, 55) &&
			optionalInputIsValid(data.value.hausNr, 10) &&
			optionalInputIsValid(data.value.hausNrZusatz, 30);
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	// --- util ---

	async function addKindergarten() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ...partialData } = data.value;
		await props.add(partialData);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.goToDefaultView(null);
	}

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
