<template>
	<div class="page--content page--content--full">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" @selected:updated="setSelection" :import-zeitraster="importZeitraster" :schulform="schulform">
			<span class="opacity-50" v-if="!selected"><i-ri-information-line class="inline-block -mt-0.5" /> Im Zeitraster klicken, um einen Eintrag, Pausenzeit, Wochentag oder Stunde auszuwählen.</span>
			<stundenplan-detail-zeitrastereintrag :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :item="item" :stundenplan-manager="stundenplanManager" v-if="(item instanceof StundenplanZeitraster)" />
			<stundenplan-detail-pausenzeit :patch-pausenzeit="patchPausenzeit" :remove-pausenzeiten="removePausenzeiten" :item="item" :stundenplan-manager="stundenplanManager" v-if="(item instanceof StundenplanPausenzeit)" :list-lehrer="listLehrer" :add-aufsicht-und-bereich="addAufsichtUndBereich" :list-aufsichtsbereiche="listAufsichtsbereiche" />
			<stundenplan-detail-wochentag :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" :item="item" :stundenplan-manager="stundenplanManager" v-if="(item instanceof Wochentag)" />
			<stundenplan-detail-stunde :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :item="item" :stundenplan-manager="stundenplanManager" v-if="(typeof item === 'number')" />
		</stundenplan-ansicht-planung>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanZeitrasterPausenzeitProps } from "./SStundenplanZeitrasterPausenzeitProps";
	import { Wochentag, StundenplanZeitraster, StundenplanPausenzeit } from "@core";
	import { computed } from "vue";

	const props = defineProps<StundenplanZeitrasterPausenzeitProps>();

	const listAufsichtsbereiche = computed(()=> props.stundenplanManager().aufsichtsbereichGetMengeAsList());
	const item = computed(() => props.selected());

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-8;
		grid-auto-rows: 100%;
		grid-template-columns: 2fr minmax(20rem, 0.5fr);
		grid-auto-columns: max-content;
	}

</style>
