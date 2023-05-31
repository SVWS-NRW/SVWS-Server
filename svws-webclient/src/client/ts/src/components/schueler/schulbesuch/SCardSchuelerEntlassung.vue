<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Entlassung am" :model-value="data.entlassungDatum"
				@update:model-value="doPatch({ entlassungDatum: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.entlassungJahrgang"
				@update:model-value="doPatch({ entlassungJahrgang: String($event) })" type="text" />
			<svws-ui-text-input placeholder="Bemerkung / Entlassgrund" :model-value="data.entlassungGrundID"
				@update:model-value="doPatch({ entlassungGrundID: Number($event) })" span="full" />
			<svws-ui-text-input placeholder="Art des Abschlusses" :model-value="data.entlassungAbschlussartID"
				@update:model-value="doPatch({ entlassungAbschlussartID: String($event) })" span="full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core";

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
