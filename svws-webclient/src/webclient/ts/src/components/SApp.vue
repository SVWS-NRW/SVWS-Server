<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-sidebar-menu :collapsed="isCollapsed" class="print:hidden" @toggle="onToggle">
				<template #header>
					<svws-ui-sidebar-menu-header :collapsed="isCollapsed">
						SVWS-NRW
					</svws-ui-sidebar-menu-header>
					<div v-if="!isCollapsed">
						<div v-if="schule_abschnitte" class="mt-4 px-4">
							<svws-ui-multi-select v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort"
								:item-text="item_text"></svws-ui-multi-select>
						</div>
					</div>
					<div v-else class="mt-9" />
				</template>
				<template #default>
					<div class="mt-6 mb-8">
						<svws-ui-sidebar-menu-item v-for="menuItem in menuItems" :key="menuItem.caption" :collapsed="isCollapsed"
							:active="menuItem.active.includes(route.name as string)" @click="router.push({ name: menuItem.value })">
							<template #label>{{
							menuItem.caption }}</template>
							<template #icon>
								<i-ri-team-line v-if="menuItem.icon === 'team'" />
								<i-ri-user--2-line v-else-if="menuItem.icon === 'user-2'" />
								<i-ri-artboard-line v-else-if="menuItem.icon === 'artboard'" />
								<i-ri-numbers-line v-else-if="menuItem.icon === 'numbers'" />
								<i-ri-group-line v-else-if="menuItem.icon === 'group'" />
								<i-ri-line-chart-line v-else-if="menuItem.icon === 'line-chart'" />
								<i-ri-book-read-line v-else-if="menuItem.icon === 'book-read'" />
								<i-ri-parent-line v-else-if="menuItem.icon === 'parent'" />
								<i-ri-community-line v-else-if="menuItem.icon === 'community'" />
							</template>
						</svws-ui-sidebar-menu-item>
					</div>
				</template>
				<template #footer>
					<svws-ui-sidebar-menu-item class="print:hidden" subline="" :collapsed="isCollapsed" @click="logout"><template
							#label>Abmelden</template><template #icon>
							<i-ri-logout-box-line />
						</template></svws-ui-sidebar-menu-item>
				</template>
			</svws-ui-sidebar-menu>
		</template>
		<template #secondaryMenu>
			<router-view name="liste" />
		</template>
		<template #main>
			<svws-ui-overlay v-if="showOverlay || initializing" />
			<div class="page-wrapper svws-ui-bg-white">
				<svws-ui-overlay v-if="showOverlay || initializing" />
				<main class="relative h-screen">
					<router-view />
				</main>
				<s-app-status />
			</div>
		</template>
		<template #contentSidebar>
			<div id="sidebar"
				class="svws-ui-bg-white svws-ui-text-black svws-ui-border-l-2 svws-ui-border-dark-20 print:hidden"></div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">
import type {
	Schuljahresabschnitt
} from "@svws-nrw/svws-core-ts";
import type { Schule } from "~/apps/schule/Schule";

import { computed, ComputedRef, defineAsyncComponent, ref, watch, WritableComputedRef } from "vue";
import { useRoute } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { router } from "~/router"

const route = useRoute();

interface MenuItem {
	caption: string;
	value: string;
	icon: string;
	active: string[];
};


const SAppStatus = defineAsyncComponent({
	loader: () => import("~/components/SAppStatus.vue")
});


const main = injectMainApp();
const appSchule: ComputedRef<Schule> = computed(() => { return main.apps.schule });

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
const isCollapsed = ref(false);

const schulname: ComputedRef<string> = computed(() => {
	const name = appSchule.value.schuleStammdaten.daten?.bezeichnung1
	return name ? name.toString() : "fehlende Bezeichnung"
});

const fullMenuItems: MenuItem[] = [
	{
		caption: schulname.value,
		value: "Schule",
		active: ["schule"],
		icon: "community"
	},
	{
		caption: "Kataloge",
		value: "kataloge",
		active: ["kataloge", "faecher", "jahrgaenge", "foerderschwerpunkte", "religionen"],
		icon: "book-read"
	},
	{
		caption: "Schüler",
		value: "schueler",
		active: ["schueler"],
		icon: "team"
	},
	{
		caption: "Lehrkräfte",
		value: "lehrer",
		active: ["lehrer"],
		icon: "user-2"
	},
	{
		caption: "Klassen",
		value: "klassen",
		active: ["klassen"],
		icon: "group"
	},
	{
		caption: "Kurse",
		value: "kurse",
		active: ["kurse"],
		icon: "parent"
	},
	{
		caption: "Oberstufe",
		value: "gost",
		active: ["gost"],
		icon: "numbers"
	},
	{
		caption: "Statistik",
		value: "statistik",
		active: ["statistik"],
		icon: "line-chart"
	}
] as MenuItem[];
const minDelayReached = ref(false);
const minDurationReached = ref(false);
const showOverlay = ref(false);

const menuItems: ComputedRef<{
	caption: string;
	value: string;
	icon: string;
	active: string[];
}[]> = computed(() => {
	if (!main.config.hasGost) {
		return fullMenuItems.filter(
			item => item.value !== "gost"
		);
	}
	return fullMenuItems;
});

const initializing: ComputedRef<boolean> = computed(() => main.config.apiLoadingStatus.initializing);

const schule_abschnitte: ComputedRef<Array<Schuljahresabschnitt> | undefined> = computed(() => {
	const liste = appSchule.value.schuleStammdaten.daten?.abschnitte;
	return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
});

const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
	get(): Schuljahresabschnitt {
		return main.config.akt_abschnitt;
	},
	set(abschnitt: Schuljahresabschnitt) {
		main.config.akt_abschnitt = abschnitt
	}
});


setTimeout(() => (minDelayReached.value = true), 100); // Minimum Delay bevor das Overlay eingeblendet wird. Soll flackern des Bildschirms vermeiden.
setTimeout(() => (minDurationReached.value = true), 400); // Minimum Dauer bevor das Overlay ausgeblendet wird. Soll flackern des Bildschirms vermeiden.
watch(minDelayReached, () => {
	showOverlay.value = true;
});
watch(minDurationReached, () => {
	showOverlay.value = false;
});
watch(() => route.name, () => { selectedItems.value = [] })

function logout() {
	main.logout()
	router.push('/login')
}

function onToggle(value: boolean) {
	isCollapsed.value = value;
};

function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
	return (
		b.schuljahr +
		b.abschnitt * 0.1 -
		(a.schuljahr + a.abschnitt * 0.1)
	);
};

function item_text(item: Schuljahresabschnitt) {
	return item.schuljahr
		? `${item.schuljahr}, ${item.abschnitt}. HJ`
		: "Abschnitt";
};
</script>

<style>
.svws-app {
	display: flex;
	position: relative;
	flex: 1 1 auto;
	flex-direction: column;
	height: 100%;
	width: 100%;
	-ms-user-select: None;
	-moz-user-select: None;
	-webkit-user-select: None;
	user-select: None;
	color: var(--font-color);
}

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

	.page {}
}
</style>
