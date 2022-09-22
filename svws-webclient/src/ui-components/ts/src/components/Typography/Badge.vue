<script setup lang='ts'>
type Variant = "primary" | "success" | "error" | "highlight" | "light";
type Size = "big" | "medium" | "normal" | "small" | "tiny";

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

<style>
.badge {
	@apply border;
	@apply font-bold;
	@apply rounded;
}

.badge--normal {
	@apply px-2 py-1;
	@apply text-body;
}

.badge--tiny {
	@apply px-1 py-0;
	@apply text-button;
}

.badge--small {
	@apply px-2 py-1;
	@apply text-button;
}

.badge--medium {
	@apply px-2.5 py-1.5;
	@apply text-headline-4;
}

.badge--big {
	@apply px-3 py-2;
	@apply text-headline-3;
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
