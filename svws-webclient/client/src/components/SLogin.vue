<template>
	<ui-login-layout :version :githash>
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<div v-if="connecting || inputFocus" class="text-left my-1">
				<span class="font-bold">Status: </span>Verbinde ...
			</div>
			<div v-else-if="inputDBSchemata.isEmpty() && !connecting" class="text-left py-4">
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
				<svws-ui-input-wrapper v-if="inputDBSchemata.size() > 0" class="mt-1" center>
					<svws-ui-select v-model="schema" title="Datenbank-Schema" :items="inputDBSchemata" :item-text="i => `${i.name ?? 'SCHEMANAME FEHLT'}${i.isDeactivated ? ' (Nicht verfügbar)':''}`" class="w-full" @update:model-value="schema => schema && setSchema(schema)" />
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
	<svws-ui-notifications v-if="error !== null">
		<svws-ui-notification type="error">
			<template #header> {{ error.name }} </template>
			{{ error.message }}
		</svws-ui-notification>
	</svws-ui-notifications>
</template>

<script setup lang="ts">

	import { nextTick, onMounted, ref, shallowRef } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import { version } from "@version";
	import { githash } from "@githash";
	import type { LoginProps } from "./SLoginProps";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DBSchemaListeEintrag } from "@core/core/data/db/DBSchemaListeEintrag";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import SvwsUiTextInput from "@ui/ui/controls/SvwsUiTextInput.vue";

	const props = defineProps<LoginProps>();
	const benutzerState = useBenutzerState();

	const refUsername = ref<ComponentExposed<typeof SvwsUiTextInput>>();
	const firstauth = ref(true);
	const schema = shallowRef<DBSchemaListeEintrag | undefined>();
	const username = ref("");
	const password = ref("");
	const error = ref<{ name: string; message: string; } | null>(null);

	const viteProxyAdresse = ref();
	const viteServerModeDEV = ref(false);

	onMounted(() => {
		viteProxyAdresse.value = import.meta.env.VITE_SERVER_API_URL;
		viteServerModeDEV.value = import.meta.env.DEV;
		try {
			const set = new Set();
			set.difference(new Set());
		} catch {
			error.value = { name: "Achtung", message: "Ihr Browser ist veraltet. Bitte aktualisieren Sie Ihren Browser auf eine aktuelle Version. Die weitere Nutzung wird zu Fehlern im SVWS-Client führen." };
		}
	});

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);

	const connection_failed = ref(false);

	const inputDBSchemata = shallowRef<List<DBSchemaListeEintrag>>(new ArrayList());

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		error.value = null;
		try {
			inputDBSchemata.value = await props.connectTo();
			if (inputDBSchemata.value.isEmpty()) {
				throw new DeveloperNotificationException("Es sind keine Schemata vorhanden.");
			}
			schema.value = inputDBSchemata.value.get(0);
		} catch (e) {
			connection_failed.value = true;
			connecting.value = false;
			if (e instanceof DeveloperNotificationException) {
				error.value = { name: "Serverfehler", message: e.message };
			}
			return;
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
			const lastSchema = localStorage.getItem("SVWS-Client Last Used Schema");
			if ((lastSchema !== null) && (lastSchema !== '')) {
				for (const s of inputDBSchemata.value) {
					if (s.name === lastSchema) {
						schema.value = s;
						break;
					}
				}
			}
		}
		// Der Browser soll sich darum kümmern...
		// const lastUsername = localStorage.getItem(`SVWS-Client Last Used Username for Schema_${schema.value.name}`);
		// if (lastUsername !== null)
		// 	username.value = lastUsername;
		connection_failed.value = false;
		connecting.value = false;
		await nextTick(() => {
			refUsername.value?.doFocus();
		});
	}

	async function doLogin() {
		inputFocus.value = false;
		error.value = null;
		if ((schema.value === undefined) || (schema.value.name === null)) {
			error.value = { name: "Eingabefehler", message: "Es muss ein gültiges Schema ausgewählt sein." };
			return;
		}
		authenticating.value = true;
		try {
			await props.login(schema.value.name, username.value, password.value);
			firstauth.value = false;
			if (benutzerState.authenticated) {
				localStorage.setItem("SVWS-Client Last Used Schema", schema.value.name);
				// localStorage.setItem(`SVWS-Client Last Used Username for Schema_${schema.value.name}`, username.value);
			} else {
				error.value = { name: "Eingabefehler", message: "Passwort oder Benutzername falsch." };
			}
		} catch (e) {
			if (e instanceof UserNotificationException) {
				error.value = e;
			}
		}
		authenticating.value = false;
	}

</script>
