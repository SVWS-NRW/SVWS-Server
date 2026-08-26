<template>
	<svws-ui-radio-group id="rgQuartalAuswahl" :row="true">
		<svws-ui-radio-option name="rgQuartalAuswahl" label="Alle" :value="0" :model-value="state.quartal"
			@update:model-value="state.setQuartal(0)" />
		<svws-ui-radio-option name="rgQuartalAuswahl" :label="state.halbjahr === GostHalbjahr.Q22 ? 'Halbjahr' : '1. Quartal'" :value="1"
			:model-value="state.quartal" @update:model-value="state.setQuartal(1)" />
		<svws-ui-radio-option name="rgQuartalAuswahl" :label="state.halbjahr === GostHalbjahr.Q22 ? 'Abitur' : '2. Quartal'" :value="2"
			:model-value="state.quartal" @update:model-value="state.setQuartal(2)" />
		<svws-ui-checkbox class="ml-8" type="toggle" v-if="showAlleJahrgaenge" :model-value="state.zeigeAlleJahrgaenge" @update:model-value="state.setZeigeAlleJahrgaenge">Jahrgangsübergreifend</svws-ui-checkbox>
	</svws-ui-radio-group>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from '@core';
	import { useGostKlausurplanungState } from '@ui';

	defineProps<{
		showAlleJahrgaenge?: boolean;
	}>();
	const state = useGostKlausurplanungState();

</script>
