<template>
	<div class="h-full w-full p-8 grid gap-2" style="grid-template-columns: 1fr 30rem;">
		<stundenplan-ansicht-planung :manager="stundenplanManager" :patch-zeitraster="patchZeitraster" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" />
		<div>
			TODO Details zum ausgewählten Zeitraster- oder Pausenzeiten-Eintrag
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanZeitrasterPausenzeitProps } from "./SStundenplanZeitrasterPausenzeitProps";

	const props = defineProps<StundenplanZeitrasterPausenzeitProps>();

</script>
