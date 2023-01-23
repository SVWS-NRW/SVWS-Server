<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="login-wrapper">
				<div class="login-container">
					<div class="login-form">
						<h1 class="login-form-header">
							<span class="login-form-logo">
								<img src="/images/Wappenzeichen_NRW.svg" alt="Wappenzeichen NRW">
							</span>
							<span>SVWS NRW</span>
						</h1>
						<div class="flex items-center w-full space-x-4 mt-1">
							<svws-ui-text-input v-model="serverAddress" type="text" placeholder="Server Addresse" />
							<svws-ui-button type="secondary" @click="connectClicked">
								Verbinden
							</svws-ui-button>
						</div>
						<Transition>
							<div v-if="inputDBSchema" class="login-input-group mt-16">
								<svws-ui-multi-select v-model="inputDBSchema" title="DB-Schema" :items="inputDBSchemata" :item-text="get_name" />
								<div class="flex space-x-4 w-full">
									<svws-ui-text-input v-model="username" type="text" placeholder="Benutzername" />
									<svws-ui-text-input v-model="password"
										type="password"
										placeholder="Passwort"
										@keyup.enter="login" />
								</div>
								<div class="w-full flex justify-end items-start">
									<div v-if="main.config.isAuthenticated === false" class="text-left mr-4 leading-tight text-red-500 font-bold">Fehler bei der Anmeldung. Passwort oder Benutzername falsch.</div>
									<div v-else class="text-left mr-4 leading-tight w-full my-auto opacity-50">Verbunden mit {{ serverAddress }}</div>
									<svws-ui-button @click="login">Anmelden</svws-ui-button>
								</div>
							</div>
							<div v-else class="login-input-group">
								<div v-if="!connection_ok" class="text-red-500 font-bold mt-4">Fehler beim Verbinden zum Server.</div>
							</div>
						</Transition>
					</div>
				</div>
				<div class="login-footer">
					<img class="lg:order-last login-footer-logo" src="/images/MSB_NRW_Logo.svg">
					<div class="lg:pr-16">
						<span class="block mb-1">Powered by SVWS-NRW, Version {{ version }}</span>
						<p class="login-footer-hint mb-1 opacity-50">
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
	import { routeSchueler } from "~/router/apps/RouteSchueler";

	const route = useRoute();

	const serverAddress = ref("localhost");
	const username = ref("Admin");
	const password = ref("");
	const defaultRoute = routeSchueler;

	const connection_ok: Ref<boolean> = ref(true)

	const main: Main = injectMainApp();

	const inputDBSchemata: ComputedRef<DBSchemaListeEintrag[] | undefined> = computed(() => {
		return main.config.dbSchemata;
	});

	watch(() => main.config.pending, (pending: boolean) => {
		const guards = ['/', '/login']
		const redirect = route.query.redirect as string | undefined
		if (!pending) {
			if (redirect && !guards.includes(redirect))
				void router.push(redirect);
			else
				void router.push({ name: defaultRoute.name });
		}
	})

	void main.connectTo(window.location.hostname + ":" + window.location.port)

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

	function get_name(i: DBSchemaListeEintrag): string {
		return i?.name?.toString() || '';
	}

	async function connectClicked() {
		connection_ok.value = true;
		connection_ok.value = await main.connectTo(serverAddress.value);
	}

	async function login() {
		await main.authenticate(username.value, password.value);
	}

</script>

<style lang="postcss" scoped>
.app-layout--wrapper {
	@apply p-4;
}

.login-wrapper {
	@apply flex h-full flex-col justify-between;
}

.login-container {
	@apply bg-cover bg-top rounded-t-2xl h-full flex flex-col justify-center items-center px-4;
	/*background-image: radial-gradient(rgba(0,0,0,0.25), rgba(0,0,0,0.3)), url("/images/login-background-1.jpg");*/
	/*background-image: radial-gradient(rgba(0,0,0,0.15), rgba(0,0,0,0.2)), url("/images/placeholder-background.jpg");*/
	background-image: url("/images/placeholder-background.jpg");
}

.login-form {
	@apply w-full p-4 lg:p-8 bg-white rounded-xl flex flex-col items-center;
	max-width: 40rem;
	@apply shadow-lg;
}

.login-input-group .wrapper {
	@apply w-full;
}

.login-input-group {
	@apply space-y-4 w-full text-center flex flex-col items-center;
}

.login-form-header {
	@apply flex flex-row items-center space-x-4 font-bold leading-tight mb-6;
	font-size: 2.618rem;
}

.login-form-logo {
	@apply h-[0.8em] w-[0.8em];
}

.login-footer {
	@apply p-8 flex flex-wrap lg:flex-nowrap justify-between items-center;
}

.login-footer-logo {
	@apply h-auto w-80 mb-4 lg:mb-0;
}

.login-footer-links {
	@apply flex flex-row items-center space-x-2;
}

.login-footer-link {
	@apply inline-block underline opacity-50;
}

.login-footer-link:hover,
.login-footer-link:focus {
	@apply opacity-100 no-underline;
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
