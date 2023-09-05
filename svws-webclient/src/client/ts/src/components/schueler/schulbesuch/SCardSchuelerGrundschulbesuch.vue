<template>
	<svws-ui-content-card title="Grundschulbesuch">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Einschulung" :model-value="data.grundschuleEinschulungsjahr" @change="doPatch({ grundschuleEinschulungsjahr: Number($event) })" type="text" />
			<svws-ui-text-input placeholder="Einschulungsart" :model-value="data.grundschuleEinschulungsartID" @change="doPatch({ grundschuleEinschulungsartID: Number($event) })" />
			<svws-ui-text-input placeholder="EP-Jahre" :model-value="data.grundschuleJahreEingangsphase" @change="doPatch({ grundschuleJahreEingangsphase: Number($event) })" />
			<svws-ui-text-input placeholder="Übergangsempfehlung Jg. 5" :model-value="data.grundschuleUebergangsempfehlungID" @change="doPatch({ grundschuleUebergangsempfehlungID: Number($event) })" type="text" />
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
