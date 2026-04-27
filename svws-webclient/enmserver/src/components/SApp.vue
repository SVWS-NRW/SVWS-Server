<template>
	<svws-ui-app-layout no-secondary-menu :tertiary-menu="showAuswahlliste()" secondary-menu-small tertiary-menu-small>
		<template #sidebar>
			<svws-ui-menu :focus-switching-enabled :focus-help-visible>
				<template #header>
					<svws-ui-menu-header :user="auth.username" class="cursor-pointer" />
				</template>
				<template #default>
					<template v-for="item in apps" :key="item.name">
						<template v-if="item.name !== 'einstellungen'">
							<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)" @keydown.enter="startSetApp(item)">
								<template #icon>
									<span class="inline-block icon-lg i-ri-book-2-line" v-if="item.name === 'leistungen'" />
									<span class="inline-block icon-lg i-ri-book-2-line" v-if="item.name === 'teilleistungen'" />
									<span class="inline-block icon-lg i-ri-book-2-line" v-if="item.name === 'ankreuzkompetenzen'" />
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
							<svws-ui-menu-item :active="is_active(item)" @click="startSetApp(item)" @keydown.enter="startSetApp(item)">
								<template #icon><span class="inline-block icon-lg i-ri-settings-3-line" /></template>
								<template #label><span class="text-xs"> {{ item.text }}</span> </template>
							</svws-ui-menu-item>
						</template>
					</template>
					<ui-color-mode />
					<svws-ui-menu-item subline="" @click="doLogout">
						<template #label>Abmelden</template>
						<template #icon> <span class="icon-lg i-ri-logout-circle-line" /> </template>
					</svws-ui-menu-item>
				</template>
				<template #version>
					<div class="flex gap-1">
						<div class="mt-1">{{ version }} <a :href="`https://github.com/SVWS-NRW/SVWS-Server/commit/${githash}`" v-if="version.includes('SNAPSHOT')">{{ githash.substring(0, 8) }}</a></div>
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
		<template #tertiaryMenu v-if="app.hide !== true">
			<template v-if="pendingSetApp">
				<div class="h-full flex flex-col">
					<div class="secondary-menu--headline">
						<h1>{{ pendingSetApp }}</h1>
					</div>
					<div class="secondary-menu--header" />
					<div class="secondary-menu--content" />
				</div>
			</template>
			<template v-else>
				<router-view :key="app.name" name="liste" />
			</template>
		</template>
		<template #main>
			<main class="app--page h-full" :class="app.name" role="main">
				<div v-show="pendingSetApp" class="flex flex-col w-full h-full grow">
					<svws-ui-header>
						<div class="flex items-center">
							<div class="w-20 mr-6" v-if="(app.name === 'schueler') || (app.name === 'lehrer')">
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
				<div v-show="!pendingSetApp" class="flex flex-col w-full h-full grow overflow-hidden" :class="{'focus-region': focusSwitchingEnabled, 'highlighted': focusHelpVisible}">
					<router-view :key="app.name" />
				</div>
			</main>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import { onMounted, onUnmounted, ref } from "vue";
	import type { AppProps } from './SAppProps';
	import { githash } from '../../githash';
	import { version } from '../../version';
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { TabData } from "@ui/ui/nav/TabData";
	import { useAuthState } from "~/states/AuthState";

	const auth = useAuthState();

	const props = defineProps<AppProps>();

	const { focusHelpVisible, focusSwitchingEnabled, enable, disable } = useRegionSwitch();

	const pendingSetApp = ref('');
	const copied = ref<boolean | null>(null);

	async function copyToClipboard() {
		try {
			await navigator.clipboard.writeText(`${version} ${githash}`);
		} catch {
			copied.value = false;
		}
		copied.value = true;
	}

	function is_active(current: TabData): boolean {
		const routename = props.app.name.split('.')[0];
		const title = current.text;
		if (routename !== current.name) {
			return false;
		}
		if (document.title !== title) {
			document.title = title;
			document.querySelector("link[rel~='icon']")?.setAttribute('href', 'assets/favicon.svg');
		}
		return true;
	}

	const hideAuswahlliste = new Set<string>(["statistik"]);

	function showAuswahlliste(): boolean {
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

	onMounted(() => {
		enable();
	});

	onUnmounted(() => {
		disable();
	});


</script>
