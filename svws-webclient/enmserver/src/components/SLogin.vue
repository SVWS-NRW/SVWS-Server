<template>
	<ui-login-layout :version="auth.version" :githash="auth.githash" application="Web Noten Manager">
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<Transition mode="out-in">
				<!-- Zeige an, wenn der Browser veraltet ist -->
				<div v-if="browserVeraltet" class="text-ui-danger font-medium"> Ihr Browser ist veraltet und kann für den WebNotenManager nicht verwendet werden. Bitte benutzen Sie einen modernen Browser. </div>

				<!-- 1. Schritt: Anmeldung mit Benutzername und Kennwort als erstem Faktor -->
				<div v-else-if="!auth.pendingPasswordChange && !auth.pending2FA">
					<div class="grid grow grid-cols-1 gap-3 justify-items-center py-0.5">
						<svws-ui-text-input v-model.trim="inputHostname" type="text" url placeholder="Serveraddresse" @keyup.enter="connect" @focus="inputFocus = true" :debounce-ms="0" />
						<div v-if="errorMessage && !serverFound" class="text-ui-danger font-medium"> {{ errorMessage }} </div>
						<svws-ui-button type="secondary" @click="connect" :disabled="!(!serverFound || connecting || inputFocus )" :class="{'opacity-25 hover:opacity-100': serverFound && !inputFocus}">
							<span v-if="!serverFound || connecting || inputFocus">Verbinden</span>
							<span v-else>Verbunden</span>
							<svws-ui-spinner :spinning="connecting" />
							<span class="icon i-ri-check-line" v-if="!connecting && serverFound && !inputFocus" />
						</svws-ui-button>
					</div>
					<Transition>
						<svws-ui-input-wrapper v-if="serverFound && !connecting" class="mt-1" center>
							<svws-ui-text-input v-model.trim="username" type="text" placeholder="Benutzername" @keyup.enter="doLogin" @methods="handleInputMethodsUsername" />
							<svws-ui-text-input v-model.trim="password" type="password" placeholder="Passwort" @keyup.enter="doLogin" />
							<svws-ui-spacing />
							<div v-if="errorMessage" class="text-ui-danger font-medium my-2"> {{ errorMessage }} </div>
							<div class="flex gap-2">
								<svws-ui-modal-hilfe> <s-login-hilfe /> </svws-ui-modal-hilfe>
								<svws-ui-button @click="doLogin" type="primary" :disabled="authenticating || (username.length === 0) || (password.length === 0)">
									Anmelden
									<svws-ui-spinner v-if="authenticating" spinning />
									<span class="icon i-ri-login-circle-line" v-else />
								</svws-ui-button>
							</div>
						</svws-ui-input-wrapper>
					</Transition>
				</div>

				<!-- 2. Schritt: Austauschen des Kennwortes, wenn ein Initialkennwort verwendte wurde -->
				<svws-ui-input-wrapper v-else-if="auth.pendingPasswordChange" class="mt-1" center>
					<div class="text-left w-full">
						<p class="font-bold text-sm mb-2">Sicherheitshinweis:</p>
						<p class="text-sm mb-4">Ihr temporäres Initialkennwort wird nun durch ein persönliches Kennwort ersetzt. Bitte notieren Sie es sich für zukünftige Anmeldungen.</p>
						<div class="bg-ui-5 p-4 border-2 border-dashed border-ui-100 rounded-lg text-2xl font-mono text-center tracking-widest mb-4 select-all">
							<!-- {{ auth.generatedPassword?.match(/.{1,4}/g)?.join(' - ') }} -->
							{{ auth.generatedPassword }}
						</div>
						<p class="text-sm mb-4"><span class="font-bold">Bestätigen:</span> Das neue Kennwort wird aktiviert und muss fortan verwendet werden.</p>
						<p class="text-sm mb-4"><span class="font-bold">Abbrechen:</span> Das Initialkennwort bleibt vorerst gültig. Bei der nächsten Anmeldung wird ein neuer Vorschlag generiert</p>
						<div v-if="errorMessage" class="text-ui-danger font-medium"> {{ errorMessage }} </div>
					</div>
					<span v-if="expirationSeconds > 0" class="text-sm font-normal font-mono opacity-50 my-2 w-full text-center">
						Anmeldesitzung läuft in {{ formattedExpiration }} ab
					</span>
					<div class="flex flex-row gap-2 w-full mt-2">
						<svws-ui-button @click="doCancelLogin" type="secondary" class="w-full"> Abbrechen </svws-ui-button>
						<svws-ui-button @click="doConfirmPasswordChange()" type="primary" class="w-full"> Bestätigen </svws-ui-button>
					</div>
				</svws-ui-input-wrapper>

				<!-- 3. Schritt: Prüfen des zweiten Faktors -->
				<svws-ui-input-wrapper v-else-if="auth.pending2FA" class="mt-1" center>
					<div class="flex flex-col gap-2 text-left w-full">
						<template v-if="(auth.totpSetup !== null) && (otpauthUrl !== null)">
							<p class="font-bold mb-2">Es liegt eine Erstanmeldung vor.</p>
							<p class="mb-3">Scannen Sie den QR-Code in Ihrer Authenticator-App oder nutzen Sie das Secret:</p>
							<div class="flex flex-col justify-center items-center gap-3">
								<qr-code :uri="otpauthUrl" />
								<div class="bg-ui-75 p-2 border rounded font-mono select-all">
									{{ auth.totpSetup.secret }}
								</div>
							</div>
							<div class="border-t w-full mt-4 mb-4" />
						</template>
						<div>
							<p class="font-bold">Geben Sie den Code ein:</p>
							<svws-ui-text-input v-model="totpToken" placeholder="Code" :min-len="6" :max-len="6"
								@keyup.enter="doVerifyTotp" @methods="handleInputMethodsTotpToken" />
							<div v-if="errorMessage" class="text-ui-danger font-medium"> {{ errorMessage }} </div>
						</div>
						<span v-if="expirationSeconds > 0" class="text-sm font-normal font-mono opacity-50 text-center w-full my-2">
							Anmeldesitzung läuft in {{ formattedExpiration }} ab
						</span>
						<div class="flex gap-2 w-full">
							<svws-ui-button @click="doCancelLogin" type="secondary" class="w-full"> Abbrechen </svws-ui-button>
							<svws-ui-button @click="doVerifyTotp" type="primary" class="w-full" :disabled="totpToken.length !== 6"> OK </svws-ui-button>
						</div>
					</div>
				</svws-ui-input-wrapper>
			</Transition>
		</template>
	</ui-login-layout>
