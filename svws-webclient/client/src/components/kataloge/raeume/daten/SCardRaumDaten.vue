<template>
	<svws-ui-content-card v-if="auswahl" title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="auswahl.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" />
			<svws-ui-input-number placeholder="Größe" :model-value="auswahl.groesse" @change="groesse => groesse && patch({groesse})" :min="1" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Beschreibung" :model-value="auswahl.beschreibung" @change="beschreibung=>patch({beschreibung})" type="text" span="full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Raum } from "@core";

	const props = defineProps<{
		auswahl: Raum | undefined;
		patch: (data : Partial<Raum>) => Promise<void>;
	}>();

</script>
