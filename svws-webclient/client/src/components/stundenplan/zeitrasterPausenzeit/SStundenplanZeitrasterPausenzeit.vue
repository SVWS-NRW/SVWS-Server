<template>
	<div class="page page-flex-row">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :add-zeitraster :remove-zeitraster :set-selection :import-zeitraster :set-settings-defaults :schulform :selected>
			<span class="opacity-50" v-if="!selected"><span class="icon i-ri-information-line -mt-0.5" /> Im Zeitraster klicken, um einen Eintrag, Pausenzeit, Wochentag oder Stunde auszuwählen.</span>
			<stundenplan-detail-zeitrastereintrag :patch-zeitraster :remove-zeitraster :selected :stundenplan-manager v-if="(selected instanceof StundenplanZeitraster)" />
			<stundenplan-detail-pausenzeit :patch-pausenzeit :remove-pausenzeiten :selected :stundenplan-manager v-if="(selected instanceof StundenplanPausenzeit)" :list-lehrer />
			<stundenplan-detail-wochentag :remove-zeitraster :add-zeitraster :remove-pausenzeiten :selected :stundenplan-manager v-if="(selected instanceof Wochentag)" />
			<stundenplan-detail-stunde :set-selection :patch-zeitraster :remove-zeitraster :add-zeitraster :selected :stundenplan-manager v-if="(typeof selected === 'number')" />
		</stundenplan-ansicht-planung>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanZeitrasterPausenzeitProps } from "./SStundenplanZeitrasterPausenzeitProps";
	import { Wochentag, StundenplanZeitraster, StundenplanPausenzeit } from "@core";

	const props = defineProps<StundenplanZeitrasterPausenzeitProps>();

	// const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

</script>
