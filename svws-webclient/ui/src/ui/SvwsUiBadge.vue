<template>
	<template v-if="short">
		<svws-ui-tooltip :indicator="false">
			<span class="svws-ui-badge badge" :class="{
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
		<span class="svws-ui-badge badge" :class="{
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

<script setup lang='ts'>
	import type { Size, Type } from '../types';

	const props = withDefaults(defineProps<{
		type?: Type;
		size?: Size;
		short?: boolean;
	}>(), {
		type: 'light',
		size: 'normal',
		short: false,
	});
</script>


<style lang="postcss">
.badge {
	@apply border;
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
	@apply bg-ui-danger;
	@apply border-ui-danger;
	@apply text-ui-ondanger;
}

.badge--primary {
	@apply bg-ui-brand;
	@apply border-ui-brand;
	@apply text-ui-onbrand;
}

.badge--light {
	@apply bg-ui-neutral;
	@apply border-ui-secondary;
	@apply text-ui-onneutral;
}

.badge--success {
	@apply bg-ui-success;
	@apply border-ui-success;
	@apply text-ui-onsuccess;
}

.badge--highlight {
	@apply bg-ui-warning;
	@apply border-ui-warning;
	@apply text-ui-onwarning;
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
