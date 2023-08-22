<template>
	<div class="app--layout" :class="{'app--layout--has-aside': $slots.aside, 'loading-skeleton pointer-events-none': skeleton, 's-sidebar-expanded': sidebarExpanded, 's-sidebar-collapsed': !sidebarExpanded}">
		<div v-if="($slots.sidebar && !fullwidthContent) || skeleton" class="app--menu">
			<slot name="sidebar" />
			<template v-if="skeleton">
				<svws-ui-menu>
					<template #header>
						<div class="flex items-center justify-center w-full">
							<span class="inline-block h-12 rounded-xl animate-pulse w-12 bg-black/5 dark:bg-white/5" />
						</div>
					</template>
					<template #default>
						<svws-ui-menu-item :active="false">
							<template #label>
								<span class="inline-block h-3 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10" />
							</template>
							<template #icon>
								<span class="inline-block h-7 rounded-full animate-pulse w-7 bg-black/5 dark:bg-white/5" />
							</template>
						</svws-ui-menu-item>
						<svws-ui-menu-item :active="false">
							<template #label>
								<span class="inline-block h-3 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10" />
							</template>
							<template #icon>
								<span class="inline-block h-7 rounded-full animate-pulse w-7 bg-black/5 dark:bg-white/5" />
							</template>
						</svws-ui-menu-item>
						<svws-ui-menu-item :active="false">
							<template #label>
								<span class="inline-block h-3 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10" />
							</template>
							<template #icon>
								<span class="inline-block h-7 rounded-full animate-pulse w-7 bg-black/5 dark:bg-white/5" />
							</template>
						</svws-ui-menu-item>
					</template>
					<template #footer>
						<svws-ui-menu-item :active="false">
							<template #label>
								<span class="inline-block h-3 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10" />
							</template>
							<template #icon>
								<span class="inline-block h-7 rounded-full animate-pulse w-7 bg-black/5 dark:bg-white/5" />
							</template>
						</svws-ui-menu-item>
					</template>
					<template #version>
						<span class="inline-block h-4 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10" />
					</template>
				</svws-ui-menu>
			</template>
		</div>
		<div v-if="($slots.secondaryMenu && !fullwidthContent) || skeleton" class="app--sidebar">
			<div class="s-toggle" v-if="!skeleton">
				<button type="button" @click="updateSidebarExpanded" :title="`Sidebar ${!sidebarExpanded ? 'einblenden' : 'ausblenden'}`">
					<i-ri-contract-left-line v-if="sidebarExpanded" />
					<i-ri-corner-up-right-double-line v-else />
					<span class="s-title" v-if="!sidebarExpanded">Sidebar einblenden</span>
				</button>
			</div>
			<div class="app--sidebar-container">
				<slot name="secondaryMenu" />
				<template v-if="skeleton">
					<svws-ui-secondary-menu>
						<template #headline>
							<span>SVWS NRW</span>
						</template>
						<template #abschnitt>
							<span class="inline-block h-4 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10 -mb-1" />
						</template>
					</svws-ui-secondary-menu>
				</template>
			</div>
		</div>
		<main class="app--content">
			<div class="app--content-container relative" :class="{'fullwidth-content' : fullwidthContent}">
				<div class="app--page" v-if="skeleton">
					<svws-ui-header>
						<div class="flex items-center">
							<div class="w-20 mr-6">
								<div class="inline-block h-20 rounded-xl animate-pulse w-20 bg-black/5 dark:bg-white/5" />
							</div>
							<div class="animate-pulse">
								<span>Daten</span>
								<br>
								<span class="opacity-40">werden geladen…</span>
							</div>
						</div>
					</svws-ui-header>
				</div>
				<slot name="main" v-else />
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
	import {ref} from "vue";

	const props = withDefaults(defineProps<{
		skeleton?: boolean;
		fullwidthContent?: boolean;
	}>(), {
		skeleton: false,
		fullwidthContent: false
	});

	const sidebarExpanded = ref<boolean>(true);

	const updateSidebarExpanded = () => {
		sidebarExpanded.value = !sidebarExpanded.value;
		localStorage.setItem('sidebarExpanded', sidebarExpanded.value ? 'true' : 'false');
	};

	if (localStorage.getItem('sidebarExpanded')) {
		sidebarExpanded.value = localStorage.getItem('sidebarExpanded') === 'true';
	}

