<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField"
					:model-value="manager().auswahl().bezeichnung"
					@change="patchBezeichnung"
					:valid="bezeichnungIsValid" :min-len="1" :max-len="255" required :readonly="!hatKompetenzUpdate" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { LernplattformenDatenProps } from "~/components/schule/kataloge/lernplattformen/daten/LernplattformenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";
	import { isUniqueInList, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<LernplattformenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));

	async function patchBezeichnung(bezeichnung: string | null) {
		if (bezeichnungIsValid(bezeichnung)) {
			await props.patch({ bezeichnung: bezeichnung ?? '' });
		}
	}

	function bezeichnungIsValid(value: string | null) {
		if (!mandatoryInputIsValid(value, 255)) {
			return false;
		}

		return isUniqueInList(value, props.manager().liste.list(), 'bezeichnung', 'id', props.manager().auswahlID() ?? undefined);
	}

</script>
