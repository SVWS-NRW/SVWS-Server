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
	}" href="#" @click.prevent="onClick"
	:title="$slots.label()[0].children">
		<span v-if="$slots.icon" class="sidebar--menu-item--icon">
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
	@apply p-2 rounded my-1;
	@apply cursor-pointer;
}

.sidebar--menu-item:hover,
.sidebar--menu-item:focus {
	@apply bg-dark-20 bg-opacity-20;
}

.sidebar--menu-item--collapsed {
	@apply flex-col;
	padding: 0.25rem 0.1rem;
}

.sidebar--menu-item--label {
	@apply text-ellipsis overflow-hidden text-base;
	max-width: 100%;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 1;
	word-break: break-all;
}

.sidebar--menu-item--collapsed .sidebar--menu-item--label {
	font-size: 0.78rem;
}

.sidebar--menu--body .sidebar--menu-item,
.sidebar--menu--footer .sidebar--menu-item {
	@apply text-white;
}

.sidebar--menu--body .sidebar--menu-item--active,
.sidebar--menu--footer .sidebar--menu-item--active {
	@apply bg-light bg-opacity-100 text-dark;
}

.secondary-menu--content .sidebar--menu-item {
	@apply text-dark px-3 inline-block;
	margin: 0.1rem 0;
}

.secondary-menu--content .sidebar--menu-item:hover,
.secondary-menu--content .sidebar--menu-item:focus {
	@apply bg-dark-20 bg-opacity-50;
}

.secondary-menu--content .sidebar--menu-item--active {
	@apply border-dark;
	@apply font-bold;
}

.sidebar--menu-item--icon {
	@apply mr-2;
	font-size: 1.618rem;
}

.sidebar--menu-item--collapsed .sidebar--menu-item--icon {
	@apply mr-0 mt-1;
}

.sidebar--menu-item--subline {
	@apply text-caption font-normal;
}
</style>
