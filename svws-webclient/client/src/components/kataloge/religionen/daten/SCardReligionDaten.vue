<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" span="full" />
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.text" @change="text=>patch({text})" type="text" />
			<svws-ui-text-input placeholder="Zeugnisbezeichnung" :model-value="data.textZeugnis" @change="textZeugnis=>patch({textZeugnis})" type="text" />
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
			<svws-ui-select title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="Religion.values()" :item-text="(i: Religion) => i.daten.kuerzel" required />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ReligionEintrag } from "@core";
	import { Religion } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: ReligionEintrag;
		patch: (data : Partial<ReligionEintrag>) => Promise<void>;
	}>();

	const inputStatistikKuerzel = computed<Religion | undefined>({
		get: () => Religion.getByKuerzel(props.data.kuerzel || null) || undefined,
		set: (value) => void props.patch({ kuerzel: value?.daten.kuerzel || null })
	});

</script>
