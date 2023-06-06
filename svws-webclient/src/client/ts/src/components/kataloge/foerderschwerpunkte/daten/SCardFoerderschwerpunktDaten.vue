<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" />
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" v-model="bezeichnung" type="text" span="full" />
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
			<svws-ui-multi-select title="Statistik-Eintrag" v-model="statistikEintrag" :items="statistikKatalog"
				:item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' (' + i.daten.beschreibung + ')'" required statistics />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";

	import type { FoerderschwerpunktEintrag } from "@core";
	import { Foerderschwerpunkt } from "@core";

	const props = defineProps<{
		data: FoerderschwerpunktEintrag
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<FoerderschwerpunktEintrag>): void;
	}>()

	function doPatch(data: Partial<FoerderschwerpunktEintrag>) {
		emit('patch', data);
	}

	const id: ComputedRef<number | undefined> = computed(() => props.data.id);

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzel,
		set: (value) => doPatch({ kuerzel: value })
	});

	const bezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.text,
		set: (value) => doPatch({ text: value })
	});

	const statistikKatalog: ComputedRef<Foerderschwerpunkt[]> = computed(() => Foerderschwerpunkt.values());
	const statistikEintrag: WritableComputedRef<Foerderschwerpunkt | undefined> = computed({
		get: () => Foerderschwerpunkt.getByKuerzel(props.data.kuerzelStatistik) ?? undefined,
		set: (value) => doPatch({ kuerzelStatistik: value?.daten.kuerzel })
	});

</script>
