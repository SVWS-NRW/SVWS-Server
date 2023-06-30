<script setup lang='ts'>
	import type { Size, Type } from '../../types';

	const props = withDefaults(defineProps<{
		type?: Type;
		size?: Size;
		short?: boolean;
	}>(), {
		type: 'light',
		size: 'normal',
		short: false
	});
</script>

<template>
	<template v-if="short">
		<svws-ui-tooltip :indicator="false">
			<span class="badge" :class="{
				'badge--primary': type === 'primary',
				'badge--success': type === 'success',
				'badge--error': type === 'error',
				'badge--highlight': type === 'highlight',
				'badge--light': type === 'light',
				'badge--lg': size === 'big',
				'badge--normal': size === 'normal',
				'badge--small': size === 'small',
				'badge--short': short,
			}">
				<slot />
			</span>
			<template #content>
				<slot />
			</template>
		</svws-ui-tooltip>
	</template>
	<template v-else>
		<span class="badge" :class="{
			'badge--primary': type === 'primary',
			'badge--success': type === 'success',
			'badge--error': type === 'error',
			'badge--highlight': type === 'highlight',
			'badge--light': type === 'light',
			'badge--lg': size === 'big',
			'badge--normal': size === 'normal',
			'badge--small': size === 'small',
			'badge--short': short,
		}">
			<slot />
		</span>
	</template>
</template>

<style lang="postcss">
.badge {
	@apply font-bold leading-none;
	@apply rounded inline-flex items-center align-text-top;
	font-size: 0.65em;
	padding: 0.35em 0.5em;
	gap: 0.25em;

	svg {
		margin: -0.1em;
	}
}

.badge--lg {
	@apply rounded-md;
	font-size: 0.8em;
	padding: 0.25em 0.45em;
}

.badge--error {
	@apply bg-error;
	@apply border-error;
	@apply text-white;
}

.badge--primary {
	@apply bg-primary;
	@apply border-primary;
	@apply text-white;
}

.badge--light {
	@apply bg-light dark:bg-white/10;
	@apply border border-black/5 dark:border-white/20;
	@apply text-dark dark:text-white;
}

.badge--success {
	@apply bg-success;
	@apply border-success;
	@apply text-dark;
}

.badge--highlight {
	@apply bg-highlight;
	@apply border-highlight;
	@apply text-dark;
}

.badge--short {
	@apply overflow-hidden relative line-clamp-1 overflow-ellipsis w-16 max-w-full break-all;
}

.badge--small {
	font-size: 0.5em;
	margin-top: 0.4em;
	margin-bottom: -0.5em;
	padding: 0.25em 0.45em;
}
</style>
