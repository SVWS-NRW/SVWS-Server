<script setup lang='ts'>
const {
	active = false,
	collapsed = false,
	subline = '',
} = defineProps<{
	active?: boolean;
	collapsed?: boolean;
	subline?: string;
}>();

const emit = defineEmits<{
	(e: 'click', event: MouseEvent): void;
}>();

function onClick(event: MouseEvent) {
	emit("click", event);
}
</script>

<template>
	<a
class="sidebar--menu-item" :class="{
		'sidebar--menu-item--active': active,
		'sidebar--menu-item--collapsed': collapsed
	}" href="#" @click.prevent="onClick">
		<span class="sidebar--menu-item--icon">
			<Icon>
				<slot name="icon" />
			</Icon>
		</span>
		<span class="sidebar--menu-item--label">
			<slot name="label" />
			<span v-if="subline" class="sidebar--menu-item--subline">
				{{ subline }}
			</span>
		</span>
	</a>
</template>

<style>
.sidebar--menu-item {
	@apply flex items-center;
	@apply py-1;
	@apply my-1;
	@apply border-l-2 border-transparent;
	@apply cursor-pointer;
}

.sidebar--menu--body .sidebar--menu-item,
.sidebar--menu--footer .sidebar--menu-item {
	@apply text-white;
}

.sidebar--menu--body .sidebar--menu-item--active,
.sidebar--menu--footer .sidebar--menu-item--active {
	@apply border-white;
	@apply font-bold;
}

.secondary-menu--content .sidebar--menu-item {
	@apply text-dark;
}

.secondary-menu--content .sidebar--menu-item--active {
	@apply border-dark;
	@apply font-bold;
}

.sidebar--menu-item--icon {
	@apply flex;
	@apply mr-4 ml-3;
	@apply text-headline-2;
}

.sidebar--menu-item--label {
	@apply flex flex-col;
	@apply text-body;

	line-height: 1.25;
}

.sidebar--menu-item--collapsed .sidebar--menu-item--label {
	@apply hidden;
}

.sidebar--menu-item--subline {
	@apply text-caption font-normal;
}
</style>
