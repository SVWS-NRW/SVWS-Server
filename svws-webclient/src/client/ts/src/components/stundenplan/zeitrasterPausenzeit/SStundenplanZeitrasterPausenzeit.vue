<template>
	<div class="page--content page--content--full">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :patch-zeitraster="patchZeitraster" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" @selected:updated="selection">
			<span class="opacity-50" v-if="!selected"><i-ri-information-line class="inline-block -mt-0.5" /> Im Zeitraster klicken, um einen Eintrag, Pausenzeit, Wochentag oder Stunde auszuwählen.</span>
			<stundenplan-detail-zeitrastereintrag :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof StundenplanZeitraster)" />
			<stundenplan-detail-pausenzeit :patch-pausenzeit="patchPausenzeit" :remove-pausenzeiten="removePausenzeiten" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof StundenplanPausenzeit)" />
			<stundenplan-detail-wochentag :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof Wochentag)" />
			<stundenplan-detail-stunde :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(typeof selected === 'number')" />
		</stundenplan-ansicht-planung>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanZeitrasterPausenzeitProps } from "./SStundenplanZeitrasterPausenzeitProps";
	import { StundenplanPausenzeit} from "@core";
	import { Wochentag, StundenplanZeitraster } from "@core";
	import { ref } from "vue";

	const props = defineProps<StundenplanZeitrasterPausenzeitProps>();
	const selected = ref<Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined>();

	function selection(event: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) {
		selected.value = event;
	}
</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: 2fr minmax(20rem, 0.5fr);
}
</style>
