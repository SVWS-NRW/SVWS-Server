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
							<template #icon> <span class="icon-lg i-ri-archive-stack-line" /> </template>
						</svws-ui-menu-item>
					</template>
				</template>
				<template #footer>
					<svws-ui-menu-item subline="" @click="doLogout">
						<template #label>Abmelden</template>
						<template #icon> <span class="icon-lg i-ri-logout-circle-line" /> </template>
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
				<div class="h-full flex flex-col">
					<div class="secondary-menu--headline">
						<h1><span>{{ pendingSetApp }}</span></h1>
					</div>
				</div>
			</template>
			<template v-else>
				<router-view :key="app.name" name="liste" />
			</template>
		</template>
		<template #main>
			<div class="app--page h-full" :class="app.name">
				<div class="h-full w-full flex flex-col grow" :class="{ 'svws-api--pending': apiStatus.pending }">
					<template v-if="pendingSetApp">
						<svws-ui-header>
							<div class="flex items-center">
								<div>
									<span class="inline-block h-[1em] rounded-sm animate-pulse w-52 bg-ui-75" />
									<br>
									<span class="inline-block h-[1em] rounded-sm animate-pulse w-20 bg-ui-75" />
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
	import type { AppProps } from './SAppProps';
	import type { TabData } from '@ui/ui/nav/TabData';
	import { version } from '../../version';

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
