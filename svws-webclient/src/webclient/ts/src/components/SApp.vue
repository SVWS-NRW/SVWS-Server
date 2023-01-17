<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-sidebar-menu>
				<template #header>
					<svws-ui-sidebar-menu-header> Admin </svws-ui-sidebar-menu-header>
				</template>
				<template #default>
					<svws-ui-sidebar-menu-item v-for="item in routeApp.menu" :key="item.name" :active="is_active(item)" @click="select(item)">
						<template #label> {{ getText(item) }} </template>
						<template #icon> <s-app-icon :routename="item.name" /> </template>
					</svws-ui-sidebar-menu-item>
				</template>
				<template #footer>
					<svws-ui-sidebar-menu-item class="print:hidden" subline="" @click="logout">
						<template #label>Abmelden</template>
						<template #icon> <i-ri-logout-circle-line /> </template>
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
			<router-view :key="$route.hash" name="liste" />
		</template>
		<template #main>
			<!-- <svws-ui-overlay v-if="showOverlay || initializing" />-->
			<div class="page-wrapper" :class="route.name">
				<!-- <svws-ui-overlay v-if="showOverlay || initializing" />-->
				<main class="relative h-full">
					<router-view :key="$route.hash" />
				</main>
				<s-app-status :error="error_message" />
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import type { Schule } from "~/apps/schule/Schule";
	import { version } from '../../version';

	import { computed, ComputedRef, onErrorCaptured, ref, Ref, watch } from "vue";
	import { useRoute } from "vue-router";
	import { injectMainApp } from "~/apps/Main";
	import { router } from "~/router";
	import { routeApp } from "~/router/RouteApp";
	import { RouteNode } from "~/router/RouteNode";

	onErrorCaptured((e)=>{
		error_message.value = e.message.toString();
	});

	const error_message: Ref<undefined|string> = ref(undefined);
	watch(error_message, (new_val)=> {
		setTimeout(()=> {
			if (new_val !== undefined) error_message.value = undefined;
		}, 10_000);
	})

	const route = useRoute();

	const main = injectMainApp();

	const appSchule: ComputedRef<Schule> = computed(() => main.apps.schule);

	const schulname: ComputedRef<string> = computed(() => {
		const name = appSchule.value.schuleStammdaten.daten?.bezeichnung1;
		return name ? name.toString() : "fehlende Bezeichnung";
	});

	const minDelayReached = ref(false);
	const minDurationReached = ref(false);
	const showOverlay = ref(false);

	const initializing: ComputedRef<boolean> = computed( () => main.config.apiLoadingStatus.initializing);

	setTimeout(() => (minDelayReached.value = true), 100); // Minimum Delay bevor das Overlay eingeblendet wird. Soll flackern des Bildschirms vermeiden.
	setTimeout(() => (minDurationReached.value = true), 400); // Minimum Dauer bevor das Overlay ausgeblendet wird. Soll flackern des Bildschirms vermeiden.
	watch(minDelayReached, () => { showOverlay.value = true; });
	watch(minDurationReached, () => { showOverlay.value = false; });

	function select(current : RouteNode<unknown, any>) : void {
		router.push({ name: current.name });
		document.title = schulname.value + " - " + current.text;
	}

	function is_active(current : RouteNode<unknown, any>): boolean {
		const routename = route.name?.toString().split('_')[0];
		if (routename === undefined)
			return false;
		if (routename === current.name)
			return true;
		return current.menu.map(r => r.name).includes(routename);
	}

	function getText(item: RouteNode<unknown, any>): string {
		return item.text;
	}

	function logout() {
		main.logout();
		document.title = "SVWS-Client";
		router.push("/login");
	}
</script>
