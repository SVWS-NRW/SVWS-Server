<script setup lang='ts'>
const {
	fullwidthContent = false,
} = defineProps<{
	fullwidthContent?: boolean;
}>();
</script>
<template>
	<div class="app-layout--wrapper">
		<div v-if="$slots.sidebar" class="app-layout--sidebar-wrapper">
			<slot name="sidebar" />
		</div>
		<div v-if="$slots.secondaryMenu" class="app-layout--secondary">
			<div class="app-layout--secondary-container">
				<slot name="secondaryMenu" />
			</div>
		</div>
		<main class="app-layout--main">
			<div class="app-layout--main-container" :class="{'fullwidth-content' : fullwidthContent}">
				<slot name="main" />
			</div>
		</main>
		<slot name="contentSidebar" />
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
		max-width: 36rem;
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
</style>
