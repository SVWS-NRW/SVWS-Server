<template>
	<svws-ui-radio-group id="rgQuartalAuswahl" :row="true">
		<svws-ui-radio-option name="rgQuartalAuswahl" label="Alle" :value="0" :model-value="quartalsauswahl.value"
			@update:model-value="quartalsauswahl.value = 0" />
		<svws-ui-radio-option name="rgQuartalAuswahl" :label="halbjahr === GostHalbjahr.Q22 ? 'Halbjahr' : '1. Quartal'" :value="1"
			:model-value="quartalsauswahl.value" @update:model-value="quartalsauswahl.value = 1" />
		<svws-ui-radio-option name="rgQuartalAuswahl" :label="halbjahr === GostHalbjahr.Q22 ? 'Abitur' : '2. Quartal'" :value="2"
			:model-value="quartalsauswahl.value" @update:model-value="quartalsauswahl.value = 2" />
		<svws-ui-checkbox class="ml-8" type="toggle" v-if="zeigeAlleJahrgaenge" :disabled="setZeigeAlleJahrgaenge === undefined" :model-value="zeigeAlleJahrgaenge()" @update:model-value="setZeigeAlleJahrgaenge">Jahrgangsübergreifend</svws-ui-checkbox>
	</svws-ui-radio-group>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from '@core';
	import { type WritableComputedRef } from 'vue';

	const props = defineProps<{
		quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
		halbjahr: GostHalbjahr;
		zeigeAlleJahrgaenge?: () => boolean;
		setZeigeAlleJahrgaenge?: (value: boolean) => void;
	}>();

</script>
