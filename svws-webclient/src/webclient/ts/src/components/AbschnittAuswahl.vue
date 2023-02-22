<template>
	<svws-ui-multi-select :model-value="aktAbschnitt" @update:model-value="updateAbschnitt" :items="abschnitte" :item-sort="item_sort" :item-text="item_text" />
</template>


<script setup lang="ts">

	import { List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();

	function updateAbschnitt(value: unknown) {
		if (value instanceof Schuljahresabschnitt)
			props.setAbschnitt(value);
	}

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);

	const item_text = (item: Schuljahresabschnitt) => item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";

</script>
