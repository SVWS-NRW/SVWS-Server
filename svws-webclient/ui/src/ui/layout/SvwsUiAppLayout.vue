<template>
	<div class="app--layout" :class="{ 'app--layout--has-aside': $slots.aside,
		's-sidebar': $slots.secondaryMenu && hasSecondaryMenu,
		's-all-sidebars-collapsed' : (!$slots.secondaryMenu || !hasSecondaryMenu || !sidebarExpanded) && (!$slots.tertiaryMenu || !hasTertiaryMenu || !secondSidebarExpanded),
		's-sidebar-collapsed': !sidebarExpanded, 's-second-sidebar-collapsed': !secondSidebarExpanded }">
		<div ref="appMenu" v-if="($slots.sidebar && !fullwidthContent)" class="app--menu" :class="{ 'has-scrollbar': hasScrollbar }" :style="{ '--scrollbar-width': scrollbarWidth + 'px' }">
			<slot name="sidebar" />
		</div>
		<div v-if="$slots.secondaryMenu && hasSecondaryMenu" class="app--sidebar" :class="{ 'app--sidebar-normal' : !secondaryMenuSmall }">
			<div class="s-toggle-first">
				<svws-ui-tooltip>
					<button type="button" @click="updateSidebarExpanded">
						<span class="icon i-ri-menu-fold-line" v-if="sidebarExpanded" />
						<span class="icon i-ri-menu-unfold-line" v-else />
					</button>
					<template #content>
						Sidebar {{ !sidebarExpanded ? 'einblenden' : 'ausblenden' }}
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="app--sidebar-container">
				<slot name="secondaryMenu" />
			</div>
		</div>
		<div v-if="$slots.tertiaryMenu && hasTertiaryMenu" class="app--second-sidebar" :class="{ 'app--sidebar-normal' : !tertiaryMenuSmall }">
			<div class="s-toggle-second">
				<svws-ui-tooltip>
					<button type="button" @click="updateSecondSidebarExpanded">
						<span class="icon i-ri-menu-fold-line" v-if="secondSidebarExpanded" />
						<span class="icon i-ri-menu-unfold-line" v-else />
					</button>
					<template #content>
						Second Sidebar {{ !secondSidebarExpanded ? 'einblenden' : 'ausblenden' }}
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="app--second-sidebar-container">
				<slot name="tertiaryMenu" />
			</div>
		</div>
		<main class="app--content">
			<div class="app--content-container relative" :class="{ 'fullwidth-content' : fullwidthContent }">
				<svws-ui-header v-if="false" />
				<slot name="main" />
			</div>
			<aside class="app-layout--aside" v-if="$slots.aside">
				<div class="app-layout--aside-container relative">
					<slot name="aside" />
				</div>
			</aside>
		</main>
	</div>
</template>

<script setup lang='ts'>

	import { onBeforeUnmount, onMounted, ref, computed } from "vue";

	const props = withDefaults(defineProps<{
		fullwidthContent?: boolean;
		noSecondaryMenu?: boolean;
		tertiaryMenu?: boolean;
		secondaryMenuSmall?: boolean;
		tertiaryMenuSmall?: boolean;
	}>(), {
		fullwidthContent: false,
		noSecondaryMenu: false,
		tertiaryMenu: false,
		secondaryMenuSmall: false,
		tertiaryMenuSmall: false,
	});

	const sidebarExpanded = ref<boolean>(true);
	const secondSidebarExpanded = ref<boolean>(true);
	const appMenu = ref<HTMLElement | null>(null);
	const hasScrollbar = ref(false);
	const scrollbarWidth = ref(0);

	const hasSecondaryMenu = computed<boolean>(() => !props.fullwidthContent && !props.noSecondaryMenu);
	const hasTertiaryMenu = computed<boolean>(() => !props.fullwidthContent && props.tertiaryMenu);

	const scrollbarObserver = new ResizeObserver((entries) => {
		entries.forEach((entry) => {
			// Endlosschleife vermeiden
			requestAnimationFrame(() => {
				scrollbarWidth.value = (entry.target as HTMLElement).offsetWidth - entry.target.clientWidth;
				hasScrollbar.value = scrollbarWidth.value > 0 ? true : false;
			});
		});
	});

	onMounted(() => {
		if (appMenu.value !== null)
			scrollbarObserver.observe(appMenu.value);
	});

	onBeforeUnmount(() => {
		if (appMenu.value !== null)
			scrollbarObserver.unobserve(appMenu.value);
	});

	function updateSidebarExpanded(): void {
		sidebarExpanded.value = !sidebarExpanded.value;
		localStorage.setItem('sidebarExpanded', sidebarExpanded.value ? 'true' : 'false');
	}

	if (localStorage.getItem('sidebarExpanded') !== null) {
		sidebarExpanded.value = localStorage.getItem('sidebarExpanded') === 'true';
	}

	function updateSecondSidebarExpanded(): void {
		secondSidebarExpanded.value = !secondSidebarExpanded.value;
		localStorage.setItem('secondSidebarExpanded', secondSidebarExpanded.value ? 'true' : 'false');
	}

	if (localStorage.getItem('secondSidebarExpanded') !== null) {
		secondSidebarExpanded.value = localStorage.getItem('secondSidebarExpanded') === 'true';
	}

