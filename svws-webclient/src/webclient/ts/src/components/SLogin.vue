<template>
	<svws-ui-app-layout>
		<template #sidebar/>
		<template #main>
			<div class="login-wrapper">
				<div class="login-container">
					<div class="login-form">
						<h1 class="login-form-header">
							<span class="login-form-logo">
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1000">
									<path fill="#fdfdfc" d="M0 500V0h1000v1000H0V500z" />
									<path fill="#009136"
										  d="M0 500V0h1000v1000H0V500zm983.5 0V16.5h-967L16.2 499c-.1 265.4 0 483 .3 483.8.3 1 97.6 1.2 483.7 1l483.3-.3V500zm-564 461c-1.2-.4-3.5-3.7-5.2-7.1-7.7-15.7-5.8-29.1 5.3-37 3.8-2.8 4.4-3.7 4.4-7a11 11 0 00-2-6.3c-1.9-2.3-2.9-2.6-7.8-2.6a22 22 0 01-18-8.5c-11-12.6-11.1-38.4-.1-59.5 11.6-22.4 32.2-31.6 46.8-21 4.6 3.4 7.6 3.7 10.9 1.4 4-2.8 4.5-5 2.7-12-2.6-10.3-.3-17.7 7.8-25.1 22.7-21 73-16 84.7 8.3 2.2 4.5 2.4 10.4.4 21.5-.5 3.1-.2 3.9 2.6 6.2 3.5 3 8 3.5 10 1.1 4-4.6 13.7-6.9 21.4-4.9 7.4 2 12 5 18.8 12.4 14.8 15.9 20.9 43.7 13.8 62.6a25.4 25.4 0 01-24.2 17.5c-5.2 0-8.2 2.5-9.3 7.8-.6 3-.3 3.6 5.4 8.8 7.7 7.1 9.6 11.7 8.9 21.4-.7 8.7-6.3 21.4-10 22.4-3.5.9-165 .6-167.4-.4zM522 940.1a67.9 67.9 0 0034.5-27 63 63 0 006.1-50.7 62.9 62.9 0 00-38.6-40.1 71 71 0 00-35-1.9A63 63 0 00448 853c-9.3 19-9.3 37 0 56a62.6 62.6 0 0040.1 32 69.2 69.2 0 0033.8-.9zM496.2 919a41 41 0 01-28-21.7c-2.4-5-2.7-6.9-2.7-16.2 0-9.3.3-11.2 2.6-16.1a38.7 38.7 0 0172.4 5.5 39 39 0 01-44.3 48.5zM38 958.8c0-.8 2.3-7.5 5.1-14.9S55 911.9 63 889a1354 1354 0 0133.6-88.7A439 439 0 01156.3 697a370 370 0 0149.2-47.8c23.1-18 44-31.2 100-63.5 66.5-38.3 91.3-55.7 118.7-83.4a237.6 237.6 0 0030.5-36.4c8.5-12 24-37.2 29.1-47.6 3.7-7.3 5-8.3 5.8-4.7 1 3.8-.3 86-1.5 98.9A491.7 491.7 0 01154 937a474.6 474.6 0 01-106.8 22.7c-7.9.5-9.3.4-9.3-.9zm857-8.8a492.5 492.5 0 01-282-179.4c-12.6-16.5-29.3-41.7-28.2-42.8a81 81 0 0117.3.2l54.9 3 55 3c47 2.7 48.5 2.6 61-3.2a71.9 71.9 0 0025.5-25.2c3.6-7 5-8.1 9.8-7.2 11.2 2 16 16.3 9.6 28.8-3 6-20 22.5-27.9 27-3.6 2.2-10.1 5-14.5 6.5-7.4 2.5-9.3 2.7-25 2.8-9.3 0-28.2-.6-41.8-1.4-18.8-1-25-1.1-26-.2-2.2 2.2 10.2 24.3 22.5 40.3 19.9 25.6 47.3 47 93.3 72.8 56.4 31.6 67.9 38.6 86 52.7a197 197 0 0126.5 24.4c0 1.3-2.2 1-16-2zm-510.6-11a55.3 55.3 0 01-15.1-7.7c-6.2-4-6.6-5.9-3.3-14.7 2.6-6.9 9.5-17.6 11.4-17.6.8 0 3 2.2 5 5 2 2.7 6 6.4 8.7 8.1a19 19 0 014.9 4 48.8 48.8 0 00-3.9 16.4c-.3 4.3-1 8-1.6 8-.6.2-3.3-.5-6.1-1.5zm231.3 1.3c-.4-.3-.7-3-.7-6 0-2.9-1-8.3-2.2-12l-2.2-6.7 2.9-2a51.9 51.9 0 0012.2-11.7c1-1.6 2.5-2.9 3.4-2.9 3.3 0 13.1 18.4 13.3 24.8 0 3.1-.5 4-4.9 7.4a62.4 62.4 0 01-12.7 6.8c-8.6 3.4-8.2 3.2-9.1 2.3zM411.3 791.4c-.9-2.3.5-14.7 2.4-21.6 2.3-8.1 4.2-9.3 13.6-8.4 8.3.7 18.2 3.3 19.6 5 .7.8 0 2.7-2.5 6.1-1.9 2.7-4.3 7.7-5.3 11l-1.9 6-8.8.6c-5 .4-10.2 1.2-11.7 1.8-3.8 1.5-4.6 1.4-5.4-.5zm177.2 0c-2.2-.9-7.4-1.7-11.5-1.7l-7.4-.2-2.2-6.4a52 52 0 00-5.3-10.8c-1.7-2.4-3.1-4.7-3.1-5.2 0-3.2 25.3-7.6 29.7-5.2 5 2.7 9.6 31.3 5 31-.7 0-3-.7-5.2-1.6zm-41.3-135.1a490.1 490.1 0 01-36.2-176c0-6.3 0-6.3 5.5-11.6a63.2 63.2 0 0138.8-15.7h3.5l35.1 65.4c19.3 36 35.8 66 36.6 66.6 1.8 1.5 2.6 0 5.5-10.4 7-25.1 21.3-52 40.9-76.4 4.5-5.7 8-11 8-12-.3-.8-30.7-25.3-67.7-54.3L549.8 379l-19.1-.2-19.2-.3-.3-169.4c-.2-135 0-169.6 1-170.3 1.9-1.1 311-1 312.8.2 1.3.8 1 3.5-1.4 21.2A471.3 471.3 0 00820 92l-.6 11.4-7 3.9a635.7 635.7 0 00-44.5 26.2 3505.6 3505.6 0 00-135.5 107.7 34.9 34.9 0 00-9.8 19c-.2 3.8 0 4.3 3.7 6.2 5.7 3 8.1 6.8 8.1 12.6 0 6.1-2.5 9.7-9 12.8-4.6 2.3-4.7 2.4-4 6a22.9 22.9 0 0016 18.8c3.1.9 6.7 1.3 8 1 1.2-.3 5.4-3 9.1-5.9 5.2-4 7.2-6.2 8.2-9.2 1.7-5.1 3.4-6.9 9-9.6 6.8-3.1 18.3-3 24.9.2 16 8 9.3 22.7-11.4 24.6-6.2.6-7.4 1.1-14 6-4.9 3.6-7.3 6-7.1 7.1.4 2.1 13 6.4 25.6 8.8a71.2 71.2 0 0034.7-1.7 40 40 0 0021.5-22.8c4-10 7.8-14.1 15.5-17 12.1-4.5 23.7-5 83-3.7 43 1 46 .7 56.8-5.1a61.2 61.2 0 0021.7-25l5.3-11.3c1.5-3 3.3-3.4 9.5-2.5 8.5 1.3 14.2 10.3 12.7 20-1 7.1-10 21-20 31.2a51.2 51.2 0 01-17.6 13.2 80.1 80.1 0 01-32.4 8.2c-6.6.6-12.3 1.4-12.6 1.7-.3.4-1.9 4.5-3.4 9.2a156 156 0 01-27.9 49.5c-9.4 11.5-31 32.7-50 49.4-42.3 36.7-50 43.7-62.3 56-42.7 42.5-64.2 84-68.2 132a44.7 44.7 0 01-1.7 11c-1.1 1-100 34.1-101.8 34.1-.8 0-3-4.2-5.4-9.8zM38 266.4V38h166.4c94.3 0 166.7.4 167 .9a43 43 0 01-3.5 10.7C358.3 73.2 350.2 98 333.5 154c-21.2 71.6-31.6 100.2-48.4 134a294.2 294.2 0 01-59 83c-27.8 27.8-58.2 49.3-121.2 85.6A1912.7 1912.7 0 0051.4 488 96.3 96.3 0 0139 495c-.8 0-1.1-61.8-1.1-228.5zm729.8-67c-2.6-.9-6.3-3-8.4-5-3.3-3.2-3.6-3.8-3-7.2 1-5.1 4.5-11.2 8-13.7 4.8-3.6 12.7-2.9 22.7 2.1 8.4 4.2 19.9 12.5 19.9 14.3 0 .5-2.1 2-4.8 3.3-12.8 6.5-26 8.8-34.4 6.2zm113.5-75c-1.8-.7-3.3-2-3.3-3 0-.8 1.1-5.4 2.5-10.1 6.5-22.4-.4-46.5-18.8-66-2.7-3-4.6-5.8-4.2-6.3.4-.8 16.7-1.1 51-1.1 44.4 0 50.5.2 52 1.6 1.2 1.3 1.5 4.4 1.5 18.2 0 9.2-.4 17.2-.8 17.9-.5.8-7 .3-25.5-2.4-18-2.5-25.1-3.2-26-2.4-1.2.9-1.1 2.4 0 8.3 1 4.7 1.3 11.5 1 18.8-.4 13.5-2.5 19.3-9 24.4-3.2 2.6-4.8 3-10.5 3.3-4.3.2-7.9-.2-10-1.1z" />
									<path fill="#e3001b"
										  d="M0 500V0h1000v1000H0V500zm983.5 0V16.5h-967L16.2 499c-.1 265.4 0 483 .3 483.8.3 1 97.6 1.2 483.7 1l483.3-.3V500zm-564 461c-1.2-.4-3.5-3.7-5.2-7.1-7.7-15.7-5.8-29.1 5.3-37 3.8-2.8 4.4-3.7 4.4-7a11 11 0 00-2-6.3c-1.9-2.3-2.9-2.6-7.8-2.6a22 22 0 01-18-8.5c-11-12.6-11.1-38.4-.1-59.5 11.6-22.4 32.2-31.6 46.8-21 4.6 3.4 7.6 3.7 10.9 1.4 4-2.8 4.5-5 2.7-12-2.6-10.3-.3-17.7 7.8-25.1 22.7-21 73-16 84.7 8.3 2.2 4.5 2.4 10.4.4 21.5-.5 3.1-.2 3.9 2.6 6.2 3.5 3 8 3.5 10 1.1 4-4.6 13.7-6.9 21.4-4.9 7.4 2 12 5 18.8 12.4 14.8 15.9 20.9 43.7 13.8 62.6a25.4 25.4 0 01-24.2 17.5c-5.2 0-8.2 2.5-9.3 7.8-.6 3-.3 3.6 5.4 8.8 7.7 7.1 9.6 11.7 8.9 21.4-.7 8.7-6.3 21.4-10 22.4-3.5.9-165 .6-167.4-.4zM522 940.1a67.9 67.9 0 0034.5-27 63 63 0 006.1-50.7 62.9 62.9 0 00-38.6-40.1 71 71 0 00-35-1.9A63 63 0 00448 853c-9.3 19-9.3 37 0 56a62.6 62.6 0 0040.1 32 69.2 69.2 0 0033.8-.9zm373 10a492.5 492.5 0 01-282-179.5c-12.6-16.5-29.3-41.7-28.2-42.8a81 81 0 0117.3.2l54.9 3 55 3c47 2.7 48.5 2.6 61-3.2a71.9 71.9 0 0025.5-25.2c3.6-7 5-8.1 9.8-7.2 11.2 2 16 16.3 9.6 28.8-3 6-20 22.5-27.9 27-3.6 2.2-10.1 5-14.5 6.5-7.4 2.5-9.3 2.7-25 2.8-9.3 0-28.2-.6-41.8-1.4-18.8-1-25-1.1-26-.2-2.2 2.2 10.2 24.3 22.5 40.3 19.9 25.6 47.3 47 93.3 72.8 56.4 31.6 67.9 38.6 86 52.7a197 197 0 0126.5 24.4c0 1.3-2.2 1-16-2zM547.2 656.1a490.1 490.1 0 01-36.2-176c0-6.2 0-6.2 5.5-11.5a63.2 63.2 0 0138.8-15.7h3.5l35.1 65.4c19.3 36 35.8 66 36.6 66.6 1.8 1.5 2.6 0 5.5-10.4 7-25.1 21.3-52 40.9-76.4 4.5-5.7 8-11 8-12-.3-.8-30.7-25.3-67.7-54.3L549.8 379l-19.1-.2-19.2-.3-.3-169.4c-.2-135 0-169.6 1-170.3 1.9-1.1 311-1 312.8.2 1.3.8 1 3.5-1.4 21.2A471.3 471.3 0 00820 92l-.6 11.4-7 3.9a635.7 635.7 0 00-44.5 26.2 3505.6 3505.6 0 00-135.5 107.7 34.9 34.9 0 00-9.8 19c-.2 3.8 0 4.3 3.7 6.2 5.7 3 8.1 6.8 8.1 12.6 0 6.1-2.5 9.7-9 12.8-4.6 2.3-4.7 2.4-4 6a22.9 22.9 0 0016 18.8c3.1.9 6.7 1.3 8 1 1.2-.3 5.4-3 9.1-5.9 5.2-4 7.2-6.2 8.2-9.2 1.7-5.1 3.4-6.9 9-9.6 6.8-3.1 18.3-3 24.9.2 16 8 9.3 22.7-11.4 24.6-6.2.6-7.4 1.1-14 6-4.9 3.6-7.3 6-7.1 7.1.4 2.1 13 6.4 25.6 8.8a71.2 71.2 0 0034.7-1.7 40 40 0 0021.5-22.8c4-10 7.8-14.1 15.5-17 12.1-4.5 23.7-5 83-3.7 43 1 46 .7 56.8-5.1a61.2 61.2 0 0021.7-25l5.3-11.3c1.5-3 3.3-3.4 9.5-2.5 8.5 1.3 14.2 10.3 12.7 20-1 7.1-10 21-20 31.2a51.2 51.2 0 01-17.6 13.2 80.1 80.1 0 01-32.4 8.2c-6.6.6-12.3 1.4-12.6 1.7-.3.4-1.9 4.5-3.4 9.2a156 156 0 01-27.9 49.5c-9.4 11.5-31 32.7-50 49.4-42.3 36.7-50 43.7-62.3 56-42.7 42.5-64.2 84-68.2 132a44.7 44.7 0 01-1.7 11c-1.1 1-100 34.1-101.8 34.1-.8 0-3-4.2-5.4-9.8zm220.6-456.7c-2.6-.9-6.3-3-8.4-5-3.3-3.2-3.6-3.8-3-7.2 1-5.1 4.5-11.2 8-13.7 4.8-3.6 12.7-2.9 22.7 2.1 8.4 4.2 19.9 12.5 19.9 14.3 0 .5-2.1 2-4.8 3.3-12.8 6.5-26 8.8-34.4 6.2zm113.5-75c-1.8-.7-3.3-2-3.3-3 0-.8 1.1-5.4 2.5-10.1 6.5-22.4-.4-46.5-18.8-66-2.7-3-4.6-5.8-4.2-6.3.4-.8 16.7-1.1 51-1.1 44.4 0 50.5.2 52 1.6 1.2 1.3 1.5 4.4 1.5 18.2 0 9.2-.4 17.2-.8 17.9-.5.8-7 .3-25.5-2.4-18-2.5-25.1-3.2-26-2.4-1.2.9-1.1 2.4 0 8.3 1 4.7 1.3 11.5 1 18.8-.4 13.5-2.5 19.3-9 24.4-3.2 2.6-4.8 3-10.5 3.3-4.3.2-7.9-.2-10-1.1z" />
									<path fill="#020202"
										  d="M0 500V0h1000v1000H0V500zm983.5 0V16.5h-967L16.2 499c-.1 265.4 0 483 .3 483.8.3 1 97.6 1.2 483.7 1l483.3-.3V500z" />
								</svg></span>
							<span class="flex flex-col">SVWS-NRW <span class="text-sm">Version {{version}}</span></span>
						</h1>
						<div class="login-input-group">
							<svws-ui-text-input v-model="serverAddress" type="text" placeholder="Server Addresse" />
							<svws-ui-button type="secondary" @click="connectClicked">
								Verbinden
							</svws-ui-button>
						</div>
						<Transition>
							<div v-if="inputDBSchema" class="login-input-group">
								<svws-ui-multi-select v-model="inputDBSchema" title="DB-Schema" :items="inputDBSchemata" :item-text="get_name" />
								<svws-ui-text-input v-model="username" type="text" placeholder="Benutzername" />
								<svws-ui-text-input
									v-model="password"
									type="password"
									placeholder="Passwort"
									@keyup.enter="login"
								/>
								<svws-ui-button @click="login">Anmelden</svws-ui-button>
								<div v-if="main.config.isAuthenticated === false" class="text-red-500 font-bold">Fehler bei der Anmeldung. Passwort oder Benutzername falsch.</div>
							</div>
							<div v-else class="login-input-group">
								<div v-if="!connection_ok" class="text-red-500 font-bold">Fehler beim Verbinden zum Server</div>
							</div>
						</Transition>
					</div>
				</div>
				<div class="login-footer lg:px-4">
					<img class="lg:order-last login-footer-logo" src="/images/MSB_NRW_Logo.svg" />
					<div class="lg:pr-16">
						<p class="login-footer-hint mb-1">
							<span class="login-footer-hint-label">Hinweis:</span> Um
							eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW
							möglichst auf geschlechtsneutrale Begriffe wie
							Lehrkräfte, Klassenleitung, Erzieher usw.
							zurückgegriffen. An Stellen, wo das nicht möglich ist,
							wird versucht alle Geschlechter gleichermaßen zu
							berücksichtigen.
						</p>
						<nav class="login-footer-links">
							<a class="login-footer-link" href="#">Impressum</a>
							<a class="login-footer-link" href="#">Datenschutz</a>
							<a class="login-footer-link" href="#">Hilfe</a>
						</nav>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">
