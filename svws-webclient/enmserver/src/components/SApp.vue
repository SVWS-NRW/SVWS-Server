<template>
	<svws-ui-app-layout no-secondary-menu :tertiary-menu="showAuswahlliste()" secondary-menu-small tertiary-menu-small>
		<template #sidebar>
			<svws-ui-menu :focus-switching-enabled :focus-help-visible>
				<template #header>
					<svws-ui-menu-header :user="username" class="cursor-pointer" />
				</template>
				<template #default>
					<template v-for="item in apps" :key="item.name">
						<template v-if="item.name !== 'einstellungen'">
							<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)">
								<template #icon>
									<span class="inline-block icon-lg i-ri-book-2-line" v-if="item.name === 'leistungen'" />
									<span class="inline-block icon-lg i-ri-book-2-line" v-if="item.name === 'teilleistungen'" />
									<span class="inline-block icon-lg i-ri-book-2-line" v-if="item.name === 'klassenleitung'" />
								</template>
								<template #label><span class="text-xs"> {{ item.text }}</span> </template>
							</svws-ui-menu-item>
						</template>
					</template>
				</template>
				<template #footer>
					<template v-for="item in apps" :key="item.name">
						<template v-if="item.name === 'einstellungen'">
							<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)">
								<template #icon><span class="inline-block icon-lg i-ri-settings-3-line" /></template>
								<template #label><span class="text-xs"> {{ item.text }}</span> </template>
							</svws-ui-menu-item>
						</template>
					</template>
					<svws-ui-menu-item subline="" @click="doLogout">
						<template #label>Abmelden</template>
						<template #icon> <span class="icon-lg i-ri-logout-circle-line inline-block" /> </template>
					</svws-ui-menu-item>
				</template>
				<template #version>
					<div class="flex gap-1">
						<div class="mt-1">{{ version }} <a :href="`https://github.com/SVWS-NRW/SVWS-Server/commit/${githash}`" v-if="version.includes('SNAPSHOT')">{{ githash.substring(0, 8) }}</a></div>
						<svws-ui-button type="transparent" @click="copyToClipboard">
							<span class="icon i-ri-file-copy-line" v-if="copied === null" />
							<span class="icon i-ri-error-warning-fill" v-else-if="copied === false" />
							<span class="icon i-ri-check-line icon-primary" v-else />
						</svws-ui-button>
					</div>
				</template>
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
		<template #tertiaryMenu v-if="app.hide !== true">
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
			<main class="app--page" :class="app.name" role="main">
				<div v-show="pendingSetApp" class="page--wrapper" :class="{'svws-api--pending': apiStatus.pending}">
					<svws-ui-header>
						<div class="flex items-center">
							<div class="w-20 mr-6" v-if="(app.name === 'schueler') || (app.name === 'lehrer')">
								<div class="inline-block h-20 rounded-xl animate-pulse w-20 bg-black/5 dark:bg-white/5" />
							</div>
							<div>
								<span class="inline-block h-[1em] rounded animate-pulse w-52 bg-black/10 dark:bg-white/10" />
								<br>
								<span class="inline-block h-[1em] rounded animate-pulse w-20 bg-black/5 dark:bg-white/5" />
							</div>
						</div>
					</svws-ui-header>
				</div>
				<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" class="region-enumeration">8</p>
				<div v-show="!pendingSetApp" class="page--wrapper" :class="{'svws-api--pending': apiStatus.pending, 'focus-region': focusSwitchingEnabled, 'highlighted': focusHelpVisible}">
					<router-view :key="app.name" />
				</div>
			</main>
		</template>
	</svws-ui-app-layout>
	<svws-ui-notifications v-if="errors.size > 0">
		<div v-if="errors.size > 1" class="bg-white">
			<svws-ui-button @click="errors.clear()" type="transparent" class="pointer-events-auto ml-auto rounded-lg bg-white border-light fixed right-6 left-0 top-5 z-50 w-[29rem] max-w-[75vw] justify-center">Alle {{ errors.size }} Meldungen schließen</svws-ui-button>
			<div class="min-h-[1.85rem]" />
		</div>
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

	import { onMounted, onUnmounted, ref, onErrorCaptured } from "vue";
	import type { AppProps } from './SAppProps';
	import { githash } from '../../githash';
	import { version } from '../../version';
	import { api } from '~/router/Api';
	import { useRegionSwitch } from "~/components/useRegionSwitch";
	import type { TabData } from "@ui/ui/nav/TabData";
	import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
	import { OpenApiError } from "@core/api/OpenApiError";

	const props = defineProps<AppProps>();

	const { focusHelpVisible, focusSwitchingEnabled , enable, disable } = useRegionSwitch();

	const pendingSetApp = ref('');
	const copied = ref<boolean|null>(null);

	async function copyToClipboard() {
		try {
			await navigator.clipboard.writeText(`${version} ${githash}`);
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}

	function is_active(current: TabData): boolean {
		const routename = props.app.name.split('.')[0];
		const title = current.text;
		if (routename !== current.name)
			return false;
		if (document.title !== title) {
			document.title = title;
			document.querySelector("link[rel~='icon']")?.setAttribute('href', 'favicon.svg')
		}
		return true;
	}

	const hideAuswahlliste = new Set<string>([ "statistik" ]);

	function showAuswahlliste() : boolean {
		return !hideAuswahlliste.has(props.selectedChild.name);
	}

	async function startSetApp(app: TabData) {
		pendingSetApp.value = app.text;
		await props.setApp(app);
		pendingSetApp.value = '';
	}

	async function doLogout() {
		document.title = "Abmelden…";
		await props.logout();
		document.title = "ENM - Externes Notenmodul";
	}

	//* Fehlerbehandlung */
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
		const json = JSON.stringify({ env: { mode: api.mode.text, version: api.version, commit: api.githash }, error }, null, 2);
		return "```json\n"+json+"\n```";
	}

	function errorHandler(event: ErrorEvent | PromiseRejectionEvent) {
		event.preventDefault();
		api.status.stop();
		console.log(event)
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

	onMounted(() => {
		enable();
	})

	onUnmounted(() => {
		disable();
	})

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
		const newError: CapturedError = { id: counter.value, name, message, stack: reason.stack?.split("\n") || '', log }
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

	span.icon, span.icon-lg {
		.dark & {
			@apply icon-white;
		}
	}

</style>
