<template>
	<svws-ui-content-card v-if="auswahl" title="Allgemein" class="min-w-128">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" :model-value="auswahl.id.toString()" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" :model-value="auswahl.kuerzel" @change="kuerzel=>patch({kuerzel: kuerzel ?? undefined})" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Beschreibung" :model-value="auswahl.beschreibung" @change="beschreibung=>patch({beschreibung: beschreibung ?? undefined})" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { AufsichtsbereicheProps } from "./SAufsichtsbereicheProps";

	const props = defineProps<AufsichtsbereicheProps>();

</script>
