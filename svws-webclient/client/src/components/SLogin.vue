<template>
	<ui-login-layout :version :githash>
		<template #logo>
			<img src="/images/Wappenzeichen_NRW_bw.svg" alt="Logo NRW" class="h-14">
		</template>
		<template #main>
			<div class="grid grow grid-cols-1 gap-3 justify-items-center py-0.5">
				<svws-ui-text-input v-model.trim="inputHostname" type="text" url placeholder="Serveraddresse" @keyup.enter="connect" @focus="inputFocus = true" :debounce-ms="0" />
				<svws-ui-button type="secondary" @click="connect" :disabled="!(inputDBSchemata.size() === 0 || connecting || inputFocus )" :class="{'opacity-25 hover:opacity-100': inputDBSchemata.size() > 0 && !inputFocus}">
					<span v-if="inputDBSchemata.size() === 0 || connecting || inputFocus">Verbinden</span>
					<span v-else>Verbunden</span>
					<svws-ui-spinner :spinning="connecting" />
					<span class="icon i-ri-check-line" v-if="!connecting && inputDBSchemata.size() > 0 && !inputFocus" />
				</svws-ui-button>
			</div>
			<Transition>
				<svws-ui-input-wrapper v-if="inputDBSchemata.size() > 0 && !connecting" class="mt-10" center>
					<svws-ui-select v-model="schema" title="Datenbank-Schema" :items="inputDBSchemata" :item-text="i => i.name ?? 'SCHEMANAME FEHLT'" class="w-full" @update:model-value="schema => schema && setSchema(schema)" />
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
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { LoginProps } from "./SLoginProps";
	import type { DBSchemaListeEintrag, List } from "@core";
	import { ArrayList, DeveloperNotificationException, JsonCoreTypeReader } from "@core";
	import { SvwsUiTextInput } from "@ui";
	import { version } from '../../version';
	import { githash } from '../../githash';

	const props = defineProps<LoginProps>();

	const refUsername = ref<ComponentExposed<typeof SvwsUiTextInput>>();
	const firstauth = ref(true);
	const schema = shallowRef<DBSchemaListeEintrag | undefined>();
	const username = ref("");
	const password = ref("");
	const error = ref<{name: string; message: string;}|null>(null);

	onMounted(() => {
		try {
			const set = new Set();
			set.difference(new Set());
		} catch (e) {
			error.value = {name: "Achtung", message: "Ihr Browser ist veraltet. Bitte aktualisieren Sie Ihren Browser auf eine aktuelle Version. Die weitere Nutzung wird zu Fehlern im SVWS-Client führen."};
		}
	})

	const connecting = ref(false);
	const authenticating = ref(false);
	const inputFocus = ref(false);

	const connection_failed = ref(false);

	const inputDBSchemata = shallowRef<List<DBSchemaListeEintrag>>(new ArrayList());

	const inputHostname = computed<string>({
		get: () => props.hostname,
		set: (value) => props.setHostname(value),
	});

	// Versuche zu beim Laden der Komponente automatisch mit Default-Einstellungen eine Verbindung zu dem Server aufzubauen
	void connect();

	async function initCoreTypes() {
		const reader = new JsonCoreTypeReader(`https://${props.hostname}`);
		await reader.loadAll();
		reader.readAll();
		props.setMapCoreTypeNameJsonData(reader.mapCoreTypeNameJsonData);
	}

	async function connect() {
		connecting.value = true;
		inputFocus.value = false;
		error.value = null;
		try {
			inputDBSchemata.value = await props.connectTo(props.hostname);
			if (inputDBSchemata.value.isEmpty())
				throw new DeveloperNotificationException("Es sind keine Schemata vorhanden.");
			schema.value = inputDBSchemata.value.get(0);
			await initCoreTypes();
		} catch (e) {
			connection_failed.value = true;
			connecting.value = false;
			const message = e instanceof DeveloperNotificationException ? e.message : "Verbindung zum Server fehlgeschlagen. Bitte die Serveradresse prüfen und erneut versuchen.";
			error.value = {name: "Serverfehler", message};
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
			if ((lastSchema !== null) && (lastSchema !== ''))
				for (const s of inputDBSchemata.value)
					if (s.name === lastSchema) {
						schema.value = s;
						break;
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
		if ((schema.value === undefined) || (schema.value.name === null))
			return error.value = {name: "Eingabefehler", message: "Es muss ein gültiges Schema ausgewählt sein."};
		authenticating.value = true;
		await props.login(schema.value.name, username.value, password.value);
		authenticating.value = false;
		firstauth.value = false;
		if (!props.authenticated)
			error.value = {name: "Eingabefehler", message: "Passwort oder Benutzername falsch."};
		else {
			localStorage.setItem("SVWS-Client Last Used Schema", schema.value.name);
			// localStorage.setItem(`SVWS-Client Last Used Username for Schema_${schema.value.name}`, username.value);
		}
	}

</script>
