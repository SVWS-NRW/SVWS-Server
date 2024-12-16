<template>
	<svws-ui-app-layout :no-secondary-menu="app.name === 'config'">
		<template #sidebar>
			<svws-ui-menu>
				<template #header>
					<svws-ui-menu-header :user="username" schule="Admin-Client" is-admin-client />
				</template>
				<template #default>
					<template v-for="item in apps" :key="item.name">
						<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)" v-if="isVisible(item)">
							<template #label> {{ item.text }} </template>
							<template #icon> <span class="icon-lg i-ri-archive-stack-line inline-block" /> </template>
						</svws-ui-menu-item>
					</template>
				</template>
				<template #footer>
					<svws-ui-menu-item subline="" @click="doLogout">
						<template #label>Abmelden</template>
						<template #icon> <span class="icon-lg i-ri-logout-circle-line inline-block" /> </template>
					</svws-ui-menu-item>
				</template>
				<template #version>{{ version }}</template>
				<template #metaNavigation>
					<a href="https://www.svws.nrw.de/faq/impressum">
						<svws-ui-button type="transparent">
							Impressum
						</svws-ui-button>
					</a>
					<datenschutz-modal v-slot="{ openModal }">
						<svws-ui-button type="transparent" @click="openModal()">
							Datenschutz
						</svws-ui-button>
					</datenschutz-modal>
				</template>
			</svws-ui-menu>
		</template>
		<template #secondaryMenu>
			<template v-if="pendingSetApp">
				<svws-ui-secondary-menu>
					<template #headline>
						<span>{{ pendingSetApp }}</span>
					</template>
					<template #abschnitt>
						<span class="inline-block h-4 rounded animate-pulse w-16 bg-black/10 dark:bg-white/10 -mb-1" />
					</template>
				</svws-ui-secondary-menu>
			</template>
			<template v-else>
				<router-view :key="app.name" name="liste" />
			</template>
		</template>
		<template #main>
			<div class="app--page" :class="app.name">
				<div class="page--wrapper" :class="{'svws-api--pending': apiStatus.pending}">
					<template v-if="pendingSetApp">
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

	import type { AppProps } from './SAppProps';
	import { ref } from "vue";
	import { version } from '../../version';
	import type { TabData } from '../../../ui/src/ui/nav/TabData';

	const props = defineProps<AppProps>();

	const pendingSetApp = ref('');

	function isVisible(item: TabData) : boolean {
		if ((item.name === 'config') && (!props.isServerAdmin))
			return false;
		return true;
	}

	function is_active(current: TabData): boolean {
		const routename = props.app.name.split('.')[0];
		const title = current.text;
		if (routename !== current.name)
			return false;
		if (document.title !== title)
			document.title = title;
		return true;
	}

	async function startSetApp(app: TabData) {
		pendingSetApp.value = app.text;
		await props.setApp(app);
		pendingSetApp.value = '';
	}

	async function doLogout() {
		document.title = "Abmelden…";
		await props.logout();
		document.title = "SVWS NRW";
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
