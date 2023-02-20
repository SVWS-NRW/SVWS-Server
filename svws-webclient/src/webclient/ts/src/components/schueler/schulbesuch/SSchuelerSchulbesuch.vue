<template>
	<div class="app-container">
		<s-card-schueler-herkunft :data="data" @patch="doPatch" />
		<s-card-schueler-entlassung :data="data" @patch="doPatch" />
		<s-card-schueler-wechsel :data="data" @patch="doPatch" />
		<s-card-schueler-grundschulbesuch :data="data" @patch="doPatch" />
		<s-card-schueler-sekundarstufe1 :data="data" @patch="doPatch" />
		<s-card-schueler-merkmale :data="data" />
		<s-card-schueler-schulen :data="data" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';
	import { SchuelerSchulbesuchProps } from './SSchuelerSchulbesuchProps';

	const props = defineProps<SchuelerSchulbesuchProps>();
	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
