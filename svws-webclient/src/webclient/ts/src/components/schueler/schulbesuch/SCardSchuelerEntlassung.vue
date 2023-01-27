<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Entlassung am" :model-value="data.entlassungDatum ?? undefined"
				@update:model-value="doPatch({ entlassungDatum: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.entlassungJahrgang ?? undefined"
				@update:model-value="doPatch({ entlassungJahrgang: String($event) })" type="text" />
			<svws-ui-text-input placeholder="Bemerkung / Entlassgrund" :model-value="data.entlassungGrundID ?? undefined"
				@update:model-value="doPatch({ entlassungGrundID: Number($event) })" />
			<svws-ui-text-input placeholder="Art des Abschlusses" :model-value="data.entlassungAbschlussartID ?? undefined"
				@update:model-value="doPatch({ entlassungAbschlussartID: String($event) })" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core-ts";

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
