<template>
	<div class="inline-flex gap-3 items-center">
		<svws-ui-tooltip :indicator="false" v-if="aktSchulabschnitt !== aktAbschnitt.id" position="bottom-start">
			<span class="cursor-pointer" :class="{'text-error text-headline-md -mr-1': aktSchulabschnitt !== aktAbschnitt.id, 'opacity-50 hover:opacity-100 text-base pt-1': aktSchulabschnitt === aktAbschnitt.id}">
				<i-ri-alert-line class="-mb-1 hover:text-error" />
			</span>
			<template #content>
				<span v-if="aktSchulabschnitt === aktAbschnitt.id">
					Der ausgewählte Abschnitt ist der aktuell geltende Schulabschnitt.
				</span>
				<span v-else>
					Aktuell geltender Schulabschnitt: <span class="font-bold">{{ aktBezeichnung }}</span>
				</span>
			</template>
		</svws-ui-tooltip>
		<svws-ui-multi-select :model-value="aktAbschnitt" @update:model-value="setAbschnitt" :items="abschnitte" :item-sort="item_sort" :item-text="item_text" :danger="aktSchulabschnitt !== aktAbschnitt.id" />
	</div>
</template>


<script setup lang="ts">

	import type { Schuljahresabschnitt } from "@core";

	const props = defineProps<{
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
		aktSchulabschnitt: number;
	}>();

	// eslint-disable-next-line vue/no-setup-props-destructure
	const abschnitt = props.abschnitte.get(props.aktSchulabschnitt);
	const aktBezeichnung = `${abschnitt?.schuljahr}.${abschnitt?.abschnitt}`;

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);
	const item_text = (item: Schuljahresabschnitt) => item.schuljahr ? `${item.schuljahr}.${item.abschnitt}` : "Abschnitt";

</script>
