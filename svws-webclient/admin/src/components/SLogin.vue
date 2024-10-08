<template>
	<svws-ui-app-layout :fullwidth-content="!authentication_success" :skeleton="authentication_success" :class="{'app--layout--login': !authentication_success}">
		<template #main>
			<div class="login-wrapper">
				<div class="login-container pt-5">
					<div class="login-form modal modal--sm my-auto">
						<div class="modal--content-wrapper pb-3 pt-0">
							<span class="h-[1rem]" style="background: repeating-linear-gradient( -45deg, #000, #000 10px, #ffff00 10px, #ffff00 20px );" />
							<div class="modal--content px-5">
								<div class="mb-6 mt-2">
									<h1 class="font-bold text-headline-xl leading-none w-full py-2">
										SVWS NRW
									</h1>
									<h2 class="text-headline-sm leading-tight opacity-50">Administrative<br>Verwaltung</h2>
								</div>
								<svws-ui-input-wrapper center>
									<svws-ui-text-input v-model.trim="inputHostname" type="text" url placeholder="Serveradresse" @keyup.enter="connect" @focus="inputFocus = true" />
									<svws-ui-button type="secondary" @click="connect" :disabled="!(!connected || connecting || inputFocus )" :class="{'opacity-25 hover:opacity-100': connected && !inputFocus}">
										<span v-if="!connected || connecting || inputFocus">Verbinden</span>
										<span v-else>Verbunden</span>
										<svws-ui-spinner :spinning="connecting" />
										<span class="icon i-ri-check-line" v-if="!connecting && connected && !inputFocus" />
									</svws-ui-button>
								</svws-ui-input-wrapper>
								<Transition>
									<svws-ui-input-wrapper v-if="connected && !connecting" class="mt-10" center>
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
								<div class="mt-16 text-sm font-medium">
									<div class="flex gap-3 items-center opacity-50">
										<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-11">
										<div class="text-left">
											<p>
												Powered by SVWS-NRW
												<br><span class="font-bold">Version {{ version }}</span>  <span v-if="version.includes('SNAPSHOT')">{{ githash.substring(0, 8) }}</span>
											</p>
											<nav class="flex flex-row items-center gap-2 mt-0.5">
												<a class="login-footer-link" href="#">Impressum</a>
												<datenschutz-modal v-slot="{ openModal }">
													<a class="login-footer-link" href="#" @click="openModal()">Datenschutz</a>
												</datenschutz-modal>
											</nav>
										</div>
									</div>
									<div class="mt-3 -mb-3 opacity-50">
										<p class="text-sm text-left">
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
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
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
		set: (value) => props.setHostname(value)
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

<style lang="postcss" scoped>
	.login-wrapper {
		@apply flex h-full flex-col justify-between;
	}

	.app--layout--login {
		@apply p-0 bg-none bg-transparent;
	}

	.app--layout--login :global(.app--content-container) {
		@apply bg-white/5;
	}

	.login-container {
		@apply bg-cover bg-top h-full flex flex-col justify-center items-center px-4;
		background-image: url('/images/start-hintergrund.jpg');
	}

	.modal {
		@apply shadow-2xl shadow-black/50 rounded-3xl;
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
