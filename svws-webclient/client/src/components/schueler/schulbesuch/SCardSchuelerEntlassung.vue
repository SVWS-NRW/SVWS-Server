<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Entlassung am" :model-value="data.entlassungDatum" @change="entlassungDatum=>patch({ entlassungDatum })" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.entlassungJahrgang" @change="entlassungJahrgang=>patch({ entlassungJahrgang })" type="text" />
			<svws-ui-text-input placeholder="Bemerkung oder Entlassgrund" :model-value="data.entlassungGrundID" @change="entlassungGrundID=>patch({ entlassungGrundID: Number(entlassungGrundID) })" span="full" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Art des Abschlusses" :model-value="data.entlassungAbschlussartID" @change="entlassungAbschlussartID=>patch({ entlassungAbschlussartID })" span="full" />
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
