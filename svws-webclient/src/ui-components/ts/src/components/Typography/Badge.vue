<script lang='ts'>
export type Variant = "primary" | "success" | "error" | "highlight" | "light";
export type Size = "big" | "medium" | "normal" | "small" | "tiny";
</script>
<script setup lang='ts'>

const {
	variant = 'light',
	size = 'normal',
} = defineProps<{
	variant?: Variant;
	size?: Size;
}>();
</script>

<template>
	<span
class="badge" :class="{
		'badge--primary': variant === 'primary',
		'badge--success': variant === 'success',
		'badge--error': variant === 'error',
		'badge--highlight': variant === 'highlight',
		'badge--light': variant === 'light',
		'badge--big': size === 'big',
		'badge--medium': size === 'medium',
		'badge--normal': size === 'normal',
		'badge--small': size === 'small',
		'badge--tiny': size === 'tiny'
	}">
		<slot />
	</span>
</template>

<style lang="postcss">
.badge {
	@apply font-bold leading-none;
	@apply rounded inline-block align-top;
	font-size: 0.65em;
	padding: 0.35em 0.5em;
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
</style>
