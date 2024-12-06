<template>
	<a class="sidebar--menu-item" :class="{
		'sidebar--menu-item--active': active,
		'sidebar--menu-item--collapsed': collapsed,
		'sidebar--menu-item--disabled': disabled,
		'sidebar--menu-item--statistik': hatlabel === 'Statistik' || statistik,
		'menuFocusField': active && secondary,
		'navigationFocusField': active && !secondary,
	}" href="#" @click.prevent="onClick"
		:title="disabled ? 'Nicht verfügbar' : hatlabel" ref="menuLink">
		<span v-if="$slots.icon" class="sidebar--menu-item--icon">
			<slot name="icon" />
		</span>
		<span class="sidebar--menu-item--label">
			<slot name="label" />
			<span v-if="subline" class="sidebar--menu-item--subline">
				{{ subline }}
			</span>
		</span>
	</a>
</template>

<script setup lang='ts'>

	import { computed, useSlots, onMounted, ref, watch } from 'vue';

	const menuLink = ref<HTMLElement>();

	const props = withDefaults(defineProps<{
		active?: boolean;
		collapsed?: boolean;
		disabled?: boolean;
		subline?: string;
		statistik?: boolean;
		focus?: boolean;
		secondary?: boolean;
	}>(), {
		active: false,
		collapsed: false,
		disabled: false,
		subline: '',
		statistik: false,
		focus: false,
		secondary: false,
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	onMounted(() => doFocus());

	watch(() => props.focus, () => doFocus());

	function doFocus() {
		if (props.focus && menuLink.value)
			menuLink.value.focus();
	}

	function onClick(event: MouseEvent) {
		emit("click", event);
	}

	const slots = useSlots();

	const hatlabel = computed(() => {
		const label = slots.label?.()
		if ((label !== undefined) && (label.length > 0) && (typeof label[0].children === 'string'))
			return label[0].children;
		return "";
	});

</script>

<style lang="postcss">
.sidebar--menu-item {
	@apply flex items-center flex-col cursor-pointer p-1 rounded my-1;
	padding: 0.25rem 0.1rem;

	@media (orientation: portrait) {
		@apply my-0 px-1 min-w-[4.5rem] justify-center;
	}

	&:last-child {
		@apply mb-0;
	}

	span.icon-lg {
		@apply -mb-2;
	}

	&--active {
		@apply bg-ui-selected text-ui-onselected;

		&:focus {
			@apply outline-none;
		}

		&:hover,
		&:focus-visible {
			@apply bg-ui-selected text-ui-onselected;
		}

		&:focus-visible {
			@apply ring ring-ui-brand;
		}

		.icon-lg {
			@apply icon-primary;
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
		@apply bg-ui-hover;
	}
}

.sidebar--menu-item--label {
	@apply text-ellipsis max-w-full overflow-hidden;
	font-size: 0.78rem;
}

@media (min-width: 1920px) {
	.sidebar--menu .sidebar--menu-item--label {
		font-size: 0.9rem;
	}
}

.secondary-menu--content .sidebar--menu-item {
	@apply text-ui;
	@apply px-3 inline-block my-1;

	&:focus {
		@apply outline-none;
	}

	&:hover,
	&:focus-visible {
		@apply bg-ui-hover;
	}

	&--active {
		@apply bg-ui-selected text-ui-onselected;
	}
}

.secondary-menu--content .sidebar--menu-item--label {
	@apply text-base;
}

.sidebar--menu-item--icon {
	font-size: 1.2rem;

	@media (min-width: 1280px) {
		font-size: 1.3rem;
	}

	@media (min-width: 2000px) {
		font-size: 1.35rem;
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
		@apply bg-ui-disabled text-ui-disabled border-ui-disabled !important;
		@apply pointer-events-none;
	}
}
</style>
