<template>
	<div class="h-full w-full p-8 grid gap-2" style="grid-template-columns: 1fr 30rem;">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :patch-zeitraster="patchZeitraster" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" @selected:updated="selection" />
		<div>
			<stundenplan-detail-zeitrastereintrag :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof StundenplanZeitraster)" />
			<stundenplan-detail-pausenzeit :patch-pausenzeit="patchPausenzeit" :remove-pausenzeiten="removePausenzeiten" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof StundenplanPausenzeit)" />
			<stundenplan-detail-wochentag :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof Wochentag)" />
			<stundenplan-detail-stunde :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(typeof selected === 'number')" />
		</div>
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
