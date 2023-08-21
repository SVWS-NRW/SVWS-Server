<template>
	<div class="h-full w-full p-8 grid gap-2" style="grid-template-columns: 1fr 30rem;">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :patch-zeitraster="patchZeitraster" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" @selected:updated="selection" />
		<div>
			<stundenplan-detail-zeitrastereintrag :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof StundenplanZeitraster)" />
			<stundenplan-detail-wochentag :remove-zeitraster="removeZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(selected instanceof Wochentag)" />
			<stundenplan-detail-stunde :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :add-zeitraster="addZeitraster" :item="selected" :stundenplan-manager="stundenplanManager" v-if="(typeof selected === 'number')" />
			TODO Details zum ausgewählten Zeitraster- oder Pausenzeiten-Eintrag <br>
			TODO Bei den Pausenzeiten können hier auch direkt Aufsichten zugeordnet werden <br>
			TODO Bei den Zeitraster-Einträgen wird auch direkt eine Übersicht über die zugeordneten Unterrichte dargestellt
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanZeitrasterPausenzeitProps } from "./SStundenplanZeitrasterPausenzeitProps";
	import type { StundenplanPausenzeit} from "@core";
	import { Wochentag, StundenplanZeitraster } from "@core";
	import { ref } from "vue";

	const props = defineProps<StundenplanZeitrasterPausenzeitProps>();
	const selected = ref<Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined>();

	function selection(event: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) {
		selected.value = event;
	}
</script>
