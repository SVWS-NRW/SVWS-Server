<template>
	<svws-ui-content-card v-if="auswahl" title="Allgemein">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" :model-value="auswahl.id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" :model-value="auswahl.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Beschreibung" :model-value="auswahl.beschreibung" @change="beschreibung=>patch({beschreibung})" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Aufsichtsbereich } from "@core";

	const props = defineProps<{
		auswahl: Aufsichtsbereich | undefined;
		patch: (data : Partial<Aufsichtsbereich>) => Promise<void>;
	}>();

</script>
