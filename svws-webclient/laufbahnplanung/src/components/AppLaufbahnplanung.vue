<template>
	<svws-ui-app-layout fullwidth-content>
		<template #main>
			<div class="app--page" :class="app.name">
				<div class="page--wrapper">
					<template v-if="pendingSetApp !== null">
						<svws-ui-header>
							<div class="flex items-center">
								<div>
									<span class="inline-block h-[1em] rounded-sm animate-pulse w-52 bg-ui-contrast-10" />
									<br>
									<span class="inline-block h-[1em] rounded-sm animate-pulse w-20 bg-ui-contrast-10" />
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

	import { ref } from "vue";
	import type { AppProps } from './AppProps';

	const props = defineProps<AppProps>();

	const pendingSetApp = ref<string | null>(null);

</script>

<style>

	@reference "../../../ui/src/assets/styles/index.css";
	.app--page {
		@apply flex grow flex-col justify-between;
		@apply h-screen;
		@apply overflow-hidden;
		@apply relative;
		@apply bg-ui-contrast-0;
	}

	.page--wrapper {
		@apply flex flex-col w-full h-full grow;
	}

	.page--flex {
		@apply flex flex-col w-full h-full;
	}

</style>
