<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-sidebar-menu>
				<template #header>
					<svws-ui-sidebar-menu-header> Admin </svws-ui-sidebar-menu-header>
				</template>
				<template #default>
					<svws-ui-sidebar-menu-item v-for="menuItem in menuItems" :key="menuItem.caption"
						:active="menuItem.active.includes(route.name?.toString().split('_')[0] as string)"
						@click="router.push({ name: menuItem.value })">
						<template #label> {{ menuItem.caption }} </template>
						<template #icon> <s-app-icon :routename="menuItem.value" /> </template>
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

	const route = useRoute();

	interface MenuItem { caption: string; value: string; icon: string; active: string[]; }

	const main = injectMainApp();
	const appSchule: ComputedRef<Schule> = computed(() => main.apps.schule);

	const menubar_selected: WritableComputedRef<string> = computed({
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

	const schulname: ComputedRef<string> = computed(() => {
		const name = appSchule.value.schuleStammdaten.daten?.bezeichnung1;
		return name ? name.toString() : "fehlende Bezeichnung";
	});

	const fullMenuItems: MenuItem[] = [
		{ caption: schulname.value, value: "schule", active: ["schule"], icon: "community" },
		{ caption: "Kataloge", value: "kataloge", active: [ "kataloge", "faecher", "jahrgaenge", "foerderschwerpunkte", "religionen" ], icon: "archive" },
		{ caption: "Schüler", value: "schueler", active: ["schueler"], icon: "group" },
		{ caption: "Lehrkräfte", value: "lehrer", active: ["lehrer"], icon: "briefcase" },
		{ caption: "Klassen", value: "klassen", active: ["klassen"], icon: "team" },
		{ caption: "Kurse", value: "kurse", active: ["kurse"], icon: "slideshow" },
		{ caption: "Oberstufe", value: "gost", active: ["gost"], icon: "graduation-cap" },
		{ caption: "Statistik", value: "statistik", active: ["statistik"], icon: "line-chart" }
	];
	const minDelayReached = ref(false);
	const minDurationReached = ref(false);
	const showOverlay = ref(false);

	const menuItems: ComputedRef<MenuItem[]> = computed(() => {
		if (!main.config.hasGost) {
			return fullMenuItems.filter(item => item.value !== "gost");
		}
		return fullMenuItems; 
	});

	const initializing: ComputedRef<boolean> = computed( () => main.config.apiLoadingStatus.initializing);

	setTimeout(() => (minDelayReached.value = true), 100); // Minimum Delay bevor das Overlay eingeblendet wird. Soll flackern des Bildschirms vermeiden.
	setTimeout(() => (minDurationReached.value = true), 400); // Minimum Dauer bevor das Overlay ausgeblendet wird. Soll flackern des Bildschirms vermeiden.
	watch(minDelayReached, () => { showOverlay.value = true; });
	watch(minDurationReached, () => { showOverlay.value = false; });

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
</style>
