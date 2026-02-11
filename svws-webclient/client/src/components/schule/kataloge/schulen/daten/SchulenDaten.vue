<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Schulangaben" v-if="manager().hasDaten()">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Schulform"
						class="contentFocusField"
						v-model="selectedSchulform"
						:manager="schulformSelectManager"
						:readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Statistik-Schulnummer"
						:model-value="manager().auswahl().schulnummerStatistik"
						readonly statistics />
					<svws-ui-text-input placeholder="Kürzel"
						:model-value="manager().auswahl().kuerzel"
						@change="patchKuerzel"
						:valid="kuerzelIsValid" :max-len="10" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Schulname"
						:model-value="manager().auswahl().name"
						@change="patchSchulname"
						:valid="schulnameIsValid" :min-len="1" :max-len="120" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						:model-value="manager().auswahl().kurzbezeichnung"
						@change="patchKurzbezeichnung"
						:valid="kurzbezeichnungIsValid" :min-len="1" :max-len="40" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Schulleitung"
						:model-value="manager().auswahl().schulleiter"
						@change="patchSchulleiter"
						:valid="schulleiterIsValid" :max-len="40" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Straße"
						:model-value="strasse"
						@change="patchStrasse"
						:valid="strasseIsValid" :max-len="55" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="PLZ" :model-value="manager().auswahl().plz"
						@change="patchPlz"
						:valid="plzIsValid" :max-len="10" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Ort"
						:model-value="manager().auswahl().ort"
						@change="patchOrt"
						:valid="ortIsValid" :max-len="50" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						:model-value="manager().auswahl().telefon"
						@change="patchTelefon"
						:valid="v => phoneNumberIsValid(v, 20)" :max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Fax" type="tel"
						:model-value="manager().auswahl().fax"
						@change="patchFax"
						:valid="v => phoneNumberIsValid(v, 20)" :max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						:model-value="manager().auswahl().email"
						@change="patchEmail"
						:valid="v => emailIsValid(v, 40)" :max-len="40" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						:model-value="manager().auswahl().sortierung"
						@change="patchSortierung"
						:valid="sortierungIsValid" :min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchulenDatenProps } from "./SchulenDatenProps";
	import { computed } from "vue";
	import type { SchulformKatalogEintrag } from "@core";
	import { AdressenUtils, BenutzerKompetenz, Schulform } from "@core";
	import { emailIsValid, isUniqueInList, mandatoryInputIsValid, numberHasDecimals, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";
	import { CoreTypeSelectManager } from "@ui";

	const props = defineProps<SchulenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const strasse = computed(
		() => AdressenUtils.combineStrasse(
			props.manager().daten().strassenname ?? "",
			props.manager().daten().hausnummer ?? "",
			props.manager().daten().zusatzHausnummer ?? ""
		));

	const schulformSelectManager = new CoreTypeSelectManager({
		clazz: Schulform.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const selectedSchulform = computed<SchulformKatalogEintrag | null>({
		get: () => Schulform.data().getEintragByID(props.manager().daten().idSchulform ?? -1),
		set: (value: SchulformKatalogEintrag | null) => void props.patch({ idSchulform: value?.id ?? null }),
	});

	const istSichtbar = computed<boolean>({
		get: () => props.manager().auswahl().istSichtbar,
		set: (value: boolean) => void patchSichtbar(value),
	});

	async function patchKuerzel(kuerzel: string | null) {
		if (kuerzelIsValid(kuerzel)) {
			await props.patch({ kuerzel: kuerzel?.trim() ?? null });
		}
	}

	async function patchSchulname(schulname: string | null) {
		if (schulnameIsValid(schulname)) {
			await props.patch({ name: schulname.trim() });
		}
	}

	async function patchKurzbezeichnung(kurzbezeichnung: string | null) {
		if (kurzbezeichnungIsValid(kurzbezeichnung)) {
			await props.patch({ kurzbezeichnung: kurzbezeichnung.trim() });
		}
	}

	async function patchSchulleiter(schulleiter: string | null) {
		if (schulleiterIsValid(schulleiter)) {
			await props.patch({ schulleiter: schulleiter?.trim() ?? null });
		}
	}

	async function patchStrasse(value: string | null) {
		if (strasseIsValid(value)) {
			const [strassenname, hausnummer, zusatzHausnummer] = AdressenUtils.splitStrasse(value);
			await props.patch({ strassenname, hausnummer, zusatzHausnummer });
		}
	}

	async function patchPlz(plz: string | null) {
		if (plzIsValid(plz)) {
			await props.patch({ plz: plz?.trim() ?? null });
		}
	}

	async function patchOrt(ort: string | null) {
		if (ortIsValid(ort)) {
			await props.patch({ ort: ort?.trim() ?? null });
		}
	}

	async function patchTelefon(telefon: string | null) {
		if (phoneNumberIsValid(telefon, 20)) {
			await props.patch({ telefon: telefon?.trim() ?? null });
		}
	}

	async function patchFax(fax: string | null) {
		if (phoneNumberIsValid(fax, 20)) {
			await props.patch({ fax: fax?.trim() ?? null });
		}
	}

	async function patchEmail(email: string | null) {
		if (emailIsValid(email, 40)) {
			await props.patch({ email: email?.trim() ?? null });
		}
	}

	async function patchSortierung(sortierung: number | null): Promise<void> {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung });
		}
	}

	async function patchSichtbar(value: boolean): Promise<void> {
		await props.patch({ istSichtbar: value });
	}

	// ---validate---

	function kuerzelIsValid(kuerzel: string | null): boolean {
		return optionalInputIsValid(kuerzel, 10)
			&& isUniqueInList(kuerzel, props.manager().liste.list(), "kuerzel", "id", props.manager().auswahlID() ?? undefined);
	}

	function schulnameIsValid(schulname: string | null): schulname is string {
		return mandatoryInputIsValid(schulname, 120)
			&& isUniqueInList(schulname, props.manager().liste.list(), "name", "id", props.manager().auswahlID() ?? undefined);
	}

	function kurzbezeichnungIsValid(kurzbezeichnung: string | null): kurzbezeichnung is string {
		return mandatoryInputIsValid(kurzbezeichnung, 40)
			&& isUniqueInList(kurzbezeichnung, props.manager().liste.list(), "kurzbezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function schulleiterIsValid(schulleiter: string | null): boolean {
		return optionalInputIsValid(schulleiter, 40);
	}

	function strasseIsValid(value: string | null) {
		const [strassenname, hausnummer, zusatzHausnummer] = AdressenUtils.splitStrasse(value);
		return optionalInputIsValid(strassenname, 55)
			&& optionalInputIsValid(hausnummer, 10)
			&& optionalInputIsValid(zusatzHausnummer, 30);
	}

	function plzIsValid(plz: string | null): boolean {
		return optionalInputIsValid(plz, 10);
	}

	function ortIsValid(ort: string | null): boolean {
		return optionalInputIsValid(ort, 50);
	}

	function sortierungIsValid(sortierung: number | null): sortierung is number {
		return !numberHasDecimals(sortierung)
			&& numberIsValid(sortierung, true, 0, 32000);
	}

</script>
