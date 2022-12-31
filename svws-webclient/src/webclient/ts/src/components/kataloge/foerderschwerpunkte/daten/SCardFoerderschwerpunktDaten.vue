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

	import { Foerderschwerpunkt } from "@svws-nrw/svws-core-ts";
	import { DataFoerderschwerpunkt } from "~/apps/kataloge/foerderschwerpunkt/DataFoerderschwerpunkt";

	const props = defineProps<{ data: DataFoerderschwerpunkt }>();

	const id: ComputedRef<number | undefined> = computed(() => props.data.daten?.id);

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzel?.toString(),
		set: (value) => props.data.patch({ kuerzel: value })
	});

	const bezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.text?.toString(),
		set: (value) => props.data.patch({ text: value })
	});

	const statistikKatalog: ComputedRef<Foerderschwerpunkt[] | undefined> = computed(() => Foerderschwerpunkt.values());
	const statistikEintrag: WritableComputedRef<Foerderschwerpunkt | undefined> = computed({
		get: () => Foerderschwerpunkt.getByKuerzel(props.data.daten?.kuerzelStatistik || null) || undefined,
		set: (value) => props.data.patch({ kuerzelStatistik: value?.daten.kuerzel })
	});

</script>