import { computed, ComputedRef, Ref, ref, watch, WritableComputedRef } from "vue";
import { DBSchemaListeEintrag } from "@svws-nrw/svws-core-ts";
import { injectMainApp, Main } from "~/apps/Main";
import { router } from "~/router";
import { useRoute } from "vue-router";
import { version } from '../../version';

const route = useRoute();

const serverAddress = ref("localhost");
const username = ref("Admin");
const password = ref("");

const connection_ok: Ref<boolean> = ref(true)

const main: Main = injectMainApp();
const inputDBSchemata: ComputedRef<DBSchemaListeEintrag[] | undefined> = computed(() => {
	return main.config.dbSchemata;
});

watch(() => main.config.pending, (pending: boolean) => {
				const guards = ['/', '/login']
				const redirect = route.query.redirect as string | undefined
				if (!pending) {
					const path: string = redirect && !guards.includes(redirect)
						? redirect
						: '/schueler'
					router.push(path)
				}
	})

main.connectTo(window.location.hostname + ":" + window.location.port)

const inputDBSchema: WritableComputedRef<DBSchemaListeEintrag | undefined> = computed({
	set(val: DBSchemaListeEintrag | undefined) {
		if (main.config) {
			main.config.dbSchema = val;
		}
	},
	get(): DBSchemaListeEintrag | undefined {
		return main.config.dbSchema;
	}
});

