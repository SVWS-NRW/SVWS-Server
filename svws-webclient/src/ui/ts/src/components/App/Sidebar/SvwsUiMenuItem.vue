<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		active?: boolean;
		collapsed?: boolean;
		disabled?: boolean;
		subline?: string;
		statistik?: boolean;
	}>(), {
		active: false,
		collapsed: false,
		disabled: false,
		subline: '',
		statistik: false,
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	function onClick(event: MouseEvent) {
		emit("click", event);
	}
</script>

<template>
	<a class="sidebar--menu-item" :class="{
		'sidebar--menu-item--active': active,
		'sidebar--menu-item--collapsed': collapsed,
		'sidebar--menu-item--disabled': disabled,
		'sidebar--menu-item--statistik': $slots.label?.()[0].children === 'Statistik' || statistik,
	}" href="#" @click.prevent="onClick"
		:title="disabled ? 'Nicht verfügbar' : ($slots.label?.()[0].children as unknown as string)">
		<span v-if="$slots.icon" class="sidebar--menu-item--icon">
			<span class="icon">
				<slot name="icon" />
			</span>
		</span>
		<span class="sidebar--menu-item--label">
			<slot name="label" />
			<span v-if="subline" class="sidebar--menu-item--subline">
				{{ subline }}
			</span>
		</span>
	</a>
</template>

<style lang="postcss">
.sidebar--menu-item {
	@apply flex items-center flex-col cursor-pointer p-2 rounded my-1;
	padding: 0.25rem 0.1rem;

	@media (orientation: portrait) {
		@apply my-0 px-1 min-w-[4.5rem] justify-center;
	}

	&:last-child {
		@apply mb-0;
	}

	&--active {
		@apply bg-svws/10 text-svws dark:bg-svws/20;

		&.sidebar--menu-item--statistik {
			@apply bg-violet-500/20 text-violet-500;
		}
	}
}

.sidebar--menu-item:hover,
.sidebar--menu-item:focus,
.sidebar--menu--body .sidebar--menu-item:hover,
.sidebar--menu--body .sidebar--menu-item:focus,
.sidebar--menu--footer .sidebar--menu-item:hover,
.sidebar--menu--footer .sidebar--menu-item:focus {
	&:not(.sidebar--menu-item--active) {
		@apply bg-black/10 dark:bg-white/10;
	}
}

.sidebar--menu-item--label {
	@apply text-ellipsis max-w-full overflow-hidden;
	font-size: 0.78rem;
}

@media (min-width: 1280px) {
	.sidebar--menu .sidebar--menu-item--label {
		font-size: 0.9rem;
	}
}

.secondary-menu--content .sidebar--menu-item {
	@apply text-black dark:text-white px-3 inline-block my-1;

	&:hover,
	&:focus {
		@apply bg-light dark:bg-white/5;
	}

	&--active {
		@apply bg-svws/5 text-svws;
	}
}

.secondary-menu--content .sidebar--menu-item--label {
	@apply text-base;
}

.sidebar--menu-item--icon {
	@apply mr-0 mt-1;
	font-size: 1.2rem;

	@media (min-width: 1280px) {
		font-size: 1.4rem;
	}

	@media (min-width: 2000px) {
		@apply mb-1;
		font-size: 1.5rem;
	}
}


.sidebar--menu-item--subline {
	@apply text-sm font-normal;

	@media (orientation: portrait) {
		@apply text-base;
	}
}

.sidebar--menu-item--disabled {
	&,
	&:hover,
	&:focus {
		@apply bg-light border-black text-black !important;
		@apply opacity-25;
		@apply cursor-not-allowed;
	}
}
</style>
