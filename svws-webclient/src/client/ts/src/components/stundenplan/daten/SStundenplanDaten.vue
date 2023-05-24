<template>
	<div class="app-container">
		<s-card-stundenplan-daten :stundenplan-manager="stundenplanManager" @patch="patch" />
		<s-card-stundenplan-raeume :stundenplan-manager="stundenplanManager" @patch-raum="patchRaum" />
		<s-card-stundenplan-pausenzeiten :stundenplan-manager="stundenplanManager" @patch-pausenzeit="patchPausenzeit" />
		<s-card-stundenplan-aufsichtsbereiche :stundenplan-manager="stundenplanManager" @patch-aufsichtsbereich="patchAufsichtsbereich" />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

</script>
