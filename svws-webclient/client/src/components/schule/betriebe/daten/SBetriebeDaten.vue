<template>
	<div class="page--content">
		<s-card-betriebe-basis-daten :daten :patch :map-beschaeftigungsarten>
			<s-card-betriebe-bemerkungen :daten :patch />
		</s-card-betriebe-basis-daten>
		<s-card-betriebe-kontakt-daten :daten :patch :map-orte />
		<s-card-betriebe-anprechpartner :daten :map-ansprechpartner :add-betrieb-ansprechpartner :remove-betrieb-ansprechpartner :patch-betrieb-anpsrechpartner />
	</div>
</template>

<script setup lang="ts">
	import type { BetriebeDatenProps } from './SBetriebeDatenProps';

	const props = defineProps<BetriebeDatenProps>()

</script>
