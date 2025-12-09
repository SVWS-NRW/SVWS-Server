<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Schulangaben" v-if="manager().hasDaten()">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })"
					focus-class-content :readonly>
					Ist sichtbar
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Sortierung" :model-value="manager().daten().sortierung" :readonly
					@change="sortierung => sortierung && patch({ sortierung })" />
				<svws-ui-select title="Schulform" :items="Schulform.data().getListBySchuljahrAndSchulform(schuljahr, schulform)" :item-text="i => i.daten(schuljahr)?.text ?? '—'" :readonly
					:model-value="manager().daten().idSchulform ? Schulform.data().getWertByID(manager().daten().idSchulform ?? -1) : undefined"
					@update:model-value="value => patch({ idSchulform: value?.daten(schuljahr)?.id ?? null})" removable />
				<svws-ui-text-input placeholder="Statistik-Schulnummer" :model-value="manager().daten().schulnummerStatistik" readonly statistics />
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :model-value="manager().daten().kuerzel"
					@change="v => patch({ kuerzel: v?.trim() ?? undefined })" :max-len="10" :readonly />
				<svws-ui-text-input placeholder="Schulname" :model-value="manager().daten().name"
					@change="v => patch({ name : v?.trim() ?? undefined })" required :max-len="120" :readonly />
				<svws-ui-text-input placeholder="Kurzbezeichnung" :model-value="manager().daten().kurzbezeichnung" required :max-len="40" :readonly
					@change="v => patch({ kurzbezeichnung: v?.trim() ?? undefined })" />
				<svws-ui-text-input placeholder="Schulleitung" :model-value="manager().daten().schulleiter"
					@change="v => patch({ schulleiter: v?.trim() ?? undefined })" :max-len="40" :readonly />
				<svws-ui-text-input placeholder="Straße" :model-value="strasse" :max-len="55" @change="patchStrasse" :readonly />
				<svws-ui-text-input placeholder="PLZ" :model-value="manager().daten().plz"
					@change="v => patch({ plz: v?.trim() ?? undefined })" :max-len="10" :readonly />
				<svws-ui-text-input placeholder="Ort" :model-value="manager().daten().ort" @change="v => patch({ ort: v?.trim()?? undefined })"
					:max-len="50" :readonly />
				<svws-ui-text-input placeholder="Telefon" :model-value="manager().daten().telefon" @change="v => patch({ telefon: v?.trim() ?? undefined })"
					type="tel" :max-len="20" :readonly />
				<svws-ui-text-input placeholder="Fax" :model-value="manager().daten().fax" @change="v => patch({ fax: v?.trim() ?? undefined })"
					type="tel" :max-len="20" :readonly />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="manager().daten().email" @change="v => patch({ email: v?.trim() ?? undefined })"
					type="email" :max-len="40" :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenProps } from "./SSchuleDatenProps";
	import { computed } from "vue";
	import { AdressenUtils, BenutzerKompetenz, Schulform } from "@core";
	import { optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuleDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);
	const strasse = computed(() => AdressenUtils.combineStrasse(props.manager().daten().strassenname ?? "",
		props.manager().daten().hausnummer ?? "", props.manager().daten().zusatzHausnummer ?? ""));

	function adresseIsValid(vals: Array<string>) {
		return optionalInputIsValid(vals[0], 55) &&
			optionalInputIsValid(vals[1], 10) &&
			optionalInputIsValid(vals[2], 30);
	}

	async function patchStrasse(value: string | null) {
		const vals = AdressenUtils.splitStrasse(value);
		if (adresseIsValid(vals))
			await props.patch({ strassenname: vals[0], hausnummer: vals[1], zusatzHausnummer: vals[2] });
	}

</script>
