<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="ID" v-model="data.id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" />
			<svws-ui-input-number placeholder="Größe" :model-value="data.groesse" @change="groesse => groesse && patch({groesse})" :min="1" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Beschreibung" :model-value="data.beschreibung" @change="beschreibung=>patch({beschreibung})" type="text" span="full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Raum } from "@core";

	const props = defineProps<{
		data: Raum;
		patch: (data : Partial<Raum>) => Promise<void>;
	}>();

</script>
