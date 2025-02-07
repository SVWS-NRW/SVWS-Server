<template>
	<div class="svws-ui-dashboard-tile"
		:class="{
			'svws-ui-dashboard-tile--dark': color === 'dark',
			'svws-ui-dashboard-tile--transparent': color === 'transparent',
			'col-span-2': span === 2,
			'col-span-full': span === 'full',
			'svws-ui-dashboard-tile--clickable': clickable,
		}">
		<div v-if="title || $slots.title" class="dashboard-tile--title">
			<slot name="title">
				{{ title }}
			</slot>
		</div>
		<slot />
		<div v-if="(number || $slots.number) || numberLabel" class="dashboard-tile--number">
			<slot name="number">
				<span v-if="number" class="line-clamp-1">{{ number }}</span>
			</slot>
			<span v-if="numberLabel" class="dashboard-tile--number-label">{{ numberLabel }}</span>
		</div>
	</div>
</template>

<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		color?: 'light' | 'dark' | 'transparent';
		span?: 1 | 2 | 'full';
		title?: string;
		number?: string;
		numberLabel?: string;
		clickable?: boolean;
	}>(), {
		color: 'light',
		span: 1,
		clickable: false,
		title: undefined,
		number: undefined,
		numberLabel: undefined,
	});
</script>
