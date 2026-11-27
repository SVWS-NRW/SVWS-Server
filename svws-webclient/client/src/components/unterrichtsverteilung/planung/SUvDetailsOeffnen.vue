<template>
	<button type="button"
		title="Details im anderen Bereich öffnen"
		aria-label="Details im anderen Bereich öffnen"
		class="pointer-events-none z-10 inline-flex shrink-0 items-center justify-center bg-ui-selected text-ui-brand opacity-0 transition-opacity group-hover:pointer-events-auto group-hover:opacity-100 focus-visible:opacity-100 focus-visible:outline-2 focus-visible:outline-ui-brand"
		:class="varianten[variante].button"
		@click.stop="props.oeffnen()">
		<span class="i-ri-eye-line icon-ui-brand" :class="varianten[variante].icon" aria-hidden="true" />
	</button>
</template>

<script setup lang="ts">
	type Variante = 'kompakt' | 'karte' | 'gross' | 'tabelle';
	const props = withDefaults(defineProps<{
		variante?: Variante;
		oeffnen: () => void;
	}>(), { variante: 'karte' });
	const varianten: Record<Variante, { button: string; icon: string }> = {
		kompakt: { button: 'absolute inset-y-px right-px w-1/2 rounded-r-sm', icon: 'icon' },
		karte: { button: 'absolute bottom-0.5 right-0.5 h-8 w-8 rounded-sm', icon: 'icon' },
		gross: { button: 'absolute bottom-1 right-1 h-12 w-12 rounded-md', icon: 'icon-xl' },
		tabelle: { button: 'h-6 w-6 rounded-sm', icon: 'icon-sm' },
	};

</script>
