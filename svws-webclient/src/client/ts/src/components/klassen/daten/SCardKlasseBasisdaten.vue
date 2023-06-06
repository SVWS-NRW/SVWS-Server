<template>
	<svws-ui-content-card title="Allgemein" class="col-span-full">
		<template #actions>
			<svws-ui-checkbox v-model="inputIstSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<svws-ui-text-input placeholder="Parallelität" v-model="parallelitaet" type="text" />
			<svws-ui-text-input placeholder="Sortierung" v-model="inputSortierung" type="text" />
			<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="mapJahrgaenge"
				:item-text="(item: JahrgangsListeEintrag) => item.kuerzel ?? ''" />
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

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzel ?? undefined,
		set: (value) => doPatch({ kuerzel: value })
	});

	const parallelitaet: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.parallelitaet ?? undefined,
		set: (value) => doPatch({ parallelitaet: value })
	});

	const jahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => ((props.data === undefined) || (props.data.idJahrgang === null)) ? undefined : props.mapJahrgaenge.get(props.data.idJahrgang),
		set: (value) => doPatch({ idJahrgang: value?.id })
	});

	const inputIstSichtbar: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.data.istSichtbar,
		set: (value) => doPatch({ istSichtbar: value })
	});

	const inputSortierung: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.sortierung,
		set: (value) => doPatch({ sortierung: value })
	});

</script>
