<template>
	<svws-ui-tooltip v-if="short" :indicator="false">
		<span class="svws-ui-badge badge" :class="classes"><slot /></span>
		<template #content><slot /></template>
	</svws-ui-tooltip>
	<span v-else class="svws-ui-badge badge" :class="classes"><slot /></span>
</template>

<script setup lang='ts'>

	import type { Size, Type } from '@ui/types';
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		/** Bestimmt die Farbe bzw. Erscheinung des Badges. */
		type?: Type;
		/** Bestimmt die Größe des Badges. */
		size?: Size;
		/** Kürzt den Badge auf eine feste Breite (w-16) mit einzeiliger Textabschneidung.
		 * Der vollständige Inhalt wird als Tooltip angezeigt. */
		short?: boolean;
		/** Stellt den Badge mit abgerundeten Ecken dar. */
		rounded?: boolean;
	}>(), {
		type: 'light',
		size: 'normal',
		short: false,
		rounded: false,
	});

	defineSlots<{
		/** Inhalt des Badges */
		default(): void;
	}>();

	const classes = computed(() => ({
		'badge--primary': props.type === 'primary',
		'badge--success': props.type === 'success',
		'badge--error': props.type === 'error',
		'badge--highlight': props.type === 'highlight',
		'badge--light': props.type === 'light',
		'badge--lg': props.size === 'big',
		'badge--normal': props.size === 'normal',
		'badge--small': props.size === 'small',
		'badge--short': props.short,
		'badge--rounded': props.rounded,
	}));
</script>
