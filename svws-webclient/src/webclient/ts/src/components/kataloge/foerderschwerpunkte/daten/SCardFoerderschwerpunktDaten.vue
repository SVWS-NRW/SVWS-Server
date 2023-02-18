<template>
	<svws-ui-content-card title="Daten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="ID" v-model="id" type="text" />
			<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" v-model="bezeichnung" type="text" />
			<svws-ui-multi-select title="Statistik-Eintrag" v-model="statistikEintrag" :items="statistikKatalog"
				:item-text="(i: Foerderschwerpunkt) => i.daten.kuerzel + ' (' + i.daten.beschreibung + ')'" required statistics />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Foerderschwerpunkt, FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";

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
