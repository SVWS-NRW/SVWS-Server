<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<template #header>
					<svws-ui-menu-header :user="username" :schule="schulname" />
				</template>
				<template #default>
					<template v-for="item in apps" :key="item.name">
						<svws-ui-menu-item :active="is_active(item)" @click="setApp(item)">
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
				<template #version>{{ version }}</template>
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
		<template #secondaryMenu>
			<router-view :key="app.name" name="liste" />
		</template>
		<template #main>
			<div class="app--page" :class="app.name">
				<div class="page--wrapper">
					<router-view :key="app.name" />
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import type { AppProps } from './SAppProps';
	import type { AuswahlChildData } from './AuswahlChildData';
	import { computed, ref, watch } from "vue";
	import { version } from '../../version';
	import {router} from "~/router/RouteManager";

	const props = defineProps<AppProps>();

	const schulname: ComputedRef<string> = computed(() => {
		const name = props.schuleStammdaten.bezeichnung1;
		return name ? name : "Fehlende Bezeichnung für die Schule";
	});

	const minDelayReached = ref(false);
	const minDurationReached = ref(false);
	const showOverlay = ref(false);

	setTimeout(() => (minDelayReached.value = true), 100); // Minimum Delay bevor das Overlay eingeblendet wird. Soll flackern des Bildschirms vermeiden.
	setTimeout(() => (minDurationReached.value = true), 400); // Minimum Dauer bevor das Overlay ausgeblendet wird. Soll flackern des Bildschirms vermeiden.
	watch(minDelayReached, () => { showOverlay.value = true; });
	watch(minDurationReached, () => { showOverlay.value = false; });

	function is_active(current: AuswahlChildData): boolean {
		const routename = props.app.name?.toString().split('.')[0];
		if ((props.app.name === 'benutzer' || props.app.name === 'benutzergruppen') && current.name === 'schule')
			return true;
		if (routename !== current.name)
			return false;
		document.title = current.text + " - " + schulname.value;
		return true;
	}

	async function doLogout() {
		document.title = "SVWS NRW";
		await props.logout();
	}

</script>

<style lang="postcss">
.app--page {
	@apply flex flex-grow flex-col justify-between;
	@apply h-screen;
	@apply overflow-hidden;
	@apply relative;
	@apply bg-white;
}

 .page--wrapper {
	 @apply relative h-full;
 }

 .page--flex {
	 @apply flex flex-col w-full h-full;
 }
</style>
