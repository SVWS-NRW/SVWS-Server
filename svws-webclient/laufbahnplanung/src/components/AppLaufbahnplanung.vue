<template>
	<svws-ui-app-layout fullwidth-content>
		<template #main>
			<div class="app--page h-full" :class="app.name">
				<div class="flex flex-col w-full h-full grow">
					<template v-if="pendingSetApp !== null">
						<svws-ui-header>
							<div class="flex items-center">
								<div>
									<span class="inline-block h-[1em] rounded-sm animate-pulse w-52 bg-ui-10" />
									<br>
									<span class="inline-block h-[1em] rounded-sm animate-pulse w-20 bg-ui-10" />
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
