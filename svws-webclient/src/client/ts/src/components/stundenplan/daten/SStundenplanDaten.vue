<template>
	<div class="app-container">
		<s-card-stundenplan-daten :stundenplan-manager="stundenplanManager" :patch="patch" />
		<s-card-stundenplan-raeume :stundenplan-manager="stundenplanManager" :patch-raum="patchRaum" :add-raum="addRaum" :remove-raeume="removeRaeume" :import-raeume="importRaeume" />
		<s-card-stundenplan-pausenzeiten :stundenplan-manager="stundenplanManager" :patch-pausenzeit="patchPausenzeit" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" />
		<s-card-stundenplan-aufsichtsbereiche :stundenplan-manager="stundenplanManager" :patch-aufsichtsbereich="patchAufsichtsbereich" :add-aufsichtsbereich="addAufsichtsbereich" :remove-aufsichtsbereiche="removeAufsichtsbereiche" />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

</script>
