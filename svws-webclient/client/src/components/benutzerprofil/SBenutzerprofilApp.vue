<template>
	<svws-ui-header>
		<div>
			<span class="inline-block mr-3">{{ benutzer().anzeigename }}</span>
			<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
				ID: {{ benutzer().id }}
			</svws-ui-badge>
		</div>
	</svws-ui-header>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Passwort ändern">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Erste Eingabe neues Passwort" v-model.trim="erstesPasswort" type="password" />
				<svws-ui-text-input placeholder="Zweite Eingabe neues Passwort" v-model.trim="zweitesPasswort" type="password" />
				<svws-ui-button :type="ok === null ? 'secondary': ok === true ? 'primary' : 'danger'" @click="password" :disabled="erstesPasswort !== zweitesPasswort"> Passwort ändern </svws-ui-button>
				{{ ok === true ? "Das Passwort wurde geändert, bitte melden Sie sich neu an" : ok === false ? 'Es gab einen Fehler bei der Passwortänderung' : '' }}
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="E-Mail-Benutzerdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Name" :model-value="benutzerEMailDaten().name" @change="name => patchBenutzerEMailDaten({name: name ?? undefined})" type="text" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="benutzerEMailDaten().address" @change="address => patchBenutzerEMailDaten({address: address ?? undefined})" type="text" />
				<svws-ui-text-input placeholder="SMTP-Username" :model-value="benutzerEMailDaten().usernameSMTP" @change="usernameSMTP => patchBenutzerEMailDaten({usernameSMTP: usernameSMTP ?? undefined})" type="text" />
				<svws-ui-text-input placeholder="SMTP-Passwort" v-model.trim="smtpPassword" type="password" />
				<svws-ui-textarea-input placeholder="Signatur"	:model-value="benutzerEMailDaten().signatur" @change="signatur => patchBenutzerEMailDaten({ signatur: signatur ?? '' })" resizeable="vertical" autoresize />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Ansicht">
			<div class="flex flex-col gap-5">
				<div class="flex flex-col gap-2 text-left">
					<span class="font-bold">Skalierung</span>
					<svws-ui-radio-group :row="true">
						<svws-ui-radio-option value="small" v-model="fontSize" name="fontSize" label="Kleiner" @click="updateFontSize('small')" />
						<svws-ui-radio-option value="default" v-model="fontSize" name="fontSize" label="Normal" @click="updateFontSize('default')" />
						<svws-ui-radio-option value="large" v-model="fontSize" name="fontSize" label="Größer" @click="updateFontSize('large')" />
					</svws-ui-radio-group>
				</div>
				<div v-if="mode !== ServerMode.STABLE" class="flex flex-col gap-2 text-left">
					<span class="font-bold">Theme</span>
					<svws-ui-radio-group :row="true">
						<svws-ui-radio-option value="light" v-model="themeRef" name="theme" label="Light" @click="updateTheme('light')" />
						<svws-ui-radio-option value="dark" v-model="themeRef" name="theme" label="Dark (In Entwicklung)" @click="updateTheme('dark')" />
					</svws-ui-radio-group>
					<div v-if="themeRef === 'dark'" class="mt-2 text-ui-25">
						Achtung! Das Dark-Theme befindet sich gerade noch in der Entwicklung und ist noch nicht
						vollständig umgesetzt.
						<span class="font-bold text-ui-0">Es kann an einigen Stellen zu Darstellungsproblemen führen.</span>span
					</div>
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { BenutzerprofilAppProps } from "./SBenutzerprofilAppProps";
	import { ServerMode } from "@core";

	const props = defineProps<BenutzerprofilAppProps>();

	const erstesPasswort = ref('');
	const zweitesPasswort = ref('');
	const ok = ref<boolean | null>(null);

	const _smtpPassword = ref<string>('');

	const smtpPassword = computed<string>({
		get: () => _smtpPassword.value,
		set: (passwordSMTP) => void props.patchBenutzerEMailDaten({passwordSMTP}),
	})

	watch(() => props.benutzerEMailDaten(), async () => await decryptSMTPPassword());

	async function password() {
		ok.value = await props.patchPasswort(erstesPasswort.value, zweitesPasswort.value);
	}

	async function decryptSMTPPassword() {
		try {
			const password = props.benutzerEMailDaten().passwordSMTP;
			if (password === "") {
				_smtpPassword.value = "";
				return;
			}
			const encoded = await props.aes.decryptBase64(password);
			_smtpPassword.value = new TextDecoder().decode(encoded);
		} catch(e) {
			_smtpPassword.value = "";
		}
	}

	const themeRef = ref<string>('light');
	const fontSize = ref<string>('default');
	const updateFontSize = (size: string | null) => {
		if (size === null)
			return;
		document.documentElement.classList.remove('font-size-small', 'font-size-large');
		if (size !== 'default')
			document.documentElement.classList.add(`font-size-${size}`);
		localStorage.setItem('fontSize', size);
		fontSize.value = size;
	};

	const updateTheme = (theme: string | null) => {
		if (theme === null)
			return;
		document.documentElement.classList.remove('light', 'dark');
		if (theme !== 'auto')
			document.documentElement.classList.add(theme);
		localStorage.setItem('theme', theme);
		themeRef.value = theme;
	};

	updateTheme(localStorage.getItem('theme'));
	updateFontSize(localStorage.getItem('fontSize'));

</script>
