<template>
	<div class="page--content page--content--full">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :patch-zeitraster="patchZeitraster" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" @selected:updated="selection" :import-zeitraster="async()=>undefined">
			<span class="opacity-50" v-if="!selected"><i-ri-information-line class="inline-block -mt-0.5" /> Im Zeitraster klicken, um einen Eintrag, Pausenzeit, Wochentag oder Stunde auszuwählen.</span>
		</stundenplan-ansicht-planung>
	</div>
</template>

<script setup lang="ts">

	import type { Wochentag, StundenplanZeitraster, StundenplanPausenzeit } from "@core";
	import type { ZeitrasterDatenProps } from "./SZeitrasterDatenProps";
	import { ref } from "vue";

	const props = defineProps<ZeitrasterDatenProps>();
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

<script setup lang="ts">


</script>
