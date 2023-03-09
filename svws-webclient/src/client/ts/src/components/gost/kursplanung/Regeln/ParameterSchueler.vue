<template>
	<svws-ui-multi-select v-model="selected" :items="mapSchueler"
		:item-text="(i: SchuelerListeEintrag) => `${i.nachname}, ${i.vorname}`"
		:item-filter="filter" :item-sort="sort" autocomplete />
</template>

<script setup lang="ts">
	import { SchuelerListeEintrag } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		modelValue: SchuelerListeEintrag;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<SchuelerListeEintrag> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const filter = (items: SchuelerListeEintrag[], search: string) => {
		return items.filter((i: SchuelerListeEintrag) => {
			if (i) {
				return (
					i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) ||
					i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())
				);
			}
			return [];
		});
	};

	const sort = (a: SchuelerListeEintrag, b: SchuelerListeEintrag) => {
		if (a.nachname && b.nachname) {
			return a.nachname.localeCompare(b.nachname);
		} else if (a.nachname && !b.nachname) {
			return -1;
		} else if (!a.nachname && !b.nachname) {
			return 1;
		}
		return 0;
	};

</script>
