<script setup lang='ts'>
	import { Size } from '../../types';

	const props = withDefaults(defineProps<{
		// size?: Extract<Size, 'normal' | 'small'>;
		size?: 'normal' | 'small';
		progress?: number;
	}>(), {
		size: 'normal',
		progress: 0,
	});
</script>

<template>
	<div class="progress-bar" :class="{
		'progress-bar--normal': size === 'normal',
		'progress-bar--small': size === 'small'
	}">
		<div class="progress-bar--label">
			<slot />
		</div>
		<div class="progress-bar--percentage">
			{{ `${Number(progress.toFixed(2))} %` }}
		</div>
		<div class="progress-bar--bar">
			<div class="progress-bar--completed" :style="{ width: `${progress}%` }" />
		</div>
	</div>
</template>

<style>
.progress-bar {
	@apply flex flex-wrap justify-between;
}

.progress-bar--normal {
	@apply text-input;
}

.progress-bar--small {
	@apply text-caption;
}

.progress-bar--bar {
	@apply bg-light;
	@apply overflow-hidden;
	@apply rounded-full;
	@apply w-full;
}

.progress-bar--normal .progress-bar--bar {
	@apply h-3;
	@apply mt-3;
}

.progress-bar--small .progress-bar--bar {
	@apply h-1;
	@apply mt-1;
}

.progress-bar--completed {
	@apply bg-primary;
	@apply transition-all;
}

.progress-bar--normal .progress-bar--completed {
	@apply h-3;
}

.progress-bar--small .progress-bar--completed {
	@apply h-1;
}
</style>
