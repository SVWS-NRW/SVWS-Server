<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<template #header>
					<!--<span v-if="apiStatus?.pending">...</span>-->
					<!--TODO: Statt Name den vollen Anzeigenamen anzeigen (erstellt dann automatisch eine Ausgabe der Initialien-->
					<svws-ui-menu-header :user="username" :schule="schulname" :schema="schemaname" @click="setApp(benutzerprofilApp)" class="cursor-pointer" />
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
				<template #version>{{ version }} <span v-if="version.includes('SNAPSHOT')">{{ githash.substring(0, 8) }}</span></template>
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
		<template #secondaryMenu v-if="app.hideAuswahl !== true">
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
						<svws-ui-router-tab-bar :routes="loadingSkeletonRoutes" :hidden="[]" :model-value="selectedRoute" class="loading-skeleton opacity-50" />
					</template>
					<template v-else>
						<router-view :key="app.name" />
					</template>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
	<svws-ui-notifications v-if="errors.size > 0">
		<template v-if="errors.size > 1">
			<svws-ui-button @click="errors.clear()" type="transparent" class="pointer-events-auto ml-auto rounded-lg bg-white border-light fixed right-6 left-0 top-5 z-50 w-[29rem] max-w-[75vw] justify-center">Alle {{ errors.size }} Meldungen schließen</svws-ui-button>
			<div class="min-h-[1.85rem]" />
		</template>
		<template v-for="error of [...errors.values()].reverse().slice(0, 20)" :key="error.id">
			<svws-ui-notification type="error" :id="error.id" @click="id => errors.delete(id)" :to-copy="copyString(error)">
				<template #header>
					{{ error.name }}
				</template>
				{{ error.message }}
				<template v-if="error.log !== null">
					<p v-for="log in error.log.log" :key="log || ''" v-text="log" />
				</template>
				<template #stack v-if="error.stack">
					<pre v-text="error.stack" />
				</template>
			</svws-ui-notification>
		</template>
	</svws-ui-notifications>
</template>

<script setup lang="ts">

	import { computed, ref, onErrorCaptured } from "vue";
	import type { AuswahlChildData } from './AuswahlChildData';
	import type { AppProps } from './SAppProps';
	import type { SimpleOperationResponse } from '@core';
	import { DeveloperNotificationException, OpenApiError, UserNotificationException } from '@core';
	import { githash } from '../../githash';
	import { version } from '../../version';
	import { api } from '~/router/Api';


	const props = defineProps<AppProps>();

	const schulname = computed<string>(() => {
		const name = props.schuleStammdaten.bezeichnung1;
		return name ? name : "Fehlende Bezeichnung für die Schule";
	});

	const pendingSetApp = ref('');

	const loadingSkeletonRoutes = [
		{ path: '/', name: '', component: { render: () => null }, meta: { text: '' } },
		{ path: '/loading', name: 'loading2', component: { render: () => null }, meta: { text: 'Daten laden…' } },
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


	/** Fehlerbehandlung */
	type CapturedError = {
		id: number;
		name: string;
		message: string;
		stack: string | string[];
		log: SimpleOperationResponse | null;
	};

	const counter = ref(0);
	const errors = ref<Map<number, CapturedError>>(new Map());

	function copyString(error: CapturedError) {
		const json = JSON.stringify({ env: { mode: api.mode.text, version: api.version, "Commit": api.githash }, error }, null, 2);
		return props.backticks() ? "```json\n"+json+"\n```" : json;
	}

	function errorHandler(event: ErrorEvent | PromiseRejectionEvent) {
		event.preventDefault();
		api.status.stop();
		if (event instanceof ErrorEvent)
			void createCapturedError(event.error);
		if (event instanceof PromiseRejectionEvent)
			void createCapturedError(event.reason);
	}

	// Dieser Listener gilt nur für Promises
	window.addEventListener("unhandledrejection", errorHandler);

	// Dieser Listener fängt alle anderen Fehler ab
	window.addEventListener("error", errorHandler);

	onErrorCaptured((reason) => {
		api.status.stop();
		if (reason.name === 'resetAllErrors')
			errors.value.clear();
		else
			void createCapturedError(reason);
		return false;
	});

	async function createCapturedError(reason: Error) {
		console.warn(reason);
		counter.value++;
		let name = `Fehler ${reason.name !== 'Error' ? ': ' + reason.name : ''}`;
		let message = reason.message;
		let log = null;
		if (reason instanceof DeveloperNotificationException)
			name = "Programmierfehler: Bitte melden Sie diesen Fehler."
		else if (reason instanceof UserNotificationException)
			name = "Nutzungsfehler: Dieser Fehler wurde durch eine nicht vorgesehene Nutzung der verwendeten Funktion hervorgerufen, z.B. durch unmögliche Kombinationen etc.";
		else if (reason instanceof OpenApiError) {
			name = "API-Fehler: Dieser Fehler wird durch eine fehlerhafte Kommunikation mit dem Server verursacht. In der Regel bedeutet das, dass die verschickten Daten nicht den Vorgaben entsprechen."
			if (reason.response instanceof Response) {
				try {
					let res;
					if (reason.response.headers.get('content-type') === 'application/json') {
						res = await reason.response.json();
						if ('log' in res && 'success' in res)
							log = res satisfies SimpleOperationResponse;
					}
					else
						res = await reason.response.text();
					if (res.length > 0)
						message = res;
					else
						message += ' - Status: '+reason.response.status;
				} catch(e) { void e }
			}
		}
		const newError: CapturedError = {
			id: counter.value,
			name,
			message,
			stack: reason.stack?.split("\n") || '',
			log,
		}
		errors.value.set(newError.id, newError);
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
