<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper>
			<svws-ui-input-number placeholder="ID" :model-value="data.id" @change="id=> id && patch({id})" />
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>patch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.text" @change="text=>patch({text})" type="text" span="full" />
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
			<svws-ui-select title="Statistik-Eintrag" v-model="statistikEintrag" :items="Foerderschwerpunkt.values()"
				:item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' (' + i.daten.beschreibung + ')'" required statistics />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunktEintrag } from "@core";
	import { Foerderschwerpunkt } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: FoerderschwerpunktEintrag;
		patch: (data : Partial<FoerderschwerpunktEintrag>) => Promise<void>;
	}>();

	const statistikEintrag = computed<Foerderschwerpunkt | undefined>({
		get: () => Foerderschwerpunkt.getByKuerzel(props.data.kuerzelStatistik) ?? undefined,
		set: (value) => void props.patch({ kuerzelStatistik: value?.daten.kuerzel })
	});

</script>
