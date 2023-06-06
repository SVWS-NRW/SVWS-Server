<template>
	<svws-ui-app-layout :fullwidth-content="true" class="app--layout--login">
		<template #main>
			<div class="login-wrapper">
				<div class="login-container">
					<div class="login-form modal modal--sm">
						<div class="modal--content-wrapper">
							<div class="modal--content">
								<div class="login-form-header mb-8 px-6 py-6 mt-4">
									<h1 class="flex gap-2 leading-none font-bold w-full justify-center text-headline-xl">
										<span>SVWS</span>
										<span>NRW</span>
									</h1>
									<h2>Schulverwaltung</h2>
								</div>
								<svws-ui-input-wrapper class="px-6" center>
									<svws-ui-text-input v-model="inputHostname" type="text" url placeholder="Serveraddresse" @keyup.enter="connect" @focus="inputFocus = true" />
									<svws-ui-button type="secondary" @click="connect" :disabled="connecting" :class="{'opacity-25 hover:opacity-100': inputDBSchemata.size() > 0 && !inputFocus}">
										<span v-if="inputDBSchemata.size() === 0 || connecting || inputFocus">Verbinden</span>
										<span v-else>Verbunden</span>
										<svws-ui-spinner :spinning="connecting" />
										<i-ri-check-line v-if="!connecting && inputDBSchemata.size() > 0 && !inputFocus" />
									</svws-ui-button>
								</svws-ui-input-wrapper>
								<Transition>
									<svws-ui-input-wrapper v-if="inputDBSchemata.size() > 0 && !connecting" class="mt-9 px-6" center>
										<svws-ui-multi-select v-model="schema" title="DB-Schema" :items="inputDBSchemata" :item-text="get_name" class="w-full" @update:model-value="setSchema" />
										<svws-ui-text-input v-model="username" type="text" placeholder="Benutzername" @keyup.enter="doLogin" />
										<svws-ui-text-input v-model="password" type="password" placeholder="Passwort" @keyup.enter="doLogin" />
										<svws-ui-button @click="doLogin" type="primary" :disabled="authenticating">
											Anmelden
											<svws-ui-spinner v-if="authenticating" spinning />
											<i-ri-login-circle-line v-else />
										</svws-ui-button>
									</svws-ui-input-wrapper>
								</Transition>
								<div class="mt-12 text-center text-sm font-medium">
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
									<div class="mt-8 px-6 pt-4 pb-5">
										<p class="text-sm opacity-50 text-left">
											Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf
											geschlechtsneutrale Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw.
											zurückgegriffen. An Stellen, wo das nicht möglich ist, wird versucht alle
											Geschlechter gleichermaßen zu berücksichtigen.
										</p>
										<img class="h-12 mt-6" src="/images/MSB_NRW_Logo.svg" alt="Das NRW-Logo">
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

	import type { DBSchemaListeEintrag, List } from "@svws-nrw/svws-core";
	import type { LoginProps } from "./SLoginProps";
	import { computed, ref } from "vue";
	import { ArrayList } from "@svws-nrw/svws-core";
	import { version } from '../../version';

	const props = defineProps<LoginProps>();

	const firstauth = ref(true);
	const schema = ref<DBSchemaListeEintrag | undefined>();
	const username = ref("Admin");
	const password = ref("");

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);

	const connection_failed = ref(false);

	const inputDBSchemata = ref<List<DBSchemaListeEintrag>>(new ArrayList<DBSchemaListeEintrag>());

	const inputHostname = computed<string>({
		get: () => props.hostname,
		set: (value) => props.setHostname(value)
	});

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	function get_name(i: DBSchemaListeEintrag): string {
		return i.name ?? '';
	}

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		try {
			inputDBSchemata.value = await props.connectTo(props.hostname);
			if (inputDBSchemata.value.size() <= 0)
				throw new Error("Verbindung zum Server fehlgeschlagen. Bitte die Serveradresse prüfen und erneut versuchen.");
		} catch (error) {
			connection_failed.value = true;
			connecting.value = false;
			throw error;
		}
		let hasDefault = false;
		for (const s of inputDBSchemata.value) {
			if (s.isDefault) {
				schema.value = s;
				hasDefault = true;
			}
			if (s.name === props.schemaPrevious) {
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

	async function doLogin() {
		inputFocus.value = false;
		if ((schema.value === undefined) || (schema.value.name === null))
			throw new Error("Es muss ein gültiges Schema ausgewählt sein.");
		authenticating.value = true;
		await props.login(schema.value.name, username.value, password.value);
		authenticating.value = false;
		firstauth.value = false;
		if (!props.authenticated)
			throw new Error("Passwort oder Benutzername falsch.");
	}

	// const gradients = [
	// 	[{color:"00000c",position:0},{color:"00000c",position:0}],
	// 	[{color:"020111",position:85},{color:"191621",position:100}],
	// 	[{color:"020111",position:60},{color:"20202c",position:100}],
	// 	[{color:"020111",position:10},{color:"3a3a52",position:100}],
	// 	[{color:"20202c",position:0},{color:"515175",position:100}],
	// 	[{color:"40405c",position:0},{color:"6f71aa",position:80},{color:"8a76ab",position:100}],
	// 	[{color:"4a4969",position:0},{color:"7072ab",position:50},{color:"cd82a0",position:100}],
	// 	[{color:"757abf",position:0},{color:"8583be",position:60},{color:"eab0d1",position:100}],
	// 	[{color:"82addb",position:0},{color:"ebb2b1",position:100}],
	// 	[{color:"94c5f8",position:1},{color:"a6e6ff",position:70},{color:"b1b5ea",position:100}],
	// 	[{color:"b7eaff",position:0},{color:"94dfff",position:100}],
	// 	[{color:"9be2fe",position:0},{color:"67d1fb",position:100}],
	// 	[{color:"90dffe",position:0},{color:"38a3d1",position:100}],
	// 	[{color:"57c1eb",position:0},{color:"246fa8",position:100}],
	// 	[{color:"2d91c2",position:0},{color:"1e528e",position:100}],
	// 	[{color:"2473ab",position:0},{color:"1e528e",position:70},{color:"5b7983",position:100}],
	// 	[{color:"1e528e",position:0},{color:"265889",position:50},{color:"9da671",position:100}],
	// 	[{color:"1e528e",position:0},{color:"728a7c",position:50},{color:"e9ce5d",position:100}],
	// 	[{color:"154277",position:0},{color:"576e71",position:30},{color:"e1c45e",position:70},{color:"b26339",position:100}],
	// 	[{color:"163C52",position:0},{color:"4F4F47",position:30},{color:"C5752D",position:60},{color:"B7490F",position:80},{color:"2F1107",position:100}],
	// 	[{color:"071B26",position:0},{color:"071B26",position:30},{color:"8A3B12",position:80},{color:"240E03",position:100}],
	// 	[{color:"010A10",position:30},{color:"59230B",position:80},{color:"2F1107",position:100}],
	// 	[{color:"090401",position:50},{color:"4B1D06",position:100}],
	// 	[{color:"00000c",position:80},{color:"150800",position:100}],
	// ];

	// function toCSSGradient(data) {
	// 	let css = "linear-gradient(to bottom, ";
	// 	let len = data.length;

	// 	for (let i = 0; i < len; i++) {
	// 		let item = data[i];
	// 		css += " #" + item.color + " " + item.position + "%";
	// 		if (i < len - 1) css += ",";
	// 	}
	// 	return css + ")";
	// }

	// const now = new Date();
	// const hour = now.getHours();
	// const gradient = gradients[hour];
	/*document.documentElement.style.backgroundImage = toCSSGradient(gradient);*/

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
	/*background-image: radial-gradient(rgba(0,0,0,0.25), rgba(0,0,0,0.3)), url("/images/login-background-1.jpg");*/
	/*background-image: radial-gradient(rgba(0,0,0,0.15), rgba(0,0,0,0.2)), url("/images/placeholder-background.jpg");*/
	background-image: url("/images/placeholder-background.jpg");
}

.modal {
	@apply shadow-2xl shadow-black/50 rounded-2xl;
}

.login-form .modal--content {
	@apply p-0;
}

.login-form-header {
	@apply flex flex-col items-center justify-between gap-2 leading-tight;
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
