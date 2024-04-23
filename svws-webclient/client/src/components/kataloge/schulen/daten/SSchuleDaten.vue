<template>
	<div class="page--content">
		<svws-ui-content-card title="Schulangaben" v-if="auswahl !== undefined">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="auswahl.istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })"> Ist sichtbar </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="auswahl.istAenderbar" @update:model-value="istAenderbar => patch({ istAenderbar })"> Ist Änderbar </svws-ui-checkbox>
				<svws-ui-input-number placeholder="Sortierung" :model-value="auswahl.sortierung" @change="sortierung=> sortierung && patch({ sortierung })" />
				<svws-ui-text-input placeholder="Kürzel" :model-value="auswahl.kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel || null })" />
				<svws-ui-text-input placeholder="Kurzbezeichnung" :model-value="auswahl.kurzbezeichnung" @change="kurzbezeichnung => patch({ kurzbezeichnung: kurzbezeichnung || null })" />
				<svws-ui-text-input placeholder="Schulnummer" :model-value="auswahl.schulnummer" @change="schulnummer => patch({ schulnummer })" />
				<svws-ui-select title="Schulform" :model-value="Schulform.getByNummer(String(auswahl.schulformID || 1))" :items="Schulform.values()" :item-text="i=>i.daten.bezeichnung" @update:model-value="schulform => patch({ schulformID: schulform?.daten.id || null})" removable />
				<svws-ui-text-input placeholder="Schulname" :model-value="auswahl.name" @change="name => patch({name})" />
				<svws-ui-text-input placeholder="Schulleitung" :model-value="auswahl.schulleiter" @change="schulleiter => patch({ schulleiter: schulleiter || null })" />
				<svws-ui-text-input placeholder="Straße" :model-value="strasse" @change="patchStrasse" type="text" />
				<svws-ui-text-input placeholder="PLZ" :model-value="auswahl.plz" @change="plz => patch({ plz: plz || null })" type="text" />
				<svws-ui-text-input placeholder="Ort" :model-value="auswahl.ort" @change="ort => patch({ ort: ort || null })" type="text" />
				<svws-ui-text-input placeholder="Telefon" :model-value="auswahl.telefon" @change="telefon => patch({ telefon: telefon || null })" type="tel" />
				<svws-ui-text-input placeholder="Fax" :model-value="auswahl.fax" @change="fax => patch({ fax: fax || null })" type="tel" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="auswahl.email" @change="email => patch({ email: email || null })" type="email" verify-email />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuleDatenProps } from "./SSchuleDatenProps";
	import { AdressenUtils, Schulform } from "@core"

	const props = defineProps<SchuleDatenProps>();
	const strasse = computed(() => AdressenUtils.combineStrasse(props.auswahl?.strassenname || "", props.auswahl?.hausnummer || "", props.auswahl?.hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
		}
	}

</script>
