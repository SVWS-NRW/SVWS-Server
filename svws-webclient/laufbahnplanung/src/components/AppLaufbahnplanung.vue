<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<template #header>
					<!-- -->
				</template>
				<template #default>
					<template v-for="item in apps" :key="item.name">
						<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)">
							<template #label> {{ item.text }} </template>
							<template #icon> <app-icon :routename="item.name" /> </template>
						</svws-ui-menu-item>
					</template>
				</template>
				<template #footer>
					<!-- -->
				</template>
				<template #version>{{ version }}</template>
				<template #metaNavigation>
					<a href="https://www.svws.nrw.de/faq/impressum">
						<svws-ui-button type="transparent">
							Impressum
						</svws-ui-button>
					</a>
					<a href="#">
						<svws-ui-button type="transparent">
							Datenschutz
						</svws-ui-button>
					</a>
				</template>
			</svws-ui-menu>
		</template>
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

	import { ref } from "vue";
	import type { AuswahlChildData } from './AuswahlChildData';
	import type { AppProps } from './AppProps';
	import { version } from '../../version';

	const props = defineProps<AppProps>();

	const pendingSetApp = ref<string | null>(null);

	function is_active(current: AuswahlChildData): boolean {
		const routename = props.app.name?.toString().split('.')[0];
		const title = "Laufbahnplanung - " + current.text;
		if (routename !== current.name)
			return false;
		if (document.title !== title) {
			document.title = title;
			document.querySelector("link[rel~='icon']")?.setAttribute('href', 'favicon.svg')
		}
		return true;
	}

	async function startSetApp(app: AuswahlChildData) {
		pendingSetApp.value = app.text;
		await props.setApp(app);
		pendingSetApp.value = null;
	}

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
