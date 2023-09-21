<template>
	<svws-ui-radio-group id="rgQuartalAuswahl" :row="true">
		<svws-ui-radio-option name="rgQuartalAuswahl" label="Alle" value="0" @click="quartalsauswahl.value = 0" :model-value="quartalsauswahl.value.toString()" />
		<svws-ui-radio-option name="rgQuartalAuswahl" label="1. Quartal" value="1" @click="quartalsauswahl.value = 1" :model-value="quartalsauswahl.value.toString()" />
		<svws-ui-radio-option name="rgQuartalAuswahl" label="2. Quartal" value="2" @click="quartalsauswahl.value = 2" :model-value="quartalsauswahl.value.toString()" />
	</svws-ui-radio-group>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from 'vue';

	const props = defineProps<{
		quartalsauswahl: WritableComputedRef<0 | 1 | 2>;
	}>();

</script>
