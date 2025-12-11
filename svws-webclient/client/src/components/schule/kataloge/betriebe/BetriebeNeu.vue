<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Name" class="contentFocusField"
						v-model="data.name"
						:valid="() => fieldIsValid('name')" :min-len="1" :max-len="50" :disabled="!hatKompetenzAdd" required />
					<svws-ui-text-input placeholder="Namensergänzung"
						v-model="data.nameZusatz"
						:valid="() => fieldIsValid('nameZusatz')" :max-len="50" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="placeholder Betriebsart" readonly />
					<svws-ui-text-input placeholder="Branche"
						v-model="data.branche"
						:valid="() => fieldIsValid('branche')" :max-len="50" :disabled="!hatKompetenzAdd" />
					<svws-ui-textarea-input placeholder="Bemerkungen" class="h-42 max-h-42 overflow-y-auto"
						:model-value="data.bemerkungen"
						@input="value => data.bemerkungen = value"
						:valid="() => fieldIsValid('bemerkungen')" resizeable="none" :max-len="255" />
					<div>
						<svws-ui-input-wrapper :grid="1">
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="data.isAusbildungsbetrieb" :disabled="!hatKompetenzAdd">
								Ausbildungsbetrieb
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="data.isMassnahmentraeger" :disabled="!hatKompetenzAdd">
								Maßnahmenträger
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="data.belehrungNachISGErforderlich" :disabled="!hatKompetenzAdd">
								Belehrung nach ISG notwendig
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="data.bietetPraktikumsplaetzeAn" :disabled="!hatKompetenzAdd">
								Bietet Praktikumsplätze
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="data.erweitertesFuehrungszeugnisErforderlich" :disabled="!hatKompetenzAdd">
								Erweitertes Führungszeugnis notwendig
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße"
						v-model="adresse"
						:valid="() => fieldIsValid('strasse')" :max-len="50" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="placeholder Wohnort" readonly />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="data.telefon1"
						:valid="() => fieldIsValid('telefon1')" :max-len="20" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="2. Telefon" type="tel"
						v-model="data.telefon2"
						:valid="() => fieldIsValid('telefon2')" :max-len="20" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="data.eMail"
						:max-len="100" :valid="() => fieldIsValid('eMail')" :disabled="!hatKompetenzAdd" />
					<svws-ui-text-input placeholder="Fax" type="tel"
						v-model="data.fax"
						:valid="() => fieldIsValid('fax')" :max-len="20" :disabled="!hatKompetenzAdd" />
					<svws-ui-spacing :size="2" />
					<svws-ui-input-number placeholder="Sortierung"
						v-model="data.sortierung"
						:valid="() => fieldIsValid('sortierung')" :min="0" :max="32000" :disabled="!hatKompetenzAdd" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="data.isSichtbar" :disabled="!hatKompetenzAdd">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addBetriebe" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { AdressenUtils, BenutzerKompetenz, Betrieb } from "@core";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";
	import type { BetriebeNeuProps } from "~/components/schule/kataloge/betriebe/BetriebeNeuProps";

	const props = defineProps<BetriebeNeuProps>();
	const data = ref<Betrieb>(Object.assign(new Betrieb(), { isSichtbar: true, sortierung: 32000, anzahlRestabschnitte: 0 }));
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const adresse = computed({
		get: () => AdressenUtils.combineStrasse(data.value.strasse, data.value.hausnummer, data.value.hausnummerZusatz),
		set: (adresse: string | null) => {
			const vals = AdressenUtils.splitStrasse(adresse);
			data.value.strasse = vals[0];
			data.value.hausnummer = vals[1];
			data.value.hausnummerZusatz = vals[2];
		},
	});

	// --- validate ---

	function nameIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 50)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "name");
	}

	function adresseIsValid() {
		return optionalInputIsValid(data.value.strasse, 55)
			&& optionalInputIsValid(data.value.hausnummer, 10)
			&& optionalInputIsValid(data.value.hausnummerZusatz, 30);
	}

	function sortierungIsValid(sortierung: number): boolean {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof Betrieb));
	});

	const fieldIsValid = (field: keyof Betrieb): boolean => {
		switch (field) {
			case 'name':
				return nameIsValid(data.value.name);
			case 'nameZusatz':
				return optionalInputIsValid(data.value.nameZusatz, 50);
			case 'branche':
				return optionalInputIsValid(data.value.branche, 50);
			case 'bemerkungen':
				return optionalInputIsValid(data.value.branche, 255);
			case 'strasse':
				return adresseIsValid();
			case 'telefon1':
				return phoneNumberIsValid(data.value.telefon1, 20);
			case 'telefon2':
				return phoneNumberIsValid(data.value.telefon2, 20);
			case "eMail":
				return emailIsValid(data.value.eMail, 100);
			case 'fax':
				return phoneNumberIsValid(data.value.fax, 20);
			case 'sortierung':
				return sortierungIsValid(data.value.sortierung);
			default:
				return true;
		}
	};

	// --- util ---
	async function addBetriebe() {
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
