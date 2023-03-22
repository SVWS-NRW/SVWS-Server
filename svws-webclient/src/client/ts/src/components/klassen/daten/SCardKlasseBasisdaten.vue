<template>
	<svws-ui-content-card title="Basisdaten">
		<template #actions>
			<svws-ui-checkbox v-model="inputIstSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<svws-ui-text-input placeholder="Parallelität" v-model="parallelitaet" type="text" />
			<svws-ui-text-input placeholder="Sortierung" v-model="inputSortierung" type="text" />
			<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="mapJahrgaenge"
								  :item-text="(item: JahrgangsListeEintrag) => item.kuerzel ?? ''" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { JahrgangsListeEintrag, KlassenDaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: KlassenDaten,
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>
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
