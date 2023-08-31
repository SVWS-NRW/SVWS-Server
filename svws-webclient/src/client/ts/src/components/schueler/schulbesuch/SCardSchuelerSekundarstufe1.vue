<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="Jahr Wechsel Sek I" :model-value="data.sekIWechsel" @blur="doPatch({ sekIWechsel: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="Erste Schulform Sek I" :model-value="data.sekIErsteSchulform" @blur="sekIErsteSchulform=>doPatch({ sekIErsteSchulform })" type="text" />
			<svws-ui-text-input placeholder="Jahr Wechsel Sek II" :model-value="data.sekIIWechsel" @blur="doPatch({ sekIIWechsel: Number($event) })" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@core";

	defineProps<{
		data: SchuelerSchulbesuchsdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerSchulbesuchsdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerSchulbesuchsdaten>) {
		emit('patch', data);
	}

</script>
