<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Entlassung am" :model-value="data.entlassungDatum" @change="entlassungDatum=>doPatch({ entlassungDatum })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.entlassungJahrgang" @change="entlassungJahrgang=>doPatch({ entlassungJahrgang })" type="text" />
			<svws-ui-text-input placeholder="Bemerkung oder Entlassgrund" :model-value="data.entlassungGrundID" @change="entlassungGrundID=>doPatch({ entlassungGrundID: Number(entlassungGrundID) })" span="full" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Art des Abschlusses" :model-value="data.entlassungAbschlussartID" @change="entlassungAbschlussartID=>doPatch({ entlassungAbschlussartID })" span="full" />
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
