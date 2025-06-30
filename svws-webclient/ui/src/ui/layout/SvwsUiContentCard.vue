<template>
	<div :id class="content-card" :class="{'h-full': overflowScroll, 'svws-has-background': hasBackground}">
		<div v-if="title || $slots.actions || $slots.title" class="content-card--header" :class="{ 'content-card--header--has-actions': $slots.actions }">
			<slot name="title">
				<h3 v-if="title" class="content-card--headline" :class="{'content-card--headline--large': largeTitle}" :title>
					{{ title }}
				</h3>
			</slot>
			<div v-if="$slots.actions" class="content-card--actions">
				<slot name="actions" />
			</div>
		</div>
		<div class="content-card--content" :class="{ 'content-card--content--overflow-scroll': overflowScroll }">
			<slot />
		</div>
	</div>
</template>

<script setup lang='ts'>

	import { useId } from "vue";

	const props = withDefaults(defineProps<{
		title?: string;
		overflowScroll?: boolean;
		largeTitle?: boolean;
		hasBackground?: boolean;
	}>(), {
		title: '',
		overflowScroll: false,
		largeTitle: false,
		hasBackground: false,
	});

	defineSlots();
	const id = useId();

</script>
