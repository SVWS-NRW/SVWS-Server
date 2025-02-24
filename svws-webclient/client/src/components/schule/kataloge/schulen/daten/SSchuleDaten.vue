<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Schulangaben" v-if="schuleListeManager().hasDaten()">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="schuleListeManager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" focus-class-content>
					Ist sichtbar
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Sortierung" :model-value="schuleListeManager().daten().sortierung"
					@change="sortierung => sortierung && patch({ sortierung })" />
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :model-value="schuleListeManager().daten().kuerzel" :valid="kuerzelIsValid"
					@change="patchKuerzel" :max-len="10" />
				<svws-ui-text-input placeholder="Kurzbezeichnung" :model-value="schuleListeManager().daten().kurzbezeichnung" required :max-len="40"
					@change="patchKurzBezeichnung" :valid="v => mandatoryInputIsValid(v, 40)" />
				<svws-ui-text-input placeholder="Schulnummer" :model-value="schuleListeManager().daten().schulnummer" :valid="schulnummerIsValid"
					@change="patchSchulnummer" required :min-len="6" :max-len="6" />
				<svws-ui-select title="Schulform" :items="Schulform.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'"
					:model-value="schuleListeManager().daten().idSchulform ? Schulform.data().getWertByID(schuleListeManager().daten().idSchulform ?? -1) : undefined"
					@update:model-value="schulform => patch({ idSchulform: schulform?.daten(schuljahr)?.id ?? null})" removable />
				<svws-ui-text-input placeholder="Schulname" :model-value="schuleListeManager().daten().name" :valid="v => mandatoryInputIsValid(v, 120)"
					@change="patchSchulname" required :max-len="120" />
				<svws-ui-text-input placeholder="Schulleitung" :model-value="schuleListeManager().daten().schulleiter" :valid="v => optionalInputIsValid(v, 40)"
					@change="patchSchulleiter" :max-len="40" />
				<svws-ui-text-input placeholder="Straße" :model-value="strasse" :max-len="55" :valid="v => adresseIsValid(AdressenUtils.splitStrasse(v))"
					@change="patchStrasse" />
				<svws-ui-text-input placeholder="PLZ" :model-value="schuleListeManager().daten().plz" :valid="v => optionalNumberIsValid(v, 10)"
					@change="patchPlz" :max-len="10" />
				<svws-ui-text-input placeholder="Ort" :model-value="schuleListeManager().daten().ort" :valid="v => optionalInputIsValid(v, 50)"
					@change="patchOrt" :max-len="50" />
				<svws-ui-text-input placeholder="Telefon" :model-value="schuleListeManager().daten().telefon" :valid="phoneNumberIsValid"
					@change="patchTelefon" type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Fax" :model-value="schuleListeManager().daten().fax" :valid="phoneNumberIsValid"
					@change="patchFax" type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="schuleListeManager().daten().email" :valid="emailIsValid"
					@change="patchEmail" type="email" :max-len="40" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuleDatenProps } from "./SSchuleDatenProps";
	import { AdressenUtils, Schulform, JavaObject, JavaString } from "@core"

	const props = defineProps<SchuleDatenProps>();
	const strasse = computed(() => AdressenUtils.combineStrasse(props.schuleListeManager().daten().strassenname ?? "",
		props.schuleListeManager().daten().hausnummer ?? "", props.schuleListeManager().daten().zusatzHausnummer ?? ""));

	//validation logic

	function patchKuerzel(kuerzel : string | null) {
		if (kuerzelIsValid(kuerzel))
			void props.patch({ kuerzel })
	}

	function kuerzelIsValid(kuerzel : string | null) {
		if ((kuerzel === null) || (JavaString.isBlank(kuerzel)))
			return true;
		if (kuerzel.length > 10)
			return false;
		for (const schuleintrag of props.schuleListeManager().liste.list()) {
			if ((schuleintrag.id !== props.schuleListeManager().auswahl().id) && JavaObject.equalsTranspiler(schuleintrag.kuerzel, kuerzel))
				return false;
		}
		return true;
	}

	function patchSchulnummer(schulnummer : string | null) {
		if (schulnummerIsValid(schulnummer))
			void props.patch({ schulnummer: schulnummer ?? undefined });
	}

	function schulnummerIsValid(schulnummer : string | null) {
		if ((schulnummer === null) || (JavaString.isBlank(schulnummer)) || (!/^\d{6}$/.test(schulnummer)))
			return false;
		for (const schuleintrag of props.schuleListeManager().liste.list()) {
			if ((schuleintrag.id !== props.schuleListeManager().auswahl().id) && JavaObject.equalsTranspiler(schuleintrag.schulnummer, schulnummer))
				return false;
		}
		return true;
	}

	function mandatoryInputIsValid(name: string | null, maxLength : number) {
		return ((name !== null) && !JavaString.isBlank(name) && (name.length <= maxLength));
	}

	function patchSchulname(name : string | null) {
		if (mandatoryInputIsValid(name, 120) && (name !== null))
			void props.patch({name});
	}

	function patchKurzBezeichnung(kurzbezeichnung : string | null ) {
		if (mandatoryInputIsValid(kurzbezeichnung, 40) && (kurzbezeichnung !== null))
			void props.patch({ kurzbezeichnung });
	}

	function optionalInputIsValid(input : string | null, maxLength : number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return true;
		return input.length <= maxLength;
	}

	function optionalNumberIsValid(input: string | null, maxLength : number) {
		if (input === null || JavaString.isBlank(input))
			return true;
		if (input.length > maxLength)
			return false;
		return /^\d+$/.test(input);
	}

	function patchSchulleiter(schulleiter : string | null) {
		if (optionalInputIsValid(schulleiter, 40))
			void props.patch({ schulleiter });
	}

	function adresseIsValid(vals : Array<string>) {
		return optionalInputIsValid(vals[0], 55) &&
			optionalInputIsValid(vals[1], 10) &&
			optionalInputIsValid(vals[2], 30);
	}

	function patchStrasse(value: string | null) {
		const vals = AdressenUtils.splitStrasse(value);
		if (adresseIsValid(vals))
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], zusatzHausnummer: vals[2] });
	}

	function patchPlz(plz : string | null) {
		if (optionalNumberIsValid(plz, 10))
			void props.patch({plz});
	}

	function patchOrt(ort : string | null) {
		if (optionalInputIsValid(ort, 50))
			void props.patch({ort});
	}

	function phoneNumberIsValid(input: string | null) {
		if ((input === null) || (JavaString.isBlank(input)))
			return true;
		// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
		return /^\+?\d+([-/]?\d+)*$/.test(input) && input.length <= 20;
	}

	function patchTelefon(telefon : string | null) {
		if (phoneNumberIsValid(telefon))
			void props.patch({telefon});
	}

	function patchFax(fax : string | null) {
		if (phoneNumberIsValid(fax))
			void props.patch({fax});
	}

	function emailIsValid(value: string | null) {
		if ((value === null) || JavaString.isBlank(value))
			return true;
		if (value.length > 40)
			return false;
		return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
	}

	function patchEmail(email : string | null) {
		if (emailIsValid(email))
			void props.patch({email});
	}

</script>
