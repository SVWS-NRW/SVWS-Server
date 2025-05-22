<template>
	<svws-ui-app-layout :no-secondary-menu="!menu.hasSubmenu" :tertiary-menu="menu.hasAuswahlliste" secondary-menu-small>
		<template #sidebar>
			<svws-ui-menu :focus-switching-enabled :focus-help-visible>
				<template #header>
					<svws-ui-menu-header v-if="menu.benutzerprofil !== null" :user="username" :schule="schulname" :schema="schemaname" @click="startSetApp(menu.benutzerprofil)" class="cursor-pointer" />
				</template>
				<template #default>
					<template v-for="item in menu.main" :key="item.name">
						<svws-ui-menu-item :active="menu.mainEntry.name === item.name" @click="startSetApp(item)">
							<template #icon><span class="icon-lg" :class="getIcon(item)" /></template>
							<template #label><span class="text-xs"> {{ item.text }}</span> </template>
						</svws-ui-menu-item>
					</template>
				</template>
				<template #footer>
					<template v-if="menu.einstellungen !== null">
						<svws-ui-menu-item :active="menu.mainEntry.name === menu.einstellungen.name" @click="startSetApp(menu.einstellungen)">
							<template #icon><span class="icon-lg" :class="getIcon(menu.einstellungen)" /></template>
							<template #label><span class="text-xs"> {{ menu.einstellungen.text }}</span> </template>
						</svws-ui-menu-item>
					</template>
					<svws-ui-menu-item subline="" @click="doLogout">
						<template #label>Abmelden</template>
						<template #icon><span class="icon-lg i-ri-logout-circle-line" /></template>
					</svws-ui-menu-item>
				</template>
				<template #version>
					<div class="flex gap-1">
						<div class="mt-1">{{ version }}<span v-if="version.includes('SNAPSHOT')">&nbsp;{{ servermode.name() }}-Mode&nbsp;<a :href="`https://github.com/SVWS-NRW/SVWS-Server/commit/${githash}`">{{ githash.substring(0, 8) }}</a></span></div>
						<svws-ui-button type="transparent" @click="copyToClipboard">
							<span class="icon i-ri-file-copy-line" v-if="copied === null" />
							<span class="icon i-ri-error-warning-fill" v-else-if="copied === false" />
							<span class="icon i-ri-check-line icon-ui-brand" v-else />
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
		<template #secondaryMenu v-if="menu.hasSubmenu">
			<template v-if="pendingSetApp">
				<div class="h-full flex flex-col">
					<div class="secondary-menu--headline">
						<h1><span>{{ pendingSetApp }}</span></h1>
						<div><span class="inline-block h-4 rounded-sm animate-pulse w-16 -mb-1" /></div>
					</div>
				</div>
			</template>
			<template v-else>
				<div class="h-full flex flex-col">
					<div class="secondary-menu--headline">
						<h1> {{ menu.mainEntry.text }} </h1>
						<div><abschnitt-auswahl :daten="schuljahresabschnittsauswahl" /></div>
					</div>
					<div class="secondary-menu--header" />
					<div class="secondary-menu--content">
						<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" class="region-enumeration">2</p>
						<svws-ui-secondary-menu-navigation class="focus-region" :class="{'highlighted': focusHelpVisible}" :tab-manager="(menu.current.name.startsWith('schule') ? tabManagerSchule : tabManagerEinstellungen)" />
					</div>
				</div>
			</template>
		</template>
		<template #tertiaryMenu v-if="menu.current.hide !== true">
			<template v-if="pendingSetApp">
				<div class="h-full flex flex-col">
					<div class="secondary-menu--headline">
						<h1><span>{{ pendingSetApp }}</span></h1>
						<div><span class="inline-block h-4 rounded-sm animate-pulse w-16 -mb-1 bg-ui-75" /></div>
					</div>
				</div>
			</template>
			<template v-else>
				<router-view :key="menu.current.name" name="liste" />
			</template>
		</template>
		<template #main>
			<main class="app--page h-full" :class="menu.current.name" role="main">
				<div v-show="pendingSetApp" class="flex flex-col w-full h-full grow" :class="{'svws-api--pending': apiStatus.pending}">
					<svws-ui-header>
						<div class="flex items-center">
							<div class="w-20 mr-6" v-if="(menu.current.name === 'schueler') || (menu.current.name === 'lehrer')">
								<div class="inline-block h-20 rounded-xl animate-pulse w-20 bg-ui-75" />
							</div>
							<div>
								<span class="inline-block h-[1em] rounded-sm animate-pulse w-52 bg-ui-75" />
								<br>
								<span class="inline-block h-[1em] rounded-sm animate-pulse w-20 bg-ui-75" />
							</div>
						</div>
					</svws-ui-header>
				</div>
				<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" class="region-enumeration">8</p>
				<div v-show="!pendingSetApp" class="flex flex-col w-full h-full grow overflow-hidden" :class="{'svws-api--pending': apiStatus.pending, 'focus-region': focusSwitchingEnabled, 'highlighted': focusHelpVisible}">
					<router-view :key="menu.current.name" />
				</div>
			</main>
		</template>
	</svws-ui-app-layout>
	<svws-ui-notifications v-if="errors.size > 0">
		<div v-if="errors.size > 1" class="bg-ui">
			<svws-ui-button @click="errors.clear()" type="transparent" class="pointer-events-auto ml-auto rounded-lg bg-ui border-ui-25 fixed right-6 left-0 top-5 z-50 w-full justify-center">Alle {{ errors.size }} Meldungen schließen</svws-ui-button>
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

	import { computed, onMounted, onUnmounted, ref, onErrorCaptured, watch } from "vue";
	import type { TabData } from "@ui";
	import type { AppProps } from './SAppProps';
	import type { SimpleOperationResponse } from '@core';
	import { DeveloperNotificationException, OpenApiError, UserNotificationException } from '@core';
	import { githash } from '../../githash';
	import { version } from '../../version';
	import { api } from '~/router/Api';
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<AppProps>();

	const { focusHelpVisible, focusSwitchingEnabled , enable, disable } = useRegionSwitch();
	onMounted(() => enable());
	onUnmounted(() => disable());

	watch(() => props.menu.current.name, (m) => {
		const mainText = props.menu.mainEntry.text;
		const subText = props.menu.current.text;
		const title = mainText + " - " + ((mainText !== subText) ? subText + " - " : "") + schulname.value;
		if (document.title !== title)
			document.title = title;
	});

	const schulname = computed<string>(() => {
		const name = props.schuleStammdaten.bezeichnung1;
		return (name.length > 0) ? name : "Fehlende Bezeichnung für die Schule";
	});

	const pendingSetApp = ref('');
	const copied = ref<boolean|null>(null);

	function getIcon(menu: TabData) : string {
		switch(menu.image) {
			case "i-ri-school-line":
			case "i-ri-group-line":
			case "i-ri-briefcase-line":
			case "i-ri-team-line":
			case "i-ri-book-2-line":
			case "i-ri-graduation-cap-line":
			case "i-ri-bar-chart-2-line":
			case "i-ri-calendar-event-line":
			case "i-ri-settings-3-line":
				return menu.image;
			default: return "";
		}
	}

	async function copyToClipboard() {
		try {
			await navigator.clipboard.writeText(`${version} ${githash}`);
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}

	async function startSetApp(app: TabData) {
		pendingSetApp.value = app.text;
		await props.menu.setEintrag(app);
		pendingSetApp.value = '';
	}

	async function doLogout() {
		document.title = "Abmelden…";
		await props.logout();
		document.title = "SVWS NRW";
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
				const text = await reason.response.text();
				try {
					const res = JSON.parse(text)
					if (('log' in res) && ('success' in res))
						log = res satisfies SimpleOperationResponse;
				} catch {
					if (text.length > 0)
						message = text;
					else
						message += ` - Status: ${reason.response.status}`;
				}
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
