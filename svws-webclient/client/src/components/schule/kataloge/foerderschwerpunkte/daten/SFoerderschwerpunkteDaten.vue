<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" span="full" placeholder="ASD-Bezeichnung" :model-value="manager().daten().text" readonly statistics />
				<svws-ui-text-input placeholder="Kürzel" :model-value="manager().daten().kuerzel" :readonly
					@change="kuerzel => patch({ kuerzel: kuerzel ?? '' })" :max-len="50" :valid="kuerzelIsValid" />
				<svws-ui-text-input placeholder="Statistik-Kürzel" readonly :model-value="manager().daten().kuerzelStatistik" statistics />
				<svws-ui-input-number placeholder="Sortierung" :model-value="manager().daten().sortierung" :readonly :min="0" :max="32000"
					@change="value => patch({ sortierung: value === null ? 32000 : value })" />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, JavaString } from "@core";
	import { computed } from "vue";
	import type { FoerderschwerpunkteDatenProps } from "~/components/schule/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunkteDatenProps";

	const props = defineProps<FoerderschwerpunkteDatenProps>()
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

	function kuerzelIsValid(input: string | null) {
		if ((input === null) || JavaString.isBlank(input) || (input.length > 50))
			return false;

		for (const eintrag of props.manager().liste.list())
			if ((eintrag.id !== props.manager().auswahl().id) && JavaString.equalsIgnoreCase(input, eintrag.kuerzel))
				return false;

		return true;
	}

</script>
