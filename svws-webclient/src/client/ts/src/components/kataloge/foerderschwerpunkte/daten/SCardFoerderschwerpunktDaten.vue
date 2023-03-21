<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" />
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Bezeichnung" v-model="bezeichnung" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<template #title>
			<div class="content-card--header content-card--headline text-headline-sm">
				<i-ri-bar-chart-fill class="mr-1 opacity-50" />
				<span>Statistik</span>
			</div>
		</template>
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-multi-select title="Statistik-Eintrag" v-model="statistikEintrag" :items="statistikKatalog"
									  :item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' (' + i.daten.beschreibung + ')'" required statistics />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Foerderschwerpunkt, FoerderschwerpunktEintrag } from "@svws-nrw/svws-core";

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
