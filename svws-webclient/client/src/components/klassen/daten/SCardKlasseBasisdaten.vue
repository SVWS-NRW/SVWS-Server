<template>
	<svws-ui-content-card title="Allgemein">
		<template #actions>
			<svws-ui-checkbox v-model="inputIstSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel=>doPatch({kuerzel})" type="text" />
			<svws-ui-text-input placeholder="Parallelität" :model-value="data.parallelitaet" @change="parallelitaet=>doPatch({parallelitaet})" type="text" />
			<svws-ui-text-input placeholder="Sortierung" :model-value="data.sortierung" @change="sortierung=>doPatch({sortierung: Number(sortierung)})" type="text" />
			<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="mapJahrgaenge" :item-text="(item: JahrgangsListeEintrag) => item.kuerzel ?? ''" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { JahrgangsListeEintrag, KlassenDaten } from "@core";

	const props = defineProps<{
		data: KlassenDaten,
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<KlassenDaten>): void;
	}>()

	function doPatch(data: Partial<KlassenDaten>) {
		emit('patch', data);
	}

	const jahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => ((props.data === undefined) || (props.data.idJahrgang === null)) ? undefined : props.mapJahrgaenge.get(props.data.idJahrgang),
		set: (value) => doPatch({ idJahrgang: value?.id })
	});

	const inputIstSichtbar: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.data.istSichtbar,
		set: (value) => doPatch({ istSichtbar: value })
	});

</script>