function get_name(i: DBSchemaListeEintrag): string | undefined {
	return i?.name?.toString();
};

async function connectClicked() {
	connection_ok.value = true;
	connection_ok.value = await main.connectTo(serverAddress.value);
};

async function login() {
	await main.authenticate(username.value, password.value);
}
</script>

<style>
.login-wrapper {
	@apply flex h-full w-screen flex-col justify-between p-4 lg:p-8;
}

.login-container {
	@apply bg-cover bg-center rounded-2xl h-full p-8 lg:p-16 flex flex-col justify-center items-center;
	background-image: url("/images/placeholder-background.jpg");
}

.login-form {
	@apply w-full space-y-8 p-4 lg:p-8 bg-white rounded-xl flex flex-col items-center;
	max-width: 40rem;
}

.login-input-group .wrapper {
	@apply w-full;
}

.login-input-group {
	@apply space-y-4 w-full text-center flex flex-col items-center;
}

.login-form-header {
	@apply flex flex-row items-center space-x-4 text-3xl font-bold;
}

.login-form-logo {
	@apply h-12 w-12;
}

.login-footer {
	@apply pt-8 flex flex-wrap lg:flex-nowrap justify-between;
}

.login-footer-logo {
	@apply h-auto w-80 mb-4 lg:mb-0;
}

.login-footer-links {
	@apply flex flex-row items-center space-x-2;
}

.login-footer-link {
	@apply inline-block underline;
}

.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