</script>

<style lang="postcss">

	@reference "../../assets/styles/index.css";

	html {
		font-size: 10px;
		@apply bg-ui-neutral text-ui;

		&.dark {
			@apply bg-ui-black;
		}

		&.font-size-small {
			font-size: 9px;
		}

		&.font-size-large {
			font-size: 11px;
		}
	}

	@media (min-width: 1280px) {
		html {
			font-size: 12px;

			&.font-size-small {
				font-size: 11px;
			}

			&.font-size-large {
				font-size: 13px;
			}
		}
	}

	.app--layout {
		@apply relative h-screen w-full overflow-hidden bg-ui-neutral;
		@apply flex flex-row gap-2 p-2;
	}

	.app--menu {
		@apply overflow-y-auto -mr-3 pr-3 -my-3 py-3 min-w-fit max-w-32;
	}

	@-moz-document url-prefix() {
		.app--menu.has-scrollbar {
			padding-right: calc(var(--scrollbar-width) + 0.75rem);
		}
	}

	.app--sidebar,
	.app--second-sidebar,
	.app--content-container {
		@apply bg-ui;
		@apply h-full rounded-3xl;
		@apply flex flex-col;
		@apply overflow-y-hidden;
	}

	.app--content-container,
	.app--second-sidebar {
		@apply border-l-0 rounded-l-none;
	}

	.app--sidebar {
		@apply shrink-0 -mr-2 rounded-r-none p-2 pr-0 h-full border-r-0 relative;
		width: 20%;
		min-width: 24rem;
		max-width: 30rem;

		.app--sidebar-container {
			@apply rounded-2xl h-full;
			@apply border border-ui-secondary;
		}

		.secondary-menu--headline {
			@apply -mt-2;
		}

		.s-toggle-first {
			@apply absolute right-0 top-0 z-40 pt-4 pr-2 flex flex-col;

			button {
				@apply rounded-lg text-ui-secondary p-0.5 inline-flex flex-col items-center gap-1 text-headline-sm;

				svg {
					@apply shrink-0 text-headline-md;
				}

				&:focus {
					@apply outline-hidden;
				}

				&:hover,
				&:focus-visible {
					@apply bg-ui-hover text-ui;
				}

				&:focus-visible {
					@apply ring-3 ring-ui;
				}
			}

			.s-title {
				writing-mode: sideways-lr;
			}
		}
	}

	.app--sidebar:not(.app--sidebar-normal) {
		width: 15%;
		min-width: 16rem;
		max-width: 20rem;
	}

	.app--second-sidebar {
		@apply shrink-0 -mr-2 rounded-r-none p-2 pr-0 h-full border-r-0 relative;
		width: 20%;
		min-width: 24rem;
		max-width: 30rem;

		.app--second-sidebar-container {
			@apply rounded-2xl h-full;
			@apply border border-ui;
		}

		.secondary-menu--headline {
			@apply -mt-2;
		}

		.s-toggle-second {
			@apply absolute right-0 top-0 z-40 pt-4 pr-2 flex flex-col;

			button {
				@apply rounded-lg text-ui-contrast-50 p-0.5 inline-flex flex-col items-center gap-1 text-headline-sm;

				svg {
					@apply shrink-0 text-headline-md;
				}

				&:hover,
				&:focus-visible {
					@apply bg-ui-contrast-10 text-ui-contrast-100;
				}
			}

			.s-title {
				writing-mode: sideways-lr;
			}
		}
	}

	.app--second-sidebar:not(.app--sidebar-normal) {
		width: 15%;
		min-width: 16rem;
		max-width: 20rem;
	}

	@media (orientation: portrait) {
		.app--sidebar .s-toggle-first .s-toggle-second {
			@apply hidden;
		}
	}

	@media (orientation: landscape) {

		.s-sidebar-collapsed {
			.app--sidebar {
				@apply w-9 h-auto min-w-0 bg-transparent pointer-events-none z-40 pt-1 px-1 border-0;
				@apply -ml-1 3xl:-ml-0.5 -mr-10 3xl:-mr-11;

				.app--sidebar-container {
					@apply hidden;
				}
			}

			.s-toggle-first {
				@apply relative pr-0 pl-1 pointer-events-auto top-0.5 left-0.5;
			}

			.app--second-sidebar {
				@apply pl-8 rounded-l-2xl;
			}

		}

		.s-second-sidebar-collapsed {
			.app--second-sidebar {
				@apply w-9 h-auto min-w-0 bg-transparent pointer-events-none z-40 pt-1 px-1 border-0;
				@apply -ml-1 3xl:-ml-0.5 -mr-10 3xl:-mr-11;

				.app--second-sidebar-container {
					@apply hidden;
				}
			}

			.s-toggle-second {
				@apply relative pr-0 pl-1 pointer-events-auto top-0.5 left-0.5;
			}
		}

		.s-all-sidebars-collapsed {
			.app--content-container {
				@apply border border-ui-contrast-10 rounded-l-2xl;
			}

			.s-toggle-second {
				@apply mt-9;
			}
		}

		.s-all-sidebars-collapsed:not(.s-sidebar) {
			.s-toggle-second {
				@apply mt-0;
			}
		}

	}

	.app--content {
		@apply flex-1 overflow-hidden relative;
		@apply flex flex-row md:flex-row;
	}

	@media (orientation: portrait) {
		.app--layout {
			@apply flex-wrap gap-y-0;
		}

		.app--menu {
			@apply w-auto min-w-full min-h-fit max-w-none overflow-x-auto overflow-y-hidden pt-0 pb-2 my-0 -mx-2 pl-2 pr-0;

			.sidebar--menu--header {
				@apply min-h-[unset] h-full pl-3;
			}

			+ .app--content {
				@apply min-w-full;
			}
		}

		.app--sidebar,
		.app--second-sidebar,
		.app--content {
			.app--layout:not(.app--layout--login) & {
				height: calc(100% - 4rem);
			}
		}
	}

	.app--content-container {
		@apply w-full;
	}

	.app-layout--aside-container {
		@apply overflow-y-auto px-6 py-8;
		@apply h-full w-full;
		@apply flex flex-col;
	}

	.app--content-container.fullwidth-content {
		@apply rounded-none border-none;
	}

	.app--layout--has-aside {
		.app--content-container {
			padding-right: 50%;
		}
	}

	.app-layout--aside {
		@apply bg-ui text-ui border border-ui-secondary;
		@apply -ml-4 z-50 w-1/2 absolute top-9 right-2 bottom-2 rounded-2xl;
	}

	.app--sidebar-container,
	.app--second-sidebar-container {
		@apply overflow-hidden;
	}

	.secondary-menu--headline,
	.secondary-menu--header,
	.secondary-menu--content .text-headline,
	.app--sidebar .svws-ui-table-filter,
	.app--second-sidebar .svws-ui-table-filter {
		@apply px-7;
	}

	.app--content-container:not(.fullwidth-content) {
		@apply max-lg:px-0;
	}

	.secondary-menu--content .secondary-menu--navigation {
		@apply px-3;
	}

	@media (min-width: 2000px) {
		.secondary-menu--headline,
		.secondary-menu--header,
		.secondary-menu--content .text-headline,
		.app--sidebar .svws-ui-table-filter,
		.app--second-sidebar .svws-ui-table-filter {
			@apply px-8;
		}

		.secondary-menu--content .secondary-menu--navigation {
			@apply px-5;
		}

		.secondary-menu--content .v-table tr:not(.table--row-indent) th:first-child:not(.column--checkbox),
		.secondary-menu--content .v-table tr:not(.table--row-indent) td:first-child:not(.column--checkbox),
		.secondary-menu--content .v-table tr.table--row-indent {
			@apply pl-8;
		}
	}

	.sidebar--menu--header,
	.secondary-menu--headline,
	.svws-ui-header,
	.page--header {
		@apply flex flex-col items-start justify-center;
		min-height: 6.5rem;
	}

	.svws-ui-header {
		@apply flex-row items-center justify-between;
	}

	@media (min-width: 1280px) or (orientation: portrait) {
		.sidebar--menu--header,
		.secondary-menu--headline,
		.svws-ui-header,
		.page--header {
			min-height: 8rem;
		}
	}

	.app--content--placeholder {
		@apply w-full h-full flex justify-center items-center;

		span.icon {
			@apply w-full h-1/5 icon-light;
			max-width: 20vw;
			.dark & {
				@apply icon-gray;
			}
		}
	}

	.app-layout--notification-list {
		@apply fixed top-0 left-0 right-0 bottom-0 pointer-events-none z-50;
	}

</style>
