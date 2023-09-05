<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>doPatch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.bezeichnung" @change="bezeichnung=>doPatch({bezeichnung})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<template #title>
			<div class="content-card--header content-card--headline text-headline-sm">
				<i-ri-bar-chart-2-line class="mr-1 opacity-50" />
				<span>Statistik</span>
			</div>
		</template>
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="Bezeichnung in Statistik" :model-value="data.kuerzelStatistik" @change="kuerzelStatistik=>doPatch({kuerzelStatistik})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { FachDaten } from "@core";

	const props = defineProps<{
		data: FachDaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<FachDaten>): void;
	}>()

	function doPatch(data: Partial<FachDaten>) {
		emit('patch', data);
	}

</script>
