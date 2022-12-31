<template>
	<svws-ui-content-card title="Grundschulbesuch">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Einschulung" v-model="grundschuleEinschulungsjahr" type="text" />
			<svws-ui-text-input placeholder="Einschulungsart" v-model="grundschuleEinschulungsartID" />
			<svws-ui-text-input placeholder="EP-Jahre" v-model="grundschuleJahreEingangsphase" />
			<svws-ui-text-input placeholder="Übergangsempfehlung Jg. 5" v-model="grundschuleUebergangsempfehlungID" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";

	const props = defineProps<{ data: DataSchuelerSchulbesuchsdaten }>();
	
	const grundschuleEinschulungsjahr: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.grundschuleEinschulungsjahr?.valueOf(),
		set: (value) => props.data.patch({ grundschuleEinschulungsjahr: value })
	});

	const grundschuleEinschulungsartID: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.grundschuleEinschulungsartID?.valueOf(),
		set: (value) => props.data.patch({ grundschuleEinschulungsartID: value })
	});

	const grundschuleJahreEingangsphase: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.grundschuleJahreEingangsphase?.valueOf(),
		set: (value) => props.data.patch({ grundschuleJahreEingangsphase: value })
	});

	const grundschuleUebergangsempfehlungID: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.grundschuleUebergangsempfehlungID?.valueOf(),
		set: (value) => props.data.patch({ grundschuleUebergangsempfehlungID: value })
	});

</script>
