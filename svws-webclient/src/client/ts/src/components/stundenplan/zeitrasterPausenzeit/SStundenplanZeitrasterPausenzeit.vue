<template>
	<stundenplan-ansicht-planung :manager="stundenplanManager" :patch-zeitraster="patchZeitraster" :add-zeitraster="addZeitraster" :remove-zeitraster="removeZeitraster" />
	<!-- <s-card-stundenplan-pausenzeiten :stundenplan-manager="stundenplanManager" :patch-pausenzeit="patchPausenzeit" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" :list-pausenzeiten="listPausenzeiten" :import-pausenzeiten="importPausenzeiten" /> -->
</template>

<script setup lang="ts">

	import type { StundenplanZeitrasterPausenzeitProps } from "./SStundenplanZeitrasterPausenzeitProps";

	const props = defineProps<StundenplanZeitrasterPausenzeitProps>();

</script>
