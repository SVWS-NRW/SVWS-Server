<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="ID" :model-value="data.id" @change="id=>doPatch({id: Number(id)})" type="text" />
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>doPatch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.text" @change="text=>doPatch({text})" type="text" span="full" />
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
			<svws-ui-multi-select title="Statistik-Eintrag" v-model="statistikEintrag" :items="Foerderschwerpunkt.values()"
				:item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' (' + i.daten.beschreibung + ')'" required statistics />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { FoerderschwerpunktEintrag } from "@core";
	import { Foerderschwerpunkt } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: FoerderschwerpunktEintrag
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<FoerderschwerpunktEintrag>): void;
	}>()

	function doPatch(data: Partial<FoerderschwerpunktEintrag>) {
		emit('patch', data);
	}

	const statistikEintrag = computed<Foerderschwerpunkt | undefined>({
		get: () => Foerderschwerpunkt.getByKuerzel(props.data.kuerzelStatistik) ?? undefined,
		set: (value) => doPatch({ kuerzelStatistik: value?.daten.kuerzel })
	});

</script>
