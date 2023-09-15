<template>
	<div class="page--content">
		<s-card-betriebe-basis-daten :daten="data" @patch="patch" :map-beschaeftigungsarten="mapBeschaeftigungsarten">
			<s-card-betriebe-bemerkungen :daten="data" @patch="patch" />
		</s-card-betriebe-basis-daten>
		<s-card-betriebe-kontakt-daten :daten="data" @patch="patch" :map-orte="mapOrte" />
		<s-card-betriebe-anprechpartner :daten="data" :map-ansprechpartner="mapAnsprechpartner"
			:add-betrieb-ansprechpartner="addBetriebAnsprechpartner" :remove-betrieb-ansprechpartner="removeBetriebAnsprechpartner" :patch-betrieb-anpsrechpartner="patchBetriebAnpsrechpartner" />
	</div>
</template>

<script setup lang="ts">
	import type { BetriebeDatenProps } from './SBetriebeProps';

	const props = defineProps<BetriebeDatenProps>()

</script>
