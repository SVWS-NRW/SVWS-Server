<script setup lang='ts'>
	import {ref} from "vue";

	const {
		fullwidthContent = false,
	} = defineProps<{
		fullwidthContent?: boolean;
	}>();

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
	<div class="app-layout--wrapper">
		<div v-if="$slots.sidebar && !contentIsFullscreen" class="app-layout--sidebar-wrapper">
			<slot name="sidebar" />
		</div>
		<div v-if="$slots.secondaryMenu && !contentIsFullscreen" class="app-layout--secondary">
			<div class="app-layout--secondary-container">
				<slot name="secondaryMenu" />
			</div>
		</div>
		<main class="app-layout--main">
			<div class="app-layout--main-container relative" :class="{'fullwidth-content' : fullwidthContent}">
				<slot name="main" />
				<!-- Experiment: Fullscreen Button, der den Content-Bereich auf die gesamte Breite des Browsers ausdehnt
				<svws-ui-button type="transparent" @click="toggleFullscreen" class="cursor-pointer absolute top-4 right-10 text-sm">
					<Icon v-if="!contentIsFullscreen"><i-ri-fullscreen-line/></Icon>
					<Icon class="flex" v-if="contentIsFullscreen">Fullscreen deaktivieren<i-ri-fullscreen-exit-line/></Icon>
				</svws-ui-button>-->
			</div>
		</main>
	</div>
</template>

<style lang="postcss">
	html {
		font-size: 10px;
	}

	@media (min-width: 1280px) {
		html {
			font-size: 12px;
		}
	}

	.page-wrapper {
		@apply flex flex-grow flex-col justify-between;
		@apply h-screen;
		@apply overflow-hidden;
		@apply relative;
		@apply bg-white lg:pl-3;
	}

	.app-layout--wrapper {
		@apply relative h-screen w-full overflow-hidden;
		@apply flex flex-row gap-2 p-2;
		@apply bg-light;
	}

	.app-layout--secondary {
		@apply w-1/5 flex-shrink-0;
		min-width: 22rem;
		max-width: 32rem;
	}

	.app-layout--main {
		@apply flex-1 overflow-hidden;
	}

	.app-layout--secondary-container,
	.app-layout--main-container {
		@apply bg-white rounded-2xl;
		@apply h-full w-full;
		@apply flex flex-col;
		@apply shadow-dark-20 shadow-sm;
		@apply border border-dark-20 border-opacity-60;
		@apply overflow-y-auto;
		-webkit-overflow-scrolling: touch;
	}

	.app-layout--secondary-container {
		@apply overflow-hidden;
	}

	.app-layout--main-container:not(.fullwidth-content),
	.secondary-menu--headline,
	.secondary-menu--header,
	.secondary-menu--content .text-headline {
		@apply px-6;
	}

	.app-layout--main-container:not(.fullwidth-content) {
		@apply max-lg:px-0;
	}

	.secondary-menu--content .secondary-menu--navigation {
		@apply px-3;
	}

	.secondary-menu--content .v-table tr:not(.table--row-indent) th:first-child:not(.column--checkbox),
	.secondary-menu--content .v-table tr:not(.table--row-indent) td:first-child:not(.column--checkbox),
	.secondary-menu--content .v-table tr.table--row-indent {
		@apply pl-6;
	}

	@media (min-width: 2000px) {
		.app-layout--main-container:not(.fullwidth-content) {
			@apply px-[1vw];
		}

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

		.app-layout--wrapper {
			@apply gap-3 p-3;
		}
	}

	.sidebar--menu--header,
	.secondary-menu--headline,
	.header--wrapper {
		@apply flex flex-col items-start justify-center;
		min-height: 6.5rem;
	}

	@media (min-width: 1280px) or (orientation: portrait) {
		.sidebar--menu--header,
		.secondary-menu--headline,
		.header--wrapper {
			min-height: 8rem;
		}
	}

	.app-layout--main--placeholder {
		@apply w-full h-full flex justify-center items-center;
		/*background: radial-gradient(ellipse 50% 50%, rgba(var(--color-dark-20), 1) 0%, rgba(var(--color-dark-20), 0) 100%);*/
		background-image: radial-gradient(ellipse 25% 4% at 50% 58%, rgba(var(--color-dark-20), 0.6) 0%, rgba(var(--color-dark-20), 0) 100%);

		svg {
			@apply w-full h-1/5 text-dark-20;
			max-width: 20vw;
		}
	}

	.app-layout--main-sidebar {
		@apply fixed top-0 right-0 bottom-0 z-30 p-6 h-full flex flex-col;
		max-width: 32vw;
	}

	.app-layout--main-sidebar--container {
		@apply rounded-xl h-full overflow-hidden flex flex-col;
		@apply shadow-lg bg-white;
		@apply border border-dark-20 border-opacity-60;
	}

	.app-layout--main-sidebar--content {
		@apply bg-light h-full p-3;
		@apply overflow-y-auto;
		-webkit-overflow-scrolling: touch;
	}

	.app-layout--main-sidebar--trigger {
		@apply pointer-events-auto;
		@apply w-full cursor-pointer relative;
		@apply p-6 pl-3;
		@apply text-headline;
	}

	.sidebar-trigger--text {
		@apply flex items-center opacity-50;

		.button--icon {
			@apply p-1;
			width: 2.5rem;
			height: 2.5rem;

			svg {
				width: 2rem;
				height: 2rem;
			}
		}
	}

	.app-layout--main-sidebar--collapsed {
		@apply pointer-events-none;
		@apply py-4 pr-4 rounded-none;

		.app-layout--main-sidebar--container {
			@apply shadow-transparent border-none bg-transparent pr-8 pt-8;
		}

		.app-layout--main-sidebar--trigger {
			@apply px-6 py-2 rounded-full bg-primary text-white text-base font-bold cursor-pointer border-2;

			&:hover,
			&:focus {
				@apply bg-light text-primary;
			}

			&:focus {
				@apply outline-none ring ring-primary ring-opacity-50;
			}
		}

		.sidebar-trigger--text {
			@apply whitespace-nowrap opacity-100;
		}

		.app-layout--main-sidebar--content {
			@apply hidden;
		}
	}

	.app-layout--main-sidebar--trigger-count {
		@apply text-white font-bold rounded-full text-sm text-center w-auto;
		@apply absolute;
		background-color: #f00;
		padding: 0.1em 0.4em;
		top: -0.5em;
		right: -0.5em;
		min-width: 1.6em;
	}
</style>
