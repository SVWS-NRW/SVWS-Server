<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Jahr Wechsel Sek I" v-model="sekIWechsel" type="text" />
			<svws-ui-text-input placeholder="Erste Schulform Sek I" v-model="sekIErsteSchulform" type="text" />
			<svws-ui-text-input placeholder="Jahr Wechsel Sek II" v-model="sekIIWechsel" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";

	const props = defineProps<{ data: DataSchuelerSchulbesuchsdaten }>();
	
	const sekIWechsel: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.sekIWechsel?.valueOf(),
		set: (value) => props.data.patch({ sekIWechsel: value })
	});

	const sekIErsteSchulform: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.sekIErsteSchulform?.toString(),
		set: (value) => props.data.patch({ sekIErsteSchulform: value })
	});

	const sekIIWechsel: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.sekIIWechsel?.valueOf(),
		set: (value) => props.data.patch({ sekIIWechsel: value })
	});

</script>
