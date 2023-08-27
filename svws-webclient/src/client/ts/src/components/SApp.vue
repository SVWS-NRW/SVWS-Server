<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<template #header>
					<!--<span v-if="apiStatus?.pending">...</span>-->
					<!--TODO: Statt Name den vollen Anzeigenamen anzeigen (erstellt dann automatisch eine Ausgabe der Initialien-->
					<svws-ui-menu-header :user="username" :schule="schulname" />
				</template>
				<template #default>
					<template v-for="item in apps" :key="item.name">
						<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)">
							<template #label> {{ item.text }} </template>
							<template #icon> <s-app-icon :routename="item.name" /> </template>
						</svws-ui-menu-item>
					</template>
				</template>
				<template #footer>
					<svws-ui-menu-item subline="" @click="doLogout">
						<template #label>Abmelden</template>
						<template #icon> <i-ri-logout-circle-line /> </template>
					</svws-ui-menu-item>
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
								<div class="w-20 mr-6" v-if="app.name === 'schueler' || app.name === 'lehrer'">
									<div class="inline-block h-20 rounded-xl animate-pulse w-20 bg-black/5 dark:bg-white/5" />
								</div>
								<div>
									<span class="inline-block h-[1em] rounded animate-pulse w-52 bg-black/10 dark:bg-white/10" />
									<br>
									<span class="inline-block h-[1em] rounded animate-pulse w-20 bg-black/5 dark:bg-white/5" />
								</div>
							</div>
						</svws-ui-header>
						<svws-ui-router-tab-bar :routes="loadingSkeletonRoutes" :hidden="[]" :model-value="selectedRoute" class="loading-skeleton" />
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

	import type { AuswahlChildData } from './AuswahlChildData';
	import type { ComputedRef } from "vue";
	import type { AppProps } from './SAppProps';
	import {computed, onMounted, ref} from "vue";
	import { version } from '../../version';

	const props = defineProps<AppProps>();

	const schulname: ComputedRef<string> = computed(() => {
		const name = props.schuleStammdaten.bezeichnung1;
		return name ? name : "Fehlende Bezeichnung für die Schule";
	});

	const pendingSetApp = ref('');

	const loadingSkeletonRoutes = [
		{ path: '/', name: '', component: { render: () => null }, meta: { text: 'Daten laden…' } },
		{ path: '/loading', name: 'loading2', component: { render: () => null }, meta: { text: '' } },
	];
	const selectedRoute = loadingSkeletonRoutes[0];

	function is_active(current: AuswahlChildData): boolean {
		const routename = props.app.name?.toString().split('.')[0];
		const title = current.text + " - " + schulname.value;
		if ((props.app.name === 'benutzer' || props.app.name === 'benutzergruppen') && current.name === 'schule')
			return true;
		if (routename !== current.name)
			return false;
		if (document.title !== title) {
			document.title = title;
			document.querySelector("link[rel~='icon']")?.setAttribute('href', 'favicon' + (props.app.name === 'statistik' ? '-statistik' : '') + '.svg')
		}
		return true;
	}

	async function startSetApp(app: AuswahlChildData) {
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
