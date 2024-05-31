<template>
	<div class="page--content page--content--full">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :add-zeitraster :remove-zeitraster @selected:updated="setSelection" :import-zeitraster :schulform>
			<span class="opacity-50" v-if="!selected"><span class="icon i-ri-information-line inline-block -mt-0.5" /> Im Zeitraster klicken, um einen Eintrag, Pausenzeit, Wochentag oder Stunde auszuwählen.</span>
			<stundenplan-detail-zeitrastereintrag :patch-zeitraster :remove-zeitraster :item="item" :stundenplan-manager v-if="(item instanceof StundenplanZeitraster)" />
			<stundenplan-detail-pausenzeit :patch-pausenzeit :remove-pausenzeiten :item :stundenplan-manager v-if="(item instanceof StundenplanPausenzeit)" :list-lehrer :list-aufsichtsbereiche />
			<stundenplan-detail-wochentag :remove-zeitraster :add-zeitraster :add-pausenzeit :remove-pausenzeiten :item="item" :stundenplan-manager v-if="(item instanceof Wochentag)" />
			<stundenplan-detail-stunde :patch-zeitraster :remove-zeitraster :add-zeitraster :item="item" :stundenplan-manager v-if="(typeof item === 'number')" />
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
