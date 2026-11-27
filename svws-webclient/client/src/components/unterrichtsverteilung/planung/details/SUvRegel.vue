<template>
	<div class="p-3">
		<svws-ui-content-card title="Blockungsregel">
			<p class="font-semibold">{{ beschreibung }}</p>
			<p class="mt-2 text-sm text-ui-secondary">Priorität: {{ prioritaet }}</p>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import type { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
	import { useUvState } from '@ui/states/UvState';

	import { createUvPresenter, formatUvRegelPrioritaet } from '../../UvPresenter';

	const props = defineProps<{
		regel: UvBlockungRegel | undefined;
	}>();

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const beschreibung = computed(() => props.regel === undefined || state.planungsabschnitt === null
		? 'Unbekannte Regel' : presenter.value.regelBeschreibung(props.regel, state.planungsabschnitt));
	const prioritaet = computed(() => props.regel === undefined ? 'Unbekannt' : formatUvRegelPrioritaet(props.regel.prioritaet));
</script>