</template>

<script setup lang="ts">

	import { computed, nextTick, onMounted, ref, shallowRef, watch } from "vue";
	import type { LoginProps } from "./SLoginProps";
	import { JsonCoreTypeReaderStatic } from "../../../core/src/asd/utils/JsonCoreTypeReaderStatic";
	import SvwsUiTextInput from "@ui/ui/controls/SvwsUiTextInput.vue";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { useAuthState } from "~/states/AuthState";

	const props = defineProps<LoginProps>();
	const auth = useAuthState();

	const connection_failed = ref(false);
	const inputHostname = computed<string>({
		get: () => auth.hostname,
		set: (value) => auth.setHostname(value),
	});

	// Greife auf Methoden der Textinputs zurück, um dieses automatisch Fokussieren zu können
	const totpTokenInput = ref<{ focus: () => void } | undefined>(undefined);
	function handleInputMethodsTotpToken(methods: { focus: () => void } | undefined) {
		totpTokenInput.value = methods;
	}
	const usernameInput = ref<{ focus: () => void } | undefined>(undefined);
	function handleInputMethodsUsername(methods: { focus: () => void } | undefined) {
		usernameInput.value = methods;
	}

	const username = ref("");
	const password = ref("");
	const totpToken = ref<string>("");
	const browserVeraltet = ref<boolean>(false);
	const errorMessage = ref<string | null>(null);

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);
	const serverFound = shallowRef<boolean>(false);

	const isTokenValid = computed(() => totpToken.value.length === 6);
	const otpauthUrl = computed<string | null>(() => {
		if (auth.totpSetup === null) {
			return null;
		}
		const issuer = encodeURIComponent(auth.totpSetup.issuer);
		const account = encodeURIComponent(auth.totpSetup.account);
		return `otpauth://totp/${issuer}:${account}?secret=${auth.totpSetup.secret}&issuer=${issuer}`;
	});

	const now = ref(Math.floor(Date.now() / 1000));
	let timer: ReturnType<typeof setInterval> | undefined = undefined;
	const expirationSeconds = computed(() => {
		return (now.value > 0) ? auth.expirationSeconds : 0;
	});
	const formattedExpiration = computed(() => {
		const s = expirationSeconds.value;
		return `${Math.floor(s / 60)}:${(s % 60).toString().padStart(2, '0')}`;
	});

	function startTimer() {
		stopTimer();
		now.value = Math.floor(Date.now() / 1000);
		timer = globalThis.setInterval(() => {
			now.value = Math.floor(Date.now() / 1000);
		}, 1000);
	}

	function stopTimer() {
		if (timer) {
			globalThis.clearInterval(timer);
			timer = undefined;
		}
	}

	watch(expirationSeconds, async (newValue) => {
		if ((newValue <= 0) && (auth.pending2FA || auth.pendingPasswordChange)) {
			await doCancelLogin();
		}
	});

	watch(() => auth.pending2FA, async (active) => {
		if (active) {
			await nextTick();
			totpTokenInput.value?.focus();
		}
	});


	onMounted(async () => {
		try {
			const set = new Set();
			set.difference(new Set());
			// Versuche beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
			await connect();
		} catch {
			browserVeraltet.value = true;
		}
	});

	async function initCoreTypes() {
		const reader = new JsonCoreTypeReaderStatic();
		reader.readAll();
	}

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		errorMessage.value = null;
		try {
			await auth.connectTo(auth.hostname);
			serverFound.value = true;
			await initCoreTypes();
			connection_failed.value = false;
		} catch (e) {
			serverFound.value = false;
			connection_failed.value = true;
			const message = e instanceof DeveloperNotificationException ? e.message : "Verbindung zum Server fehlgeschlagen. Bitte die Serveradresse prüfen und erneut versuchen.";
			errorMessage.value = message;
		} finally {
			connecting.value = false;
			await nextTick();
			usernameInput.value?.focus();
		}
	}

	async function doLogin() {
		inputFocus.value = false;
		errorMessage.value = null;
		try {
			authenticating.value = true;
			const { success, message } = await auth.login(username.value, password.value);
			if (!success) {
				errorMessage.value = message ?? "unbekannter Grund";
				return;
			}
			if (auth.authenticated && !auth.pending2FA && !auth.pendingPasswordChange) {
				await props.finishLogin();
			} else {
				startTimer();
			}
		} finally {
			authenticating.value = false;
		}
	}


	async function doCancelLogin(): Promise<void> {
		stopTimer();
		await auth.logout();

		errorMessage.value = null;
		totpToken.value = "";
		username.value = "";
		password.value = "";

		await nextTick();
		usernameInput.value?.focus();
	}


	async function doConfirmPasswordChange(): Promise<void> {
		errorMessage.value = null;
		const { success, message } = await auth.confirmPasswordChange();
		if (success) {
			if (auth.authenticated && !auth.pending2FA) {
				stopTimer();
				await props.finishLogin();
			}
		} else {
			errorMessage.value = message ?? "Das Kennwort konnte nicht bestätigt werden.";
		}
	}

	/**
	 * Prüfe das Token uns schließe im Erfolgsfall die Eingabemöglichkeit.
	 */
	async function doVerifyTotp(): Promise<void> {
		if (!isTokenValid.value) {
			return;
		}
		errorMessage.value = null;
		const { success, message } = await auth.verifyTotp(totpToken.value);
		if (success) {
			stopTimer();
			await props.finishLogin();
		} else {
			// Bei einem Fehler wieder zur Eingabe zurückkehren
			errorMessage.value = message ?? "Der eingegebene Code ist ungültig. Bitte versuchen Sie es erneut.";
			totpToken.value = "";
			totpTokenInput.value?.focus();
		}
	}

</script>
