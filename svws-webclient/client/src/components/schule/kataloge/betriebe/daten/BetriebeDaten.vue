<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<!-- Allgemein -->
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Name" class="contentFocusField"
						:model-value="manager().daten().name"
						@change="patchName"
						:valid="nameIsValid" :min-len="1" :max-len="50" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Namensergänzung"
						:model-value="manager().daten().nameZusatz"
						@change="patchNameZusatz"
						:valid="v => optionalInputIsValid(v, 50)" :max-len="50" :readonly="!hatKompetenzUpdate" />
					<ui-select label="Betriebsart"
						v-model="selectedBetriebsart"
						:manager="betriebsartenManager"
						:readonly="!hatKompetenzUpdate" searchable />
					<svws-ui-text-input placeholder="Branche"
						:model-value="manager().daten().branche"
						@change="patchBranche"
						:valid="v => optionalInputIsValid(v, 50)" :max-len="50" :readonly="!hatKompetenzUpdate" />
					<svws-ui-textarea-input placeholder="Bemerkungen"
						:model-value="manager().daten().bemerkungen"
						@change="patchBemerkungen"
						:valid="v => optionalInputIsValid(v, 255)" resizeable="none" :max-len="255" />
					<div>
						<svws-ui-input-wrapper :grid="1">
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="selectedIsAusbildungsbetrieb" :readonly="!hatKompetenzUpdate">
								Ausbildungsbetrieb
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="selectedIsMassnahmentraeger" :readonly="!hatKompetenzUpdate">
								Maßnahmenträger
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="selectedBelehrungNachISGErforderlich" :readonly="!hatKompetenzUpdate">
								Belehrung nach ISG notwendig
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="selectedBietetPraktikumsplaetzeAn" :readonly="!hatKompetenzUpdate">
								Bietet Praktikumsplätze
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="selectedErweitertesFuehrungszeugnisErforderlich" :readonly="!hatKompetenzUpdate">
								Erweitertes Führungszeugnis notwendig
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Adresse -->
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße"
						:model-value="strasse"
						@change="patchStrasse"
						:valid="adresseIsValid" :max-len="50" :readonly="!hatKompetenzUpdate" />
					<ui-select label="Wohnort"
						v-model="selectedWohnort"
						:manager="wohnortManager"
						:readonly="!hatKompetenzUpdate" searchable />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						:model-value="manager().daten().telefon1"
						@change="patchTelefon1"
						:valid="(v) => phoneNumberIsValid(v, 20)" :max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="2. Telefon" type="tel"
						:model-value="manager().daten().telefon2"
						@change="patchTelefon2"
						:valid="(v) => phoneNumberIsValid(v, 20)" :max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						:model-value="manager().daten().eMail"
						@change="patchEmail"
						:valid="(v) => emailIsValid(v, 100)" :max-len="100" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Fax" type="tel"
						:model-value="manager().daten().fax"
						@change="patchFax"
						:valid="(v) => phoneNumberIsValid(v, 20)" :max-len="20" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Ansprechpartner -->
			<betriebe-ansprechpartner :manager :add-ansprechpartner :patch-ansprechpartner :delete-ansprechpartner :hat-kompetenz-update />
			<svws-ui-spacing :size="2" />
			<!-- Ansicht & Sortierung -->
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().daten().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="selectedIsSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import type { BetriebeDatenProps } from "~/components/schule/kataloge/betriebe/daten/BetriebeDatenProps";
	import { computed } from "vue";
	import type { Betriebsart, OrtKatalogEintrag } from "@core";
	import { AdressenUtils, BenutzerKompetenz } from "@core";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";
	import { SelectManager } from "@ui";

	const props = defineProps<BetriebeDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const betriebsartenById = computed<Map<number, Betriebsart>>(() => props.manager().betriebsartenById);
	const betriebsarten = computed(() => betriebsartenById.value.values());
	const betriebsartenManager = new SelectManager({
		options: betriebsarten,
		optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	const orteById = computed<Map<number, OrtKatalogEintrag>>(() => props.manager().orteById);
	const orte = computed(() => orteById.value.values());
	const wohnortManager = new SelectManager({
		options: orte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

	const selectedBetriebsart = computed<Betriebsart | null>({
		get: () => betriebsartenById.value.get(props.manager().daten().idBetriebsart ?? -1) ?? null,
		set: (v: Betriebsart | null) => void props.patch({ 'idBetriebsart': v?.id ?? null }),
	});

	const selectedWohnort = computed<OrtKatalogEintrag | null>({
		get: () => orteById.value.get(props.manager().daten().idOrt ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => void props.patch({ 'idOrt': v?.id ?? null }),
	});

	const selectedIsAusbildungsbetrieb = computed<boolean>({
		get: () => props.manager().daten().istAusbildungsbetrieb,
		set: (v: boolean) => void props.patch({ 'istAusbildungsbetrieb': v }),
	});

	const selectedIsMassnahmentraeger = computed<boolean>({
		get: () => props.manager().daten().istMassnahmentraeger,
		set: (v: boolean) => void props.patch({ 'istMassnahmentraeger': v }),
	});

	const selectedBelehrungNachISGErforderlich = computed<boolean>({
		get: () => props.manager().daten().belehrungNachISGErforderlich,
		set: (v: boolean) => void props.patch({ 'belehrungNachISGErforderlich': v }),
	});

	const selectedBietetPraktikumsplaetzeAn = computed<boolean>({
		get: () => props.manager().daten().bietetPraktikumsplaetzeAn,
		set: (v: boolean) => void props.patch({ 'bietetPraktikumsplaetzeAn': v }),
	});

	const selectedErweitertesFuehrungszeugnisErforderlich = computed<boolean>({
		get: () => props.manager().daten().erweitertesFuehrungszeugnisErforderlich,
		set: (v: boolean) => void props.patch({ 'erweitertesFuehrungszeugnisErforderlich': v }),
	});

	const selectedIsSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
	});

	const strasse = computed(() => AdressenUtils.combineStrasse(props.manager().daten().strasse ?? "",
		props.manager().daten().hausnummer ?? "", props.manager().daten().hausnummerZusatz ?? ""));

	// patch
	async function patchName(v: string | null) {
		if (nameIsValid(v)) {
			await props.patch({ name: v?.trim() ?? null });
		}
	}

	async function patchNameZusatz(v: string | null) {
		if (optionalInputIsValid(v, 50)) {
			await props.patch({ nameZusatz: v?.trim() ?? null });
		}
	}

	async function patchBranche(v: string | null) {
		if (optionalInputIsValid(v, 50)) {
			await props.patch({ branche: v?.trim() ?? null });
		}
	}

	async function patchBemerkungen(v: string | null) {
		if (optionalInputIsValid(v, 255)) {
			await props.patch({ bemerkungen: v?.trim() ?? null });
		}
	}

	async function patchStrasse(v: string | null) {
		if (adresseIsValid(v)) {
			const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(v);
			await props.patch({ strasse, hausnummer, hausnummerZusatz });
		}
	}

	async function patchTelefon1(v: string | null) {
		if (phoneNumberIsValid(v, 20)) {
			await props.patch({ telefon1: v?.trim() ?? null });
		}
	}

	async function patchTelefon2(v: string | null) {
		if (phoneNumberIsValid(v, 20)) {
			await props.patch({ telefon2: v?.trim() ?? null });
		}
	}

	async function patchEmail(v: string | null) {
		if (emailIsValid(v, 100)) {
			await props.patch({ eMail: v?.trim() ?? null });
		}
	}

	async function patchFax(v: string | null) {
		if (phoneNumberIsValid(v, 20)) {
			await props.patch({ fax: v?.trim() ?? null });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? undefined });
		}
	}

	// validierung
	function nameIsValid(name: string | null): boolean {
		return mandatoryInputIsValid(name, 50)
			&& isUniqueInList(name, props.manager().liste.list(), "name", "id", props.manager().auswahlID() ?? undefined);
	}

	function adresseIsValid(v: string | null) {
		const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(v);
		return optionalInputIsValid(strasse, 50)
			&& optionalInputIsValid(hausnummer, 10)
			&& optionalInputIsValid(hausnummerZusatz, 30);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung) && numberIsValid(sortierung, true, 0, 32000);
	}

</script>
