<template>
	<svws-ui-content-card title="Basisdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
			<svws-ui-text-input placeholder="Kürzel Schulgliederung" v-model="inputKuerzelSchulgliederung" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" v-model="inputBezeichnung" type="text" />
			<svws-ui-multi-select title="Folgejahrgang" v-model="inputIdFolgejahrgang"
				:items="inputJahrgaenge" :item-text="(e: JahrgangsListeEintrag) => e.bezeichnung ?? ''" />
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
			<svws-ui-text-input placeholder="Bezeichnung in Statistik" v-model="inputKuerzelStatistik" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { JahrgangsDaten, JahrgangsListeEintrag} from "@svws-nrw/svws-core";
	import { List } from "@svws-nrw/svws-core";
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

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzel ?? undefined,
		set: (value) => doPatch({ kuerzel: value })
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.bezeichnung ?? undefined,
		set: (value) => doPatch({ bezeichnung: value })
	});

	const inputKuerzelStatistik: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzelStatistik ?? undefined,
		set: (value: string | undefined) => doPatch({ kuerzelStatistik: value })
	});

	const inputKuerzelSchulgliederung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzelSchulgliederung ?? undefined,
		set: (value) => doPatch({ kuerzelSchulgliederung: value })
	});

	const inputJahrgaenge: ComputedRef<Array<JahrgangsListeEintrag>> = computed(()=>
		[...props.mapJahrgaenge.values()].filter(j => j.id !== props.data.id)
	);

	const inputIdFolgejahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => props.data.idFolgejahrgang ? props.mapJahrgaenge.get(props.data.idFolgejahrgang) : undefined,
		set: (value) => doPatch({ idFolgejahrgang: value?.id })
	});

</script>
