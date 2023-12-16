<template>
	<svws-ui-content-card title="Allgemein">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" :model-value="data.id" type="text" readonly />
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Beschreibung" :model-value="data.beschreibung" @change="beschreibung=>patch({beschreibung})" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Aufsichtsbereich } from "@core";

	const props = defineProps<{
		data: Aufsichtsbereich;
		patch: (data : Partial<Aufsichtsbereich>) => Promise<void>;
	}>();

</script>
