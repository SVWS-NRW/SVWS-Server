<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="Jahr Wechsel Sek I" :model-value="data.sekIWechsel" @change="doPatch({ sekIWechsel: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="Erste Schulform Sek I" :model-value="data.sekIErsteSchulform" @change="sekIErsteSchulform=>doPatch({ sekIErsteSchulform })" type="text" />
			<svws-ui-text-input placeholder="Jahr Wechsel Sek II" :model-value="data.sekIIWechsel" @change="doPatch({ sekIIWechsel: Number($event) })" type="text" />
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
