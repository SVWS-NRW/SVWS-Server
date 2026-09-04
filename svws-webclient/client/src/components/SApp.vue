<template>
	<svws-ui-app-layout ref="appLayout" :no-secondary-menu="!menu.hasSubmenu" :tertiary-menu="menu.hasAuswahlliste" secondary-menu-small>
		<template #sidebar>
			<svws-ui-menu :focus-switching-enabled :focus-help-visible>
				<template #header>
					<svws-ui-menu-header v-if="menu.benutzerprofil !== null"
						class="cursor-pointer"
						:user="benutzerState.benutzerdaten.anzeigename"
						:schule="schulname"
						:schema="schemaname"
						:hint="userWiedervorlageHint"
						@click="startSetApp(menu.benutzerprofil)" />
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
					<div class="flex gap-1 items-center">
						<div>
							{{ version }}
							<span v-if="version.includes('SNAPSHOT')">&nbsp;{{ serverState.mode.name() }}-Mode&nbsp;
								<a :href="`https://github.com/SVWS-NRW/SVWS-Server/commit/${githash}`">{{ githash.substring(0, 8) }}</a>
							</span>
						</div>
						<div type="transparent" class="cursor-pointer icon" @click="copyToClipboard" :class="{
							'i-ri-file-copy-line': copied === null,
							'i-ri-error-warning-fill': copied === false,
							'i-ri-check-line icon-ui-brand': copied === true,
						}" />
					</div>
				</template>
				<template #metaNavigation>
					<impressum-modal v-slot="{ openModal }">
						<svws-ui-button type="transparent" @click="openModal()">
							Impressum
						</svws-ui-button>
					</impressum-modal>
					<datenschutz-modal v-slot="{ openModal }">
						<svws-ui-button type="transparent" @click="openModal()">
							Datenschutz
						</svws-ui-button>
					</datenschutz-modal>
				</template>
			</svws-ui-menu>
		</template>
		<template #secondaryMenu v-if="menu.hasSubmenu && tabManager !== null">
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
						<div class="flex justify-between w-full items-center">
							<abschnitt-auswahl :disabled="menu.current.name.startsWith('notenmodul')" />
							<div v-if="apiStatus.pending && (notenmodulState.istAdminLehrer !== null)" class="flex gap-2">
								<svws-ui-spinner spinning />
								<span class="text-base font-normal">lade Daten …</span>
							</div>
							<svws-ui-checkbox v-else-if="notenmodulState.istAdminLehrer !== null"
								type="toggle"
								:model-value="notenmodulState.istAdminLehrer"
								@update:model-value="notenmodulState.toggleAdmin()">
								Admin
							</svws-ui-checkbox>
							<div v-else />
						</div>
					</div>
					<div class="secondary-menu--header" />
					<div class="secondary-menu--content">
						<p v-if="focusSwitchingEnabled" v-show="focusHelpVisible" class="region-enumeration">2</p>
						<svws-ui-secondary-menu-navigation class="focus-region" :class="{'highlighted': focusHelpVisible}" :tab-manager />
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
				<div v-show="!pendingSetApp"
					class="flex flex-col w-full h-full grow overflow-hidden"
					:class="{'svws-api--pending': apiStatus.pending, 'focus-region': focusSwitchingEnabled, 'highlighted': focusHelpVisible}">
					<router-view :key="menu.current.name" />
				</div>
			</main>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import { computed, onMounted, onUnmounted, ref, watch } from "vue";
	import type { TabManager, TabData } from "@ui";
	import { useWiedervorlageState } from "@ui";
	import { useRegionSwitch, useServerState, useSchuleState, useNotenmodulState, useBenutzerState } from "@ui";
	import type { AppProps } from './SAppProps';
	import { githash } from '../../githash';
	import { version } from '../../version';

	const props = defineProps<AppProps>();

	const benutzerState = useBenutzerState();
	const notenmodulState = useNotenmodulState();
	const serverState = useServerState();
	const wiedervorlageState = useWiedervorlageState();
	const schuleState = useSchuleState();

	const { focusHelpVisible, focusSwitchingEnabled, enable, disable } = useRegionSwitch();

	const appLayout = ref();

	onMounted(() => {
		if (props.menu.current.name === 'statistik') {
			appLayout.value?.setSecondSidebarExpanded(false);
		} else {
			appLayout.value?.setSecondSidebarExpanded(true);
		}
		enable();
	});

	onUnmounted(() => disable());

	watch(() => props.menu.current.name, (m) => {
		const mainText = props.menu.mainEntry.text;
		const subText = props.menu.current.text;
		const title = mainText + " - " + ((mainText === subText) ? "" : subText + " - ") + schulname.value;
		if (document.title !== title) {
			document.title = title;
		}
		// Collapse sidebar for statistik
		if (m === 'statistik') {
			appLayout.value?.setSecondSidebarExpanded(false);
		} else {
			appLayout.value?.setSecondSidebarExpanded(true);
		}
	});

	const schulname = computed<string>(() => {
		const name = schuleState.stammdaten.bezeichnung1;
		return (name.length > 0) ? name : "Fehlende Bezeichnung für die Schule";
	});

	const tabManager = computed<null | (() => TabManager)>(() => {
		const name = props.menu.current.name;

		if (name.startsWith('schule')) {
			return props.tabManagerSchule;
		} else if (name.startsWith('notenmodul')) {
			return props.tabManagerNotenmodul;
		} else if (name.startsWith('einstellungen')) {
			return props.tabManagerEinstellungen;
		} else if (name.startsWith('benutzerprofil')) {
			return props.tabManagerBenutzerprofil;
		}

		return null;
	});

	const userWiedervorlageHint = computed<{ number: number, type: "highlight", text: string } | undefined>(() => {
		const anzahl = wiedervorlageState.anzahlOffeneWiedervorlagen;
		const text = anzahl === 1 ?
			"Es liegt eine offene Wiedervorlage vor." : `Es liegen ${anzahl} offene Wiedervorlagen vor.`;
		return anzahl > 0 ?
			{ number: anzahl, type: "highlight", text: text } : undefined;
	});

	const pendingSetApp = ref('');
	const copied = ref<boolean | null>(null);

	function getIcon(menu: TabData): string {
		switch (menu.image) {
			case "i-ri-school-line":
			case "i-ri-group-line":
			case "i-ri-briefcase-line":
			case "i-ri-team-line":
			case "i-ri-book-2-line":
			case "i-ri-music-2-fill":
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
		} catch {
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

</script>
