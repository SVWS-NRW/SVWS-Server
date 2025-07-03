<template>
	<ui-login-layout :version :githash application="Administrative Verwaltung" under-construction>
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<div class="grid grow grid-cols-1 gap-3 justify-items-center py-0.5">
				<svws-ui-text-input v-model.trim="inputHostname" type="text" url placeholder="Serveradresse" @keyup.enter="connect" @focus="inputFocus = true" />
				<svws-ui-button type="secondary" @click="connect" :disabled="!(!connected || connecting || inputFocus )" :class="{'opacity-25 hover:opacity-100': connected && !inputFocus}">
					<span v-if="!connected || connecting || inputFocus">Verbinden</span>
					<span v-else>Verbunden</span>
					<svws-ui-spinner :spinning="connecting" />
					<span class="icon i-ri-check-line" v-if="!connecting && connected && !inputFocus" />
				</svws-ui-button>
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

	import { computed, ref, shallowRef, watch } from "vue";
	import type { LoginProps } from "./SLoginProps";
	import { version } from '../../version';
	import { githash } from '../../githash';

	const props = defineProps<LoginProps>();

	const firstauth = ref(true);
	const username = ref("root");
	const password = ref("");

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);

	const connection_failed = ref(false);
	const authentication_success = ref(false);

	const connected = shallowRef<boolean>(false);

	const inputHostname = computed<string>({
		get: () => props.hostname,
		set: (value) => props.setHostname(value),
	});

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		try {
			connected.value = await props.connectTo(props.hostname);
			if (!connected.value)
				throw new Error("Verbindung zum Server fehlgeschlagen. Bitte die Serveradresse prüfen und erneut versuchen.");
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
		if (!props.authenticated)
			throw new Error("Passwort oder Benutzername falsch.");
	}

</script>
