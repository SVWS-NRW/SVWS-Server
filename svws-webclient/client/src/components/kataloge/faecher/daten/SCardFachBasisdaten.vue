<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({ kuerzel })" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.bezeichnung" @change="bezeichnung => patch({ bezeichnung })" type="text" />
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
			<svws-ui-text-input placeholder="Bezeichnung in Statistik" :model-value="data.kuerzelStatistik" @change="kuerzelStatistik => patch({ kuerzelStatistik })" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { FachDaten } from "@core";

	const props = defineProps<{
		data: FachDaten;
		patch: (data : Partial<FachDaten>) => Promise<void>;
	}>();

</script>
