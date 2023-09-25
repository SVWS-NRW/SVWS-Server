<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>doPatch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Kürzel Schulgliederung" :model-value="data.kuerzelSchulgliederung" @change="kuerzelSchulgliederung=>doPatch({kuerzelSchulgliederung})" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="data.bezeichnung" @change="bezeichnung=>doPatch({bezeichnung})" type="text" />
			<svws-ui-select title="Folgejahrgang" v-model="inputIdFolgejahrgang" :items="inputJahrgaenge" :item-text="e => e.bezeichnung ?? ''" />
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

	import type { JahrgangsDaten, JahrgangsListeEintrag} from "@core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		data: JahrgangsDaten,
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<JahrgangsDaten>): void;
	}>()

	function doPatch(data: Partial<JahrgangsDaten>) {
		emit('patch', data);
	}

	const id: ComputedRef<number | undefined> = computed(() => {
		return props.data.id;
	});

	const inputJahrgaenge: ComputedRef<Array<JahrgangsListeEintrag>> = computed(()=>
		[...props.mapJahrgaenge.values()].filter(j => j.id !== props.data.id)
	);

	const inputIdFolgejahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => props.data.idFolgejahrgang ? props.mapJahrgaenge.get(props.data.idFolgejahrgang) : undefined,
		set: (value) => doPatch({ idFolgejahrgang: value?.id })
	});

</script>
