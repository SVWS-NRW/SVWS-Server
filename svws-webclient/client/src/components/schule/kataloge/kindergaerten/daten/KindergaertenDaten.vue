<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<!-- Allgemein -->
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="full"
						:model-value="manager().daten().bezeichnung"
						@change="patchBezeichnung"
						:valid="bezeichnungIsValid" :min-len="1" :max-len="100" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Bemerkung" span="full"
						:model-value="manager().daten().bemerkung"
						@change="patchBemerkung"
						:valid="v => optionalInputIsValid(v, 50)" :max-len="50" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						:model-value="manager().daten().tel"
						@change="patchTelefon"
						:valid="v => phoneNumberIsValid(v, 20)" :max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						:model-value="manager().daten().email"
						@change="patchEmail"
						:valid="(v) => emailIsValid(v, 40)" :max-len="40" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Adresse -->
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße" span="full"
						:model-value="strasse"
						@change="patchStrasse"
						:valid="strasseIsValid" :max-len="55" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="PLZ"
						:model-value="manager().daten().plz"
						@change="patchPlz"
						:valid="v => optionalInputIsValid(v, 10)" :max-len="10" :disabled="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Wohnort"
						:model-value="manager().daten().ort"
						@change="patchOrt"
						:valid="v => optionalInputIsValid(v, 30)" :max-len="30" :disabled="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Sonstige -->
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
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import type { KindergaertenDatenProps } from "~/components/schule/kataloge/kindergaerten/daten/KindergaertenDatenProps";
	import { AdressenUtils, BenutzerKompetenz } from "@core";
	import { computed } from "vue";
	import { optionalInputIsValid, isUniqueInList, mandatoryInputIsValid, phoneNumberIsValid, emailIsValid, numberHasDecimals, numberIsValid } from "~/util/validation/Validation";

	const props = defineProps<KindergaertenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	const strasse = computed(() => AdressenUtils.combineStrasse(
		props.manager().daten().strassenname?.trim() ?? "",
		props.manager().daten().hausNr?.trim() ?? "",
		props.manager().daten().hausNrZusatz?.trim() ?? "")
	);

	const selectedIsSichtbar = computed<boolean>({
		get: () => props.manager().daten().istSichtbar,
		set: (v: boolean) => void props.patch({ 'istSichtbar': v }),
	});

	// --- patch---

	async function patchBezeichnung(v: string | null) {
		if (bezeichnungIsValid(v)) {
			await props.patch({ bezeichnung: v?.trim() ?? "" });
		}
	}

	async function patchBemerkung(v: string | null) {
		if (optionalInputIsValid(v, 50)) {
			await props.patch({ bemerkung: v?.trim() ?? null });
		}
	}

	async function patchTelefon(v: string | null) {
		if (phoneNumberIsValid(v, 20)) {
			await props.patch({ tel: v?.trim() ?? null });
		}
	}

	async function patchEmail(v: string | null) {
		if (emailIsValid(v, 40)) {
			await props.patch({ email: v?.trim() ?? null });
		}
	}

	async function patchStrasse(v: string | null) {
		if (strasseIsValid(v)) {
			const [strassenname, hausNr, hausNrZusatz] = AdressenUtils.splitStrasse(v);
			await props.patch({ strassenname, hausNr, hausNrZusatz });
		}
	}

	async function patchPlz(v: string | null) {
		if (optionalInputIsValid(v, 10)) {
			await props.patch({ plz: v?.trim() ?? null });
		}
	}

	async function patchOrt(v: string | null) {
		if (optionalInputIsValid(v, 30)) {
			await props.patch({ ort: v?.trim() ?? null });
		}
	}

	async function patchSortierung(sortierung: number | null) {
		if (sortierungIsValid(sortierung)) {
			await props.patch({ sortierung: sortierung ?? undefined });
		}
	}

	// --- validate---

	function bezeichnungIsValid(bezeichnung: string | null): boolean {
		return mandatoryInputIsValid(bezeichnung, 100)
			&& isUniqueInList(bezeichnung, props.manager().liste.list(), "bezeichnung", "id", props.manager().auswahlID() ?? undefined);
	}

	function strasseIsValid(v: string | null) {
		const [strassenname, hausNr, hausNrZusatz] = AdressenUtils.splitStrasse(v);
		return optionalInputIsValid(strassenname, 55)
			&& optionalInputIsValid(hausNr, 10)
			&& optionalInputIsValid(hausNrZusatz, 30);
	}

	function sortierungIsValid(sortierung: number | null): boolean {
		return !numberHasDecimals(sortierung) && numberIsValid(sortierung, true, 0, 32000);
	}

</script>
