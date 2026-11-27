<template>
	<span v-if="text !== ''"
		class="inline-flex min-w-6 shrink-0 items-center justify-center rounded border border-ui-25 px-1.5 py-0.5 text-xs font-bold text-uistatic"
		:style="{ backgroundColor: farbe }"
		:title="bezeichnung">{{ text }}</span>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import { Fach } from '@core/asd/types/fach/Fach';
	import { useUvState } from '@ui/states/UvState';

	const props = defineProps<{
		idFach?: number | null;
		kuerzel?: string;
		kuerzelStatistik?: string
	}>();

	const state = useUvState();
	const fachdaten = computed(() => {
		const fach = props.idFach === undefined || props.idFach === null ? null : state.uvManager.fachGetByIdOrNull(props.idFach);
		return fach === null ? null : state.uvManager.fachdatenGetByFach(fach);
	});

	const text = computed(() => fachdaten.value?.kuerzel ?? props.kuerzel ?? '');
	const bezeichnung = computed(() => fachdaten.value?.bezeichnung ?? text.value);
	const farbe = computed(() => Fach.getBySchluesselOrDefault(fachdaten.value?.kuerzelStatistik ?? props.kuerzelStatistik ?? props.kuerzel ?? '')
		.getHMTLFarbeRGB(state.planungsabschnitt?.schuljahr ?? -1));
</script>
