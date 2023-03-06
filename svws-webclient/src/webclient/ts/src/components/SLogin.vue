<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="login-wrapper">
				<div class="login-container">
					<div class="login-form modal modal--sm">
						<div class="modal--content-wrapper">
							<div class="modal--content">
								<div class="login-form-header mb-8 px-8 py-4 mt-6">
									<h1 class="leading-none text-center w-full">
										<span class="font-bold">SVWS <span class="font-normal">NRW</span></span>
									</h1>
								</div>
								<div class="w-full mt-1 flex flex-col gap-2 items-center px-8">
									<svws-ui-text-input v-model="inputHostname" type="text" url placeholder="Serveraddresse" />
									<svws-ui-button type="secondary" @click="connect" :disabled="connecting" @keyup.enter="connect">
										Verbinden
										<svg class="animate-spin ml-1 h-4 w-4 text-black" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" v-if="connecting">
											<circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
											<path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
										</svg>
										<i-ri-plug-line class="ml-1 w-4" v-if="!connecting && inputDBSchemata.size() === 0" />
										<i-ri-plug-fill class="ml-1 w-4" v-if="!connecting && inputDBSchemata.size() > 0" />
									</svws-ui-button>
								</div>
								<Transition>
									<div v-if="inputDBSchemata.size() > 0 && !connecting" class="flex flex-col gap-2 items-center mt-8 px-8">
										<svws-ui-multi-select v-model="schema" title="DB-Schema" :items="inputDBSchemata" :item-text="get_name" class="w-full" />
										<svws-ui-text-input v-model="username" type="text" placeholder="Benutzername" @keyup.enter="login" />
										<svws-ui-text-input v-model="password" type="password" placeholder="Passwort" @keyup.enter="login" />
										<svws-ui-button @click="login">
											Anmelden
											<i-ri-login-circle-line />
										</svws-ui-button>
									</div>
								</Transition>
								<div class="mt-12 text-center text-sm">
									<p class="mb-2 opacity-50">
										Powered by SVWS-NRW<br>Client Version {{ version }}
									</p>
									<nav class="login-footer-links mb-4">
										<a class="login-footer-link" href="#">Impressum</a>
										<a class="login-footer-link" href="#">Datenschutz</a>
										<a class="login-footer-link" href="#">
											<span class="inline-flex items-center gap-0.5 align-middle">
												<span class="hover-underline">Hilfe</span> <i-ri-question-line />
											</span>
										</a>
									</nav>
									<div class="bg-light mt-6 px-6 pt-4 pb-5">
										<img class="mb-4 w-2/3 mx-auto" src="/images/MSB_NRW_Logo.svg">
										<p class="text-sm opacity-50 text-left">
											Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf
											geschlechtsneutrale Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw.
											zurückgegriffen. An Stellen, wo das nicht möglich ist, wird versucht alle
											Geschlechter gleichermaßen zu berücksichtigen.
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<svws-ui-notifications>
						<svws-ui-notification v-if="(!props.authenticated) && (!firstauth)" type="error" icon="login">
							<template #header>
								Anmeldung fehlgeschlagen
							</template>
							<p>Passwort oder Benutzername falsch.</p>
						</svws-ui-notification>
						<svws-ui-notification v-if="connection_failed" type="error">
							<template #header>
								Verbindung zum Server fehlgeschlagen
							</template>
							<p>Bitte die Serveraddresse prüfen und erneut versuchen.</p>
						</svws-ui-notification>
					</svws-ui-notifications>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import { computed, Ref, ref, WritableComputedRef } from "vue";
	import { DBSchemaListeEintrag, List, Vector } from "@svws-nrw/svws-core-ts";
	import { version } from '../../version';

	const props = defineProps<{
		authenticated: boolean;
		hostname: string;
		setHostname: (hostname: string) => void;
		login: (schema: string, username: string, password: string) => Promise<void>;
		connectTo: (url: string) => Promise<List<DBSchemaListeEintrag>>;
	}>();

	const firstauth: Ref<boolean> = ref(true);
	const schema: Ref<DBSchemaListeEintrag | undefined> = ref(undefined);
	const username = ref("Admin");
	const password = ref("");

	const connecting = ref(false);

	const connection_failed: Ref<boolean> = ref(false);

	const inputDBSchemata: Ref<List<DBSchemaListeEintrag>> = ref(new Vector());

	const inputHostname: WritableComputedRef<string> = computed({
		get: () => props.hostname,
		set: (value) => props.setHostname(value)
	});

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	function get_name(i: DBSchemaListeEintrag): string {
		return i?.name ?? '';
	}

	async function connect() {
		connecting.value = true;
		try {
			inputDBSchemata.value = await props.connectTo(props.hostname);
		} catch (error) {
			connection_failed.value = true;
			connecting.value = false;
			return;
		}
		if (inputDBSchemata.value.size() <= 0) {
			connection_failed.value = true;
			connecting.value = false;
			return;
		}
		let hasDefault = false;
		for (const s of inputDBSchemata.value) {
			if (s.isDefault) {
				schema.value = s;
				hasDefault = true;
				break;
			}
		}
		if (!hasDefault) {
			schema.value = inputDBSchemata.value.get(0);
		}
		connection_failed.value = false;
		connecting.value = false;
	}

	async function login() {
		if ((schema.value === undefined) || (schema.value.name === null))
			throw new Error("Es muss ein gültiges Schema ausgewählt sein.");
		await props.login(schema.value.name, username.value, password.value);
		firstauth.value = false;
	}

</script>

<style lang="postcss" scoped>
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
	@apply shadow-black/25 rounded-xl;
}

.login-form .modal--content {
	@apply p-0;
}

.login-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
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
