<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-sidebar-menu>
				<template #header>
					<svws-ui-sidebar-menu-header> Admin </svws-ui-sidebar-menu-header>
				</template>
				<template #default>
					<svws-ui-sidebar-menu-item
						v-for="menuItem in menuItems"
						:key="menuItem.caption"
						:active="menuItem.active.includes(route.name as string)"
						@click="router.push({ name: menuItem.value })"
					>
						<template #label>{{ menuItem.caption }}</template>
						<template #icon>
							<i-ri-team-line v-if="menuItem.icon === 'team'" />
							<i-ri-user-2-line v-else-if="menuItem.icon === 'user-2'" />
							<i-ri-artboard-line v-else-if="menuItem.icon === 'artboard'" />
							<i-ri-numbers-line v-else-if="menuItem.icon === 'numbers'" />
							<i-ri-group-line v-else-if="menuItem.icon === 'group'" />
							<i-ri-bar-chart-2-line v-else-if="menuItem.icon === 'line-chart'" />
							<i-ri-book-read-line v-else-if="menuItem.icon === 'book-read'" />
							<i-ri-parent-line v-else-if="menuItem.icon === 'parent'" />
							<i-ri-community-line v-else-if="menuItem.icon === 'community'" />
							<i-ri-archive-line v-else-if="menuItem.icon === 'archive'" />
							<i-ri-briefcase-line v-else-if="menuItem.icon === 'briefcase'" />
							<i-ri-slideshow-line v-else-if="menuItem.icon === 'slideshow'" />
							<svg v-else-if="menuItem.icon === 'graduation-cap'" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1.2em" height="1.2em">
								<g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
									<path d="m2.573 8.463l8.659-4.329a.6.6 0 0 1 .536 0l8.659 4.33a.6.6 0 0 1 0 1.073l-8.659 4.329a.6.6 0 0 1-.536 0l-8.659-4.33a.6.6 0 0 1 0-1.073Z"/>
									<path d="M22.5 13V9.5l-2-1m-16 2v5.412a2 2 0 0 0 1.142 1.807l5 2.374a2 2 0 0 0 1.716 0l5-2.374a2 2 0 0 0 1.142-1.807V10.5"/>
								</g>
							</svg>
						</template>
					</svws-ui-sidebar-menu-item>
				</template>
				<template #footer>
					<svws-ui-sidebar-menu-item class="print:hidden" subline="" @click="logout" >
						<template #label>Abmelden</template >
						<template #icon> <i-ri-logout-circle-line /> </template >
					</svws-ui-sidebar-menu-item>
				</template>
				<template #version>v{{ version }}</template>
				<template #metaNavigation>
					<a href="https://www.svws.nrw.de/faq/impressum">Impressum</a>
					<a href="#">Datenschutz</a>
				</template>
			</svws-ui-sidebar-menu>
		</template>
		<template #secondaryMenu>
			<router-view :key="$route.hash" name="liste"/>
		</template>
		<template #main>
			<svws-ui-overlay v-if="showOverlay || initializing" />
			<div class="page-wrapper">
				<svws-ui-overlay v-if="showOverlay || initializing" />
				<main class="relative h-full">
					<router-view :key="$route.hash" />
				</main>
				<s-app-status />
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">
	import type { Schule } from "~/apps/schule/Schule";
	import { version } from '../../version';

	import { computed, ComputedRef, ref, watch, WritableComputedRef } from "vue";
	import { useRoute } from "vue-router";
	import { injectMainApp } from "~/apps/Main";
	import { router } from "~/router";
	import { App } from "~/apps/BaseApp";

	const route = useRoute();

	interface MenuItem { caption: string; value: string; icon: string; active: string[]; }

	const main = injectMainApp();
	const appSchule: ComputedRef<Schule> =
		computed(() => main.apps.schule);

	const menubar_selected: WritableComputedRef<string> =
		computed({
			get(): string {
				return main.config.selected_app;
			},
			set(val: string) {
				if (val === "abmelden") {
					main.logout();
				} else {
					if (val && val !== menubar_selected.value) {
						main.config.selected_app = val;
						selectedItems.value = [];
					}
				}
			}
		});

	const selectedItems = ref([]);

	const schulname: ComputedRef<string> =
		computed(() => {
			const name = appSchule.value.schuleStammdaten.daten?.bezeichnung1;
			return name ? name.toString() : "fehlende Bezeichnung"; });

	const fullMenuItems: MenuItem[] = [
		{ caption: schulname.value, value: "schule", active: ["schule"], icon: "community" },
		{ caption: "Kataloge", value: "kataloge", active: [ "kataloge", "faecher", "jahrgaenge", "foerderschwerpunkte", "religionen" ], icon: "archive" },
		{ caption: "Schüler", value: "schueler", active: ["schueler"], icon: "team" },
		{ caption: "Lehrkräfte", value: "lehrer", active: ["lehrer"], icon: "briefcase" },
		{ caption: "Klassen", value: "klassen", active: ["klassen"], icon: "group" },
		{ caption: "Kurse", value: "kurse", active: ["kurse"], icon: "slideshow" },
		{ caption: "Oberstufe", value: "gost", active: ["gost"], icon: "graduation-cap" },
		{ caption: "Statistik", value: "statistik", active: ["statistik"], icon: "line-chart" }
	];
	const minDelayReached = ref(false);
	const minDurationReached = ref(false);
	const showOverlay = ref(false);

	const menuItems: ComputedRef<MenuItem[]> =
		computed(() => {
			if (!main.config.hasGost) {
				return fullMenuItems.filter(item => item.value !== "gost");
			}
			return fullMenuItems; });

	const initializing: ComputedRef<boolean> =
		computed( () => main.config.apiLoadingStatus.initializing);

	setTimeout(() => (minDelayReached.value = true), 100); // Minimum Delay bevor das Overlay eingeblendet wird. Soll flackern des Bildschirms vermeiden.
	setTimeout(() => (minDurationReached.value = true), 400); // Minimum Dauer bevor das Overlay ausgeblendet wird. Soll flackern des Bildschirms vermeiden.
	watch(minDelayReached, () => { showOverlay.value = true; });
	watch(minDurationReached, () => { showOverlay.value = false; });
	watch( () => route.name, () => {
		//Die Liste werden biem Navigieren aktualisiert.
		if (route.name?.toString() === "benutzer")
			App.apps.benutzer.auswahl.update_list();
		if (route.name?.toString() === "benutzergruppe")
			App.apps.benutzergruppe.auswahl.update_list();
		selectedItems.value = [];
	});

	function logout() {
		main.logout();
		router.push("/login");
	}
</script>

<style lang="postcss">
	@page {
		size: A4 portrait;
		margin: 0mm;
	}

	@media print {
		body {
			width: 210mm;
			height: 296.8mm;
			padding: 10mm 25mm 10mm 25mm !important;
			/* overflow: hidden; */
		}
	}

	.input--schule-abschnitte input.text-input--control {
		@apply border-0 p-0 h-auto;
	}
</style>
