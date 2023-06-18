<template>
	<div class="page--content">
		<s-card-betriebe-basis-daten :daten="data" @patch="doPatch" :map-beschaeftigungsarten="mapBeschaeftigungsarten" />
		<s-card-betriebe-kontakt-daten :daten="data" @patch="doPatch" :map-orte="mapOrte" />
		<s-card-betriebe-bemerkungen :daten="data" @patch="doPatch" />
		<s-card-betriebe-anprechpartner :daten="data" :map-ansprechpartner="mapAnsprechpartner"
			:add-betrieb-ansprechpartner="addBetriebAnsprechpartner" :remove-betrieb-ansprechpartner="removeBetriebAnsprechpartner" :patch-betrieb-anpsrechpartner="patchBetriebAnpsrechpartner" /> 
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';
	import type { BetriebeDatenProps } from './SBetriebeProps';

	const props = defineProps<BetriebeDatenProps>()

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)
</script>
