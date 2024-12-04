<template>
	<svws-ui-app-layout :no-secondary-menu="!showSubmenu()" :tertiary-menu="showAuswahlliste()" secondary-menu-small>
		<template #sidebar>
			<svws-ui-menu :focus-switching-enabled :focus-help-visible>
				<template #header>
					<!--<span v-if="apiStatus?.pending">...</span>-->
					<!--TODO: Statt Name den vollen Anzeigenamen anzeigen (erstellt dann automatisch eine Ausgabe der Initialien-->
					<svws-ui-menu-header :user="username" :schule="schulname" :schema="schemaname" @click="setApp(benutzerprofilApp)" class="cursor-pointer" />
				</template>
				<template #default>
					<div :ref="el => (el !== null) && sectionRefs.set(0, <HTMLElement>el)" tabindex="-1" />
					<template v-for="item in apps" :key="item.name">
						<template v-if="item.name !== 'einstellungen'">
							<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)">
								<template #icon>
									<span class="inline-block icon-lg i-ri-team-line" v-if="item.name === 'klassen'" />
									<span class="inline-block icon-lg i-ri-group-line" v-else-if="item.name === 'schueler'" />
									<span class="inline-block icon-lg i-ri-bar-chart-2-line" v-else-if="item.name === 'statistik'" />
									<span class="inline-block icon-lg i-ri-calendar-event-line" v-else-if="item.name === 'stundenplan'" />
									<span class="inline-block icon-lg i-ri-school-line" v-else-if="item.name === 'schule'" />
									<span class="inline-block icon-lg i-ri-archive-line" v-else-if="item.name === 'kataloge'" />
									<span class="inline-block icon-lg i-ri-briefcase-line" v-else-if="item.name === 'lehrer'" />
									<span class="inline-block icon-lg i-ri-book-2-line" v-else-if="item.name === 'kurse'" />
									<span class="inline-block icon-lg i-ri-graduation-cap-line" v-else-if="item.name === 'gost'" />
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
		<template #secondaryMenu v-if="showSubmenu()">
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
				<svws-ui-secondary-menu>
					<template #headline> {{ app.name.startsWith('schule') ? "Schule" : "Einstellungen" }} </template>
					<template #abschnitt v-if="app.name.startsWith('schule')">
						<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
					</template>
					<template #header />
					<template #content>
						<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" :id="focusSwitchingEnabled && showSubmenu() ? 'menuFocusNumber' : ''" class="region-enumeration">2</p>
						<svws-ui-secondary-menu-navigation :id="focusSwitchingEnabled && showSubmenu() ? 'menuFocusBorder' : ''" class="focus-region" :tab-manager="(app.name.startsWith('schule') ? tabManagerSchule : tabManagerEinstellungen)" />
					</template>
				</svws-ui-secondary-menu>
			</template>
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
				<div :ref="el => (el !== null) && sectionRefs.set(1, <HTMLElement>el)" tabindex="-1" />
				<router-view :key="app.name" name="liste" />
			</template>
		</template>
		<template #main>
			<main class="app--page" :class="app.name" role="main">
				<div :ref="el => (el !== null) && sectionRefs.set(2, <HTMLElement>el)" tabindex="-1" />
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
				<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" id="contentFocusNumber" class="region-enumeration">8</p>
				<div :id="focusSwitchingEnabled ? 'contentFocusBorder' : ''" v-show="!pendingSetApp" class="page--wrapper" :class="{'svws-api--pending': apiStatus.pending, 'focus-region': focusSwitchingEnabled}">
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

	import { computed, onMounted, onUnmounted, ref, onErrorCaptured } from "vue";
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

	// const nextFocusSection = ref(0);
	const sectionRefs = ref<Map<number, HTMLElement>>(new Map());

	const schulname = computed<string>(() => {
		const name = props.schuleStammdaten.bezeichnung1;
		return (name.length > 0) ? name : "Fehlende Bezeichnung für die Schule";
	});

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

	const showSubmenus = new Set<string>([
		"schule", "schule.stammdaten", "schule.betriebe", "schule.einwilligungsarten", "schule.faecher", "schule.foerderschwerpunkte", "schule.jahrgaenge",
		"schule.vermerkarten", "schule.religionen", "schule.schulen", "schule.datenaustausch.kurs42", "schule.datenaustausch.untis", "schule.datenaustausch.enm",
		"schule.datenaustausch.laufbahnplanung", "schule.datenaustausch.schulbewerbung", "schule.datenaustausch.wenom", "einstellungen", "einstellungen.benutzer",
		"einstellungen.benutzergruppen",
	]);

	function showSubmenu() : boolean {
		return showSubmenus.has(props.selectedChild.name);
	}

	const hideAuswahlliste = new Set<string>([ "statistik", "einstellungen",
		"schule", "schule.stammdaten", "schule.datenaustausch.kurs42", "schule.datenaustausch.untis", "schule.datenaustausch.enm",
		"schule.datenaustausch.laufbahnplanung", "schule.datenaustausch.schulbewerbung", "schule.datenaustausch.wenom",
	]);

	function showAuswahlliste() : boolean {
		return !hideAuswahlliste.has(props.selectedChild.name);
	}

	function is_active(current: TabData): boolean {
		const routename = props.app.name.split('.')[0];
		const title = current.text + " - " + schulname.value;

		if (((props.app.name === 'benutzer') || (props.app.name === 'benutzergruppen')) && (current.name === 'schule'))
			return true;
		if (routename !== current.name)
			return false;
		if (document.title !== title) {
			document.title = title;
			document.querySelector("link[rel~='icon']")?.setAttribute('href', 'favicon' + (props.app.name === 'statistik' ? '-statistik' : '') + '.svg')
		}
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

	// window.addEventListener("keydown", switchFocus);
	//
	// function switchFocus(event: KeyboardEvent) {
	// 	if (!event.repeat && event.ctrlKey && event.altKey && ((event.key === "PageUp") || (event.key === "PageDown"))) {
	// 		nextFocusSection.value = event.key === "PageUp"
	// 			? ((nextFocusSection.value + 1) % sectionRefs.value.size)
	// 			: (nextFocusSection.value === 0 ) ? (sectionRefs.value.size - 1) : (nextFocusSection.value - 1);
	// 		sectionRefs.value.get(nextFocusSection.value)?.focus();
	// 	}
	// }

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
