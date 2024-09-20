<template>
	<div v-if="daten().aktiv" class="inline-flex gap-3 items-center">
		<svws-ui-tooltip :indicator="false" v-if="daten().schule.id !== daten().aktuell.id" position="bottom-start">
			<span class="cursor-pointer" :class="{'text-error text-headline-md -mr-1': daten().schule.id !== daten().aktuell.id, 'opacity-50 hover:opacity-100 text-base pt-1': daten().schule.id === daten().aktuell.id}">
				<span class="icon i-ri-alert-line inline-block icon-error -my-1.5 -mr-1 hover:icon-error relative -top-0.5" />
			</span>
			<template #content>
				<span v-if="daten().schule.id === daten().aktuell.id">
					Der ausgewählte Abschnitt ist der aktuell geltende Schulabschnitt.
				</span>
				<span v-else>
					Aktuell geltender Schulabschnitt: <span class="font-bold">{{ aktBezeichnung }}</span>
				</span>
			</template>
		</svws-ui-tooltip>
		<svws-ui-select :headless="!disableHeadless" :model-value="daten().aktuell" @update:model-value="abschnitt => daten().set(abschnitt!)"
			:items="daten().abschnitte" :item-sort="item_sort" :item-text="item_text" :danger="daten().schule.id !== daten().aktuell.id"
			:class="{'opacity-50 hover:opacity-100 focus-within:opacity-100': daten().schule.id === daten().aktuell.id}" :highlight-item="abschnitt" />
	</div>
	<span v-else class="text-base font-bold opacity-50 select-none">{{ daten().aktuell.schuljahr + "." + daten().aktuell.abschnitt }}</span>
</template>


<script setup lang="ts">

	import { computed } from 'vue';
	import type { Schuljahresabschnitt } from '../../../../core/src/asd/data/schule/Schuljahresabschnitt';
	import type { AbschnittAuswahlProps } from './AbschnittAuswahlProps';

	const props = defineProps<AbschnittAuswahlProps>();

	const abschnitt = computed(() => props.daten().abschnitte.get(props.daten().schule.id));
	const aktBezeichnung = abschnitt.value ? `${abschnitt.value.schuljahr}.${abschnitt.value.abschnitt}` : 'Abschnitt ungültig';

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + (b.abschnitt * 0.1) - (a.schuljahr + (a.abschnitt * 0.1));
	const item_text = (item: Schuljahresabschnitt) => item.schuljahr > 0 ? `${item.schuljahr}/${(item.schuljahr + 1) % 100}.${item.abschnitt}` : "Abschnitt";

</script>