</script>

<style lang="postcss">
	html {
		font-size: 10px;
		@apply bg-light;

		&.dark {
			@apply bg-[#000] text-white;
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
		@apply relative h-screen w-full overflow-hidden;
		@apply flex flex-row gap-2 p-2;
		/*@apply max-w-[220rem] mx-auto;*/
	}

	.app--menu {
		@apply overflow-y-auto -mr-3 pr-3 -my-3 py-3;
		min-width: 4rem;
		width: 4%;
		max-width: 8rem;
	}

	.app--sidebar,
	.app--content-container {
		@apply bg-white dark:bg-black rounded-2xl;
		@apply h-full;
		@apply flex flex-col;
		@apply border border-black/10 dark:border-white/5;
		@apply overflow-y-auto;
	}

	.app--content-container {
		@apply border-l-0 rounded-l-none;
	}

	.app--sidebar {
		@apply flex-shrink-0 -mr-2 rounded-r-none p-2 pr-0 h-full border-r-0 relative;
		width: 20%;
		min-width: 24rem;
		max-width: 36rem;

		.app--sidebar-container {
			@apply rounded-xl border border-black/25 dark:border-white/25 h-full shadow shadow-black/10;
		}

		.secondary-menu--headline {
			@apply -mt-2;
		}

		.s-toggle {
			@apply absolute right-0 top-0 z-40 pt-3.5 pr-1.5 flex flex-col;

			button {
				@apply rounded-lg text-black/25 dark:text-white/25 p-0.5 inline-flex flex-col items-center gap-1 text-headline-sm;

				svg {
					@apply flex-shrink-0 text-headline-md;
				}

				&:hover,
				&:focus-visible {
					@apply bg-black/10 dark:bg-white/10 text-black dark:text-white;
				}
			}

			.s-title {
				writing-mode: sideways-lr;
			}
		}
	}

	.s-sidebar-collapsed {
		.app--sidebar {
			@apply w-9 min-w-0 bg-white dark:bg-black z-40 pt-1 px-1;
			@apply -mr-3 lg:-mr-4 3xl:-mr-9 4xl:-mr-12;

			.app--sidebar-container {
				@apply hidden;
			}
		}

		.s-toggle {
			@apply relative pt-1 pr-0 pl-1;

			button {
				@apply pb-1 rounded-lg;
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
			@apply w-auto min-w-full h-16 max-w-none overflow-x-auto overflow-y-hidden pt-0 pb-2 my-0 -mx-2 pl-2 pr-0;

			.sidebar--menu--header {
				@apply min-h-[unset] h-full pl-3;
			}

			+ .app--content {
				@apply min-w-full;
			}
		}

		.app--sidebar,
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
		@apply overflow-y-auto;
		@apply h-full w-full;
		@apply flex flex-col py-5;
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
		@apply -ml-4 z-50 w-1/2 p-3 absolute top-0 right-0 bottom-0;
		@apply shadow-2xl shadow-black/25 dark:shadow-white/25 bg-white dark:bg-black border-l border-black/25 dark:border-white/25;
	}

	.app--sidebar-container {
		@apply overflow-hidden;
	}

	.secondary-menu--headline,
	.secondary-menu--header,
	.secondary-menu--content .text-headline {
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
		.secondary-menu--content .text-headline {
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

		.app--layout {
			@apply gap-3 p-3;
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

		svg {
			@apply w-full h-1/5 text-light dark:text-white/5;
			max-width: 20vw;
		}
	}

	.app-layout--notification-list {
		@apply fixed top-0 left-0 right-0 bottom-0 pointer-events-none z-50;
	}
</style>
