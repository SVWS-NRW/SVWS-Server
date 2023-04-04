<script setup lang='ts'>
	import { Size, Type } from '../../types';

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
				'badge--short': short,
			}">
				<slot />
			</span>
	</template>
</template>

<style lang="postcss">
.badge {
	@apply font-bold leading-none;
	@apply rounded-full inline-flex items-center align-text-top;
	font-size: 0.65em;
	padding: 0.35em 0.5em;
	gap: 0.25em;

	svg {
		margin: -0.1em;
	}
}

.badge--lg {
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
	@apply bg-light;
	@apply border-light;
	@apply text-dark;
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
	@apply overflow-hidden relative;
	max-width: 6ch;

	&:before {
		@apply absolute top-0 right-0 w-2/3 h-full bg-gradient-to-r;
		content: '';
	}

	&.badge--error:before {
		@apply from-error/0 from-10% to-error/100 to-75%;
	}

	&.badge--primary:before {
		@apply from-primary/0 from-10% to-primary/100 to-75%;
	}

	&.badge--light:before {
		@apply from-light/0 from-10% to-light/100 to-75%;
	}

	&.badge--success:before {
		@apply from-success/0 from-10% to-success/100 to-75%;
	}

	&.badge--highlight:before {
		@apply from-highlight/0 from-10% to-highlight/100 to-75%;
	}
}
</style>
