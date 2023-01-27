<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="login-wrapper">
				<div class="inline-flex bg-white py-2 px-3 rounded-lg absolute bottom-3 right-3">
					<img class="h-16" src="/images/MSB_NRW_Logo.svg">
				</div>
				<div class="login-container">
					<div class="login-form modal modal--sm">
						<div class="modal--content-wrapper pt-3 pb-2 px-4">
							<div class="modal--content">
								<div class="login-form-header mb-8">
									<h1 class="leading-none py-3 text-center w-full">
										<span class="font-black">SVWS <span class="font-normal">NRW</span></span>
									</h1>
								</div>
								<div class="w-full mt-1 flex flex-col gap-3 items-center">
									<svws-ui-text-input v-model="serverAddress" type="text" placeholder="Server Addresse" />
									<svws-ui-button type="secondary" @click="connectClicked">
										Verbinden
									</svws-ui-button>
								</div>
								<Transition>
									<div v-if="inputDBSchema" class="flex flex-col gap-3 items-center mt-16">
										<svws-ui-multi-select v-model="inputDBSchema" title="DB-Schema" :items="inputDBSchemata" :item-text="get_name" class="w-full" />
										<svws-ui-text-input v-model="username" type="text" placeholder="Benutzername" />
										<svws-ui-text-input v-model="password"
															type="password"
															placeholder="Passwort"
															@keyup.enter="login" />
										<svws-ui-button @click="login">
											Anmelden
											<i-ri-login-circle-line/>
										</svws-ui-button>
									</div>
								</Transition>
								<div class="mt-16 text-center text-sm">
									<p class="mb-2 opacity-50">
										Powered by SVWS-NRW<br/>Client Version {{ version }}
									</p>
									<nav class="login-footer-links mb-4">
										<a class="login-footer-link" href="#">Impressum</a>
										<a class="login-footer-link" href="#">Datenschutz</a>
										<a class="login-footer-link" href="#">
											<span class="inline-flex items-center gap-0.5 align-middle">
												<span class="hover-underline">Hilfe</span> <i-ri-question-line/>
											</span>
										</a>
									</nav>
									<p class="text-sm opacity-50">
										Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf
										geschlechtsneutrale Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw.
										zurückgegriffen. An Stellen, wo das nicht möglich ist, wird versucht alle
										Geschlechter gleichermaßen zu berücksichtigen.
									</p>
								</div>
							</div>
						</div>
					</div>
					<svws-ui-notification v-if="main.config.isAuthenticated === false" type="error">
						<div class="flex items-center space-x-4">
							<i-ri-lock-2-line class="text-headline" />
							<div>
								<div class="font-bold">Anmeldung fehlgeschlagen</div>
								<div class="font-normal">Passwort oder Benutzername falsch.</div>
							</div>
						</div>
					</svws-ui-notification>
					<svws-ui-notification v-if="!connection_ok" type="error">
						<div class="flex items-center space-x-4">
							<i-ri-alert-line class="text-headline" />
							<div>
								<div class="font-bold">Verbindung zum Server fehlgeschlagen</div>
								<div class="font-normal">Bitte überprüfe die Server Addresse und versuche es erneut.</div>
							</div>
						</div>
					</svws-ui-notification>
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

	const serverAddress = ref("svws-nrw.de");
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

.modal {
	@apply shadow-black/25;
}

.login-input-group .wrapper {
	@apply w-full;
}

.login-input-group {
	@apply space-y-4 w-full text-center flex flex-col items-center;
}

.login-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 3rem;
}

.login-footer-links {
	@apply flex flex-row items-center gap-2 justify-center;
}

.login-footer-link {
	@apply inline-block;
}

.login-footer-link:hover,
.login-footer-link:focus,
.login-footer-link:hover .hover-underline,
.login-footer-link:focus .hover-underline {
	@apply underline;
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
