<template>
	<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" v-model="aufnehmdendSchulnummer" type="text" />
			<svws-ui-text-input placeholder="Wechseldatum" v-model="aufnehmdendWechseldatum" type="date" />
			<svws-ui-checkbox text="Aufnahme bestätigt" v-model="aufnehmdendBestaetigt" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";

	const props = defineProps<{ data: DataSchuelerSchulbesuchsdaten }>();

	const aufnehmdendSchulnummer: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.aufnehmdendSchulnummer?.toString(),
		set: (value) => props.data.patch({ aufnehmdendSchulnummer: value })
	});

	const aufnehmdendWechseldatum: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.aufnehmdendWechseldatum?.toString(),
		set: (value) => props.data.patch({ aufnehmdendWechseldatum: value })
	});

	const aufnehmdendBestaetigt: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.data.daten?.aufnehmdendBestaetigt?.valueOf(),
		set: (value) => props.data.patch({ aufnehmdendBestaetigt: value })
	});

</script>
