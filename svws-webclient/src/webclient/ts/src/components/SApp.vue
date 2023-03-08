<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<template #header>
					<svws-ui-menu-header> {{ username }} </svws-ui-menu-header>
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
					<a href="https://www.svws.nrw.de/faq/impressum">Impressum</a>
					<a href="#">Datenschutz</a>
				</template>
			</svws-ui-menu>
		</template>
		<template #secondaryMenu>
			<router-view :key="app.name" name="liste" />
		</template>
		<template #main>
			<div class="page-wrapper" :class="app.name">
				<main class="relative h-full">
					<router-view :key="app.name" />
				</main>
				<svws-ui-notifications v-if="errors.length">
					<template v-for="error of errors.reverse()" :key="error.message">
						<svws-ui-notification type="error">
							<template #header>
								{{ error.name }}
							</template>
							{{ error.message }}
							<template #stack v-if="error.stack">
								<pre v-html="error.stack" />
							</template>
						</svws-ui-notification>
					</template>
				</svws-ui-notifications>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import { version } from '../../version';
	import { computed, ComputedRef, onErrorCaptured, ref, Ref, watch } from "vue";
	import { AppProps } from './SAppProps';
	import { AuswahlChildData } from './AuswahlChildData';

	const errors: Ref<Error[]> = ref([]);

	onErrorCaptured((e) => { errors.value.push(e); });

	const props = defineProps<AppProps>();

	const schulname: ComputedRef<string> = computed(() => {
		const name = props.schuleStammdaten.bezeichnung1;
		return name ? name : "fehlende Bezeichnung";
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
