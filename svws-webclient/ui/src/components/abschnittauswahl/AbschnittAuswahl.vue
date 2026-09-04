<template>
	<div v-if="!disabled" class="inline-flex gap-3 items-center">
		<svws-ui-tooltip :indicator="false" v-if="schuleState.abschnitt.id !== abschnittState.auswahl.id" position="bottom-start">
			<span class="cursor-pointer" :class="{'text-ui-danger text-headline-md -mr-1': schuleState.abschnitt.id !== abschnittState.auswahl.id, 'opacity-50 hover:opacity-100 text-base pt-1': schuleState.abschnitt.id === abschnittState.auswahl.id}">
				<span class="icon i-ri-alert-line icon-ui-danger -my-1.5 -mr-1 hover:icon-ui-danger relative -top-0.5" />
			</span>
			<template #content>
				<span v-if="schuleState.abschnitt.id === abschnittState.auswahl.id">
					Der ausgewählte Abschnitt ist der auswahl geltende Schulabschnitt.
				</span>
				<span v-else>
					Aktuell geltender Schulabschnitt: <span class="font-bold">{{ schuleState.abschnitt.schuljahr }}.{{ schuleState.abschnitt.abschnitt }}</span>
				</span>
			</template>
		</svws-ui-tooltip>
		<svws-ui-select headless :model-value="abschnittState.auswahl" @update:model-value="abschnitt => abschnittState.setAuswahl(abschnitt!.id)"
			:items="abschnittState.alle" :item-sort="item_sort" :item-text="item_text" :danger="schuleState.abschnitt.id !== abschnittState.auswahl.id"
			:class="{'opacity-50 hover:opacity-100 focus-within:opacity-100': schuleState.abschnitt.id === abschnittState.auswahl.id}" :highlight-item="schuleState.abschnitt" />
	</div>
	<span v-else class="text-base font-bold opacity-50 select-none">{{ abschnittState.auswahl.schuljahr }}.{{ abschnittState.auswahl.abschnitt }}</span>
</template>


<script setup lang="ts">

	import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
	import { useAbschnittState } from '@ui/states/AbschnittState';
	import { useSchuleState } from '@ui/states/SchuleState';


	const props = withDefaults(defineProps<{
		disabled?: boolean
	}>(), {
		disabled: false,
	});
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + (b.abschnitt * 0.1) - (a.schuljahr + (a.abschnitt * 0.1));
	const item_text = (item: Schuljahresabschnitt) => item.schuljahr > 0 ? `${item.schuljahr}/${(item.schuljahr + 1) % 100}.${item.abschnitt}` : "Abschnitt";

</script>
