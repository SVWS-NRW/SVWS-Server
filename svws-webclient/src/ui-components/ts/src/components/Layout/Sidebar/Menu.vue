<script setup lang='ts'>
const {
	collapsed = false,
} = defineProps<{
	collapsed?: boolean;
}>();

const emit = defineEmits<{
	(e: 'toggle', value: boolean): void;
}>();

function onCollapse() {
	emit("toggle", !collapsed);
}
</script>

<template>
	<div
class="sidebar--menu" :class="{
		'sidebar--menu--collapsed': collapsed
	}">
		<div class="sidebar--menu--header">
			<slot name="header" />
			<Button class="sidebar--menu--collapse" @click.prevent="onCollapse">
				<Icon>
					<i-ri-arrow-right-s-line v-if="collapsed" />
					<i-ri-arrow-left-s-line v-else />
				</Icon>
			</Button>
		</div>
		<div class="sidebar--menu--body">
			<slot />
		</div>
		<div class="sidebar--menu--footer">
			<slot name="footer" />
		</div>
	</div>
</template>

<style>
.sidebar--menu {
	@apply flex min-h-full flex-1 flex-col;
	@apply w-56;
	@apply bg-dark;
}

.sidebar--menu--collapsed {
	@apply w-16;
}

.sidebar--menu--header {
	@apply relative;
	@apply flex flex-shrink-0 flex-col justify-center;
	@apply px-4 py-4;
	@apply bg-white;
	min-height: 6.5rem;
}

.sidebar--menu--header input {
	@apply border-0 cursor-pointer p-0 h-auto;
}

.sidebar--menu--collapse {
	@apply absolute;
	@apply bottom-0;
	@apply flex items-center justify-center;
	@apply w-7;
	@apply h-7;
	@apply rounded-full;
	@apply bg-white;
	@apply text-headline-4 text-black;
	@apply translate-y-1/2 transform;
	@apply border-0;
	padding: 0.9rem;
	right: theme("spacing.2");
}

.sidebar--menu--collapsed .sidebar--menu--collapse {
	right: 50%;
	margin-right: -0.875rem;
}

.sidebar--menu--collapse:focus {
	@apply outline-none;
	@apply ring ring-primary ring-opacity-50;
}

.sidebar--menu--body {
	@apply flex flex-1 flex-col;
	@apply py-4;
}

.sidebar--menu--footer {
	@apply flex flex-col;
	@apply py-4;
}
</style>
