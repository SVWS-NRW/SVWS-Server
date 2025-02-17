<template>
	<ui-login-layout :version :githash application="Web Noten Manager">
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<div class="grid grow grid-cols-1 gap-3 justify-items-center">
				<svws-ui-text-input v-model.trim="inputHostname" type="text" url placeholder="Serveraddresse" @keyup.enter="connect" @focus="inputFocus = true" :debounce-ms="0" />
				<svws-ui-button type="secondary" @click="connect" :disabled="!(!serverFound || connecting || inputFocus )" :class="{'opacity-25 hover:opacity-100': serverFound && !inputFocus}">
					<span v-if="!serverFound || connecting || inputFocus">Verbinden</span>
					<span v-else>Verbunden</span>
					<svws-ui-spinner :spinning="connecting" />
					<span class="icon i-ri-check-line" v-if="!connecting && serverFound && !inputFocus" />
				</svws-ui-button>
			</div>
			<Transition>
				<svws-ui-input-wrapper v-if="serverFound && !connecting" class="mt-10" center>
					<svws-ui-text-input v-model.trim="username" type="text" placeholder="Benutzername" @keyup.enter="doLogin" ref="refUsername" />
					<svws-ui-text-input v-model.trim="password" type="password" placeholder="Passwort" @keyup.enter="doLogin" />
					<svws-ui-spacing />
					<div class="flex gap-2">
						<svws-ui-modal-hilfe> <s-login-hilfe /> </svws-ui-modal-hilfe>
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
	<svws-ui-notifications v-if="error">
		<svws-ui-notification type="error">
			<template #header> {{ error.name }} </template>
			{{ error.message }}
		</svws-ui-notification>
	</svws-ui-notifications>
</template>

<script setup lang="ts">

	import { computed, nextTick, onMounted, ref, shallowRef } from "vue";
	import { type ComponentExposed } from "vue-component-type-helpers";
	import { version } from '../../version';
	import { githash } from '../../githash';
	import type { LoginProps } from "./SLoginProps";
	import { JsonCoreTypeReaderStatic } from "../../../core/src/asd/utils/JsonCoreTypeReaderStatic";
	import SvwsUiTextInput from "@ui/ui/controls/SvwsUiTextInput.vue";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";

	const props = defineProps<LoginProps>();

	const refUsername = ref<ComponentExposed<typeof SvwsUiTextInput>>();
	const firstauth = ref(true);
	const username = ref("");
	const password = ref("");
	const error = ref<{name: string; message: string;}|null>(null);
	const copied = ref<boolean|null>(null);

	onMounted(() => {
		try {
			const set = new Set();
			set.difference(new Set());
		} catch (e) {
			error.value = {name: "Achtung", message: "Ihr Browser ist veraltet. Bitte aktualisieren Sie Ihren Browser auf eine aktuelle Version. Die weitere Nutzung wird zu Fehlern im ENM-Client führen."};
		}
	})

	async function copyToClipboard() {
		try {
			await navigator.clipboard.writeText(`${version} ${githash}`);
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);

	const connection_failed = ref(false);
	const authentication_success = ref(false);

	const serverFound = shallowRef<boolean>(false);

	const inputHostname = computed<string>({
		get: () => props.hostname,
		set: (value) => props.setHostname(value),
	});

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	async function initCoreTypes() {
		const reader = new JsonCoreTypeReaderStatic();
		reader.readAll();
	}

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		error.value = null;
		try {
			await props.connectTo(props.hostname);
			serverFound.value = true;
			await initCoreTypes();
		} catch (e) {
			connection_failed.value = true;
			connecting.value = false;
			const message = e instanceof DeveloperNotificationException ? e.message : "Verbindung zum Server fehlgeschlagen. Bitte die Serveradresse prüfen und erneut versuchen.";
			error.value = {name: "Serverfehler", message};
			return;
		}
		connection_failed.value = false;
		connecting.value = false;
		await nextTick(() => {
			refUsername.value?.doFocus();
		});
	}

	async function doLogin() {
		inputFocus.value = false;
		error.value = null;
		authenticating.value = true;
		await props.login(username.value, password.value);
		authenticating.value = false;
		firstauth.value = false;
		if (!props.authenticated)
			error.value = {name: "Eingabefehler", message: "Passwort oder Benutzername falsch. Bitte achten Sie auch auf die Groß- Kleinschreibung beim Benutzernamen."};
	}

</script>

<style lang="postcss" scoped>

	@reference "../../../ui/src/assets/styles/index.css";

	.app--layout--login {
		@apply p-0 bg-none bg-transparent;
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
