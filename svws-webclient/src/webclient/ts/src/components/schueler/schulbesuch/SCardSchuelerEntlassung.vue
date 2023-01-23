<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Entlassung am" v-model="entlassungDatum" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" v-model="entlassungJahrgang" type="date" />
			<svws-ui-text-input placeholder="Bemerkung / Entlassgrund" v-model="entlassungGrundID" />
			<svws-ui-text-input placeholder="Art des Abschlusses" v-model="entlassungAbschlussartID" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";

	const props = defineProps<{ data: DataSchuelerSchulbesuchsdaten }>();

	const entlassungDatum: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.entlassungDatum?.toString(),
		set: (value) => void props.data.patch({ entlassungDatum:  value })
	});

	const entlassungJahrgang: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.entlassungJahrgang?.toString(),
		set: (value) => void props.data.patch({ entlassungJahrgang:  value })
	});

	const entlassungGrundID: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.entlassungGrundID?.valueOf(),
		set: (value) => void props.data.patch({ entlassungGrundID: value })
	});

	const entlassungAbschlussartID: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.entlassungAbschlussartID?.toString(),
		set: (value) => void props.data.patch({ entlassungAbschlussartID: value })
	});

</script>
