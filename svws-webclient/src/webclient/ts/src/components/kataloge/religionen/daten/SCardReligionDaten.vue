<template>
	<svws-ui-content-card title="Daten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-multi-select title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="inputKatalogReligionenStatistik"
					:item-text="(i: Religion) => i.daten.kuerzel.toString()" required />
				<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung" v-model="inputText" type="text" />
				<svws-ui-text-input placeholder="Zeugnisbezeichnung" v-model="inputTextzeugnis" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { Religion} from "@svws-nrw/svws-core-ts";
	import { DataReligion } from "~/apps/kataloge/religionen/DataReligion";

	const props = defineProps<{
		data: DataReligion;
	}>();

	const inputKatalogReligionenStatistik: ComputedRef<Religion[] | undefined> = computed(() => Religion.values());

	const id: ComputedRef<number | undefined> = computed(() => {
		return props.data.daten?.id.valueOf();
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzel?.toString(),
		set: (value) => void props.data.patch({ kuerzel: value })
	});

	const inputText: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.text?.toString(),
		set: (value) => void props.data.patch({ text: value })
	});

	const inputTextzeugnis: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.textZeugnis?.toString(),
		set: (value) => void props.data.patch({ textZeugnis: value })
	});

	const inputStatistikKuerzel: WritableComputedRef<Religion | undefined> = computed({
		get: () => Religion.getByKuerzel(props.data.daten?.kuerzel || null) || undefined,
		set: (value) => void props.data.patch({ kuerzel: value?.daten.kuerzel || null })
	});

</script>
