<script setup lang='ts'>
	import {ref} from "vue";

	const props = withDefaults(defineProps<{
		fullwidthContent?: boolean;
	}>(), {
		fullwidthContent: false,
	});

	const contentIsFullscreen = ref(false);

	function toggleFullscreen() {
		contentIsFullscreen.value = !contentIsFullscreen.value;
		window.localStorage.setItem(
			"fullscreen",
			String(contentIsFullscreen.value)
		);
	}
</script>
<template>
	<div class="app--layout" :class="{'app--layout--has-aside': $slots.aside}">
		<div v-if="$slots.sidebar && !contentIsFullscreen" class="app--menu">
			<slot name="sidebar" />
		</div>
		<div v-if="$slots.secondaryMenu && !contentIsFullscreen" class="app--sidebar">
			<div class="app--sidebar-container">
				<slot name="secondaryMenu" />
			</div>
		</div>
		<main class="app--content">
			<div class="app--content-container relative" :class="{'fullwidth-content' : fullwidthContent}">
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

<style lang="postcss">
	html {
		font-size: 10px;
		@apply bg-light;
	}

	@media (min-width: 1280px) {
		html {
			font-size: 12px;
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

	.app--sidebar {
		@apply flex-shrink-0;
		width: 20%;
		min-width: 24rem;
		max-width: 36rem;
	}

	.app--content {
		@apply flex-1 overflow-hidden relative;
		@apply flex flex-row md:flex-row;
	}

	.app--sidebar-container,
	.app--content-container {
		@apply bg-white rounded-2xl;
		@apply h-full w-full;
		@apply flex flex-col;
		@apply border border-black/25;
		@apply overflow-y-auto;
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
		@apply shadow-2xl shadow-black/25 bg-white border-l border-black/25;
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
	.page--header {
		@apply flex flex-col items-start justify-center;
		min-height: 6.5rem;
	}

	@media (min-width: 1280px) or (orientation: portrait) {
		.sidebar--menu--header,
		.secondary-menu--headline,
		.page--header {
			min-height: 8rem;
		}
	}

	.app--content--placeholder {
		@apply w-full h-full flex justify-center items-center;
		/*background: radial-gradient(ellipse 50% 50%, rgba(var(--color-dark-20), 1) 0%, rgba(var(--color-dark-20), 0) 100%);*/
		/*background-image: radial-gradient(ellipse 25% 4% at 50% 58%, rgba(var(--color-dark-20), 0.6) 0%, rgba(var(--color-dark-20), 0) 100%);*/

		svg {
			@apply w-full h-1/5 text-light;
			max-width: 20vw;
		}
	}

	.app-layout--notification-list {
		@apply fixed top-0 left-0 right-0 bottom-0 pointer-events-none z-50;
	}
</style>
