<template>
	<ui-login-layout :version :githash application="Administrative Verwaltung" under-construction>
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<div v-if="connecting || inputFocus" class="text-left my-1">
				<span class="font-bold">Status: </span>Verbinde ...
			</div>
			<div v-else-if="!connected && !connecting" class="text-left py-4">
				<div class="font-bold pb-2">Kein Server verfügbar</div>
				<div>
					<template v-if="viteServerModeDEV">
						<div>Der Server ist nicht erreichbar. Haben Sie ihn gestartet und wenn ja, ist dem SVWS-Client die Adresse mit Port bekannt?</div>
						<div v-if="viteProxyAdresse !== undefined">Es ist in einer <code>.env</code>-Datei die Server-Adresse <code>{{ viteProxyAdresse }}</code> angegeben. Bitte überprüfen Sie, ob der Server unter dieser Adresse erreichbar ist.</div>
						<div v-else>Wenn der SVWS-Server läuft, aber nicht unter der gleichen Adresse wie der Client erreichbar ist, müssen Sie eine <code>.env.local</code>-Datei anlegen mit dem Wert: <code>VITE_SERVER_API_URL=https://serveradresse:port</code></div>
					</template>
					<div class="text-justify py-4">
						Bitte prüfen Sie, ob eine aktive Netzwerkverbindung zum SVWS-Server vorhanden ist.<br> Sollte dieses Problem weiterhin bestehen,
						wenden Sie sich bitte an Ihren schulischen IT-Support oder an das Fachberaterteam.
					</div>
				</div>
			</div>
			<Transition>
				<svws-ui-input-wrapper v-if="connected && !connecting" class="mt-1" center>
					<svws-ui-text-input v-model.trim="username" type="text" placeholder="Benutzername" @keyup.enter="doLogin" />
					<svws-ui-text-input v-model.trim="password" type="password" placeholder="Passwort" @keyup.enter="doLogin" />
					<svws-ui-spacing />
					<div class="flex gap-2">
						<svws-ui-button type="transparent" disabled>
							Hilfe
						</svws-ui-button>
						<svws-ui-button @click="doLogin" type="primary" :disabled="authenticating">
							Anmelden
							<svws-ui-spinner v-if="authenticating" spinning />
							<span class="icon i-ri-login-circle-line" v-else />
						</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</Transition>
		</template>
	</ui-login-layout>
</template>

<script setup lang="ts">

	import { onMounted, ref, shallowRef, watch } from "vue";
	import type { LoginProps } from "./SLoginProps";
	import { version } from "@version";
	import { githash } from "@githash";

	const props = defineProps<LoginProps>();

	const viteProxyAdresse = ref();
	const viteServerModeDEV = ref(false);

	onMounted(() => {
		viteProxyAdresse.value = import.meta.env.VITE_SERVER_API_URL;
		viteServerModeDEV.value = import.meta.env.DEV;
	});

	const firstauth = ref(true);
	const username = ref("root");
	const password = ref("");

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);

	const connection_failed = ref(false);
	const authentication_success = ref(false);

	const connected = shallowRef<boolean>(false);

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		try {
			connected.value = await props.connectTo();
		} catch (error) {
			connection_failed.value = true;
			connecting.value = false;
			throw error;
		}
		connection_failed.value = false;
		connecting.value = false;
	}

	watch(() => props.authenticated, (value) => {
		if (value) {
			authentication_success.value = true;
			document.documentElement.style.backgroundImage = "none";
			const error = new Error();
			error.name = 'resetAllErrors';
			throw error;
		}
	});

	async function doLogin() {
		inputFocus.value = false;
		authenticating.value = true;
		await props.login(username.value, password.value);
		authenticating.value = false;
		firstauth.value = false;
		if (!props.authenticated) {
			throw new Error("Passwort oder Benutzername falsch.");
		}
	}

</script>
