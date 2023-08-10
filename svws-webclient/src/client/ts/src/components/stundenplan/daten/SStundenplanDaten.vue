<template>
	<div class="page--content grid-flow-dense">
		<s-card-stundenplan-daten :stundenplan-manager="stundenplanManager" :patch="patch" :list-jahrgaenge="listJahrgaenge" :add-jahrgang="addJahrgang" :remove-jahrgang="removeJahrgang" />
		<s-card-stundenplan-raeume :stundenplan-manager="stundenplanManager" :patch-raum="patchRaum" :add-raum="addRaum" :remove-raeume="removeRaeume" :import-raeume="importRaeume" :list-raeume="listRaeume" />
		<s-card-stundenplan-aufsichtsbereiche :stundenplan-manager="stundenplanManager" :patch-aufsichtsbereich="patchAufsichtsbereich" :add-aufsichtsbereich="addAufsichtsbereich" :remove-aufsichtsbereiche="removeAufsichtsbereiche" :list-aufsichtsbereiche="listAufsichtsbereiche" :import-aufsichtsbereiche="importAufsichtsbereiche" />
		<s-card-stundenplan-pausenzeiten :stundenplan-manager="stundenplanManager" :patch-pausenzeit="patchPausenzeit" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" :list-pausenzeiten="listPausenzeiten" :import-pausenzeiten="importPausenzeiten" />
	</div>
	<div class="page--content">
		<s-card-stundenplan-zeitraster :stundenplan-manager="stundenplanManager" :add-zeitraster="addZeitraster" :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :import-zeitraster="importZeitraster" />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

</script>

<style lang="postcss" scoped>
    .page--content {
    }
</style>
