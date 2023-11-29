<template>
	<svws-ui-app-layout fullwidth-content>
		<template #main>
			<div class="app--page" :class="app.name">
				<div class="page--wrapper">
					<template v-if="pendingSetApp !== null">
						<svws-ui-header>
							<div class="flex items-center">
								<div>
									<span class="inline-block h-[1em] rounded animate-pulse w-52 bg-black/10 dark:bg-white/10" />
									<br>
									<span class="inline-block h-[1em] rounded animate-pulse w-20 bg-black/5 dark:bg-white/5" />
								</div>
							</div>
						</svws-ui-header>
					</template>
					<template v-else>
						<router-view :key="app.name" />
					</template>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import type { AppProps } from './AppProps';
	import { ref } from "vue";
	import { version } from '../../version';

	const props = defineProps<AppProps>();

	const pendingSetApp = ref<string | null>(null);

</script>

<style lang="postcss">

	.app--page {
		@apply flex flex-grow flex-col justify-between;
		@apply h-screen;
		@apply overflow-hidden;
		@apply relative;
		@apply bg-white dark:bg-black;
	}

	.page--wrapper {
		@apply flex flex-col w-full h-full flex-grow;
	}

	.page--flex {
		@apply flex flex-col w-full h-full;
	}

</style>
