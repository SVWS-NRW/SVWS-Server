<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="Jahr Wechsel Sek I" :model-value="data.sekIWechsel" @change="patch({ sekIWechsel: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="Erste Schulform Sek I" :model-value="data.sekIErsteSchulform" @change="sekIErsteSchulform=>patch({ sekIErsteSchulform })" type="text" />
			<svws-ui-text-input placeholder="Jahr Wechsel Sek II" :model-value="data.sekIIWechsel" @change="patch({ sekIIWechsel: Number($event) })" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@core";

	defineProps<{
		data: SchuelerSchulbesuchsdaten;
		patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
	}>();

</script>
