<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Schulangaben" v-if="schuleListeManager().hasDaten() !== undefined">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-checkbox :model-value="schuleListeManager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" focus-class-content> Ist sichtbar </svws-ui-checkbox>
				<svws-ui-input-number placeholder="Sortierung" :model-value="schuleListeManager().daten().sortierung" @change="sortierung=> sortierung && patch({ sortierung })" />
				<svws-ui-text-input class="contentFocusField" placeholder="Kürzel" :model-value="schuleListeManager().daten().kuerzel" @change="kuerzel => patch({ kuerzel })" />
				<svws-ui-text-input placeholder="Kurzbezeichnung" :model-value="schuleListeManager().daten().kurzbezeichnung" @change="kurzbezeichnung => patch({ kurzbezeichnung })" />
				<svws-ui-text-input placeholder="Schulnummer" :model-value="schuleListeManager().daten().schulnummer" @change="schulnummer => patch({ schulnummer: schulnummer ?? undefined })" />
				<svws-ui-select title="Schulform" :model-value="schuleListeManager().daten().idSchulform ? Schulform.data().getWertByID(schuleListeManager().daten().idSchulform ?? -1) : undefined"
					:items="Schulform.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" @update:model-value="schulform => patch({ idSchulform: schulform?.daten(schuljahr)?.id ?? null})" removable />
				<svws-ui-text-input placeholder="Schulname" :model-value="schuleListeManager().daten().name" @change="name => patch({name: name ?? undefined})" />
				<svws-ui-text-input placeholder="Schulleitung" :model-value="schuleListeManager().daten().schulleiter" @change="schulleiter => patch({ schulleiter })" />
				<svws-ui-text-input placeholder="Straße" :model-value="strasse" @change="patchStrasse" />
				<svws-ui-text-input placeholder="PLZ" :model-value="schuleListeManager().daten().plz" @change="plz => patch({ plz })" />
				<svws-ui-text-input placeholder="Ort" :model-value="schuleListeManager().daten().ort" @change="ort => patch({ ort })" />
				<svws-ui-text-input placeholder="Telefon" :model-value="schuleListeManager().daten().telefon" @change="telefon => patch({ telefon })" type="tel" />
				<svws-ui-text-input placeholder="Fax" :model-value="schuleListeManager().daten().fax" @change="fax => patch({ fax })" type="tel" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="schuleListeManager().daten().email" @change="email => patch({ email })" type="email" verify-email />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuleDatenProps } from "./SSchuleDatenProps";
	import { AdressenUtils, Schulform } from "@core"

	const props = defineProps<SchuleDatenProps>();
	const strasse = computed(() => AdressenUtils.combineStrasse(props.schuleListeManager().daten().strassenname ?? "", props.schuleListeManager().daten().hausnummer ?? "", props.schuleListeManager().daten().zusatzHausnummer ?? ""));

	function patchStrasse(value: string | null) {
		const vals = AdressenUtils.splitStrasse(value);
		void props.patch({ strassenname: vals[0], hausnummer: vals[1], zusatzHausnummer: vals[2] });
	}

</script>
