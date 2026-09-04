<template>
	<header class="svws-ui-header">
		<div class="svws-ui-header--title">
			<div class="svws-headline-wrapper">
				<h2 class="svws-headline">
					Nutzereinstellungen
				</h2>
				<span class="svws-subline inline-flex gap-x-3 gap-y-1 items-center">
					<span class="mt-1">{{ benutzerState.benutzerdaten.anzeigename }}</span>
					<svws-ui-badge type="light" title="ID" class="font-mono m-0" size="small">
						ID: {{ benutzerState.benutzerdaten.id }}
					</svws-ui-badge>
				</span>
			</div>
		</div>
		<div class="svws-ui-header--actions" />
	</header>

	<div class="page page-grid-cards">
		<svws-ui-content-card title="Passwort ändern">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Erste Eingabe neues Passwort" v-model.trim="erstesPasswort" :type="passwortAnzeigen ? 'text' : 'password'" />
				<svws-ui-text-input placeholder="Zweite Eingabe neues Passwort" v-model.trim="zweitesPasswort" type="password" />
				<svws-ui-checkbox v-model="passwortAnzeigen">Passwort anzeigen</svws-ui-checkbox>
				<div />
				<svws-ui-button :type="ok === null ? 'secondary': ok === true ? 'primary' : 'danger'" @click="password" :disabled="erstesPasswort !== zweitesPasswort"> Passwort ändern </svws-ui-button>
				{{ ok === true ? "Das Passwort wurde geändert, bitte melden Sie sich neu an" : ok === false ? 'Es gab einen Fehler bei der Passwortänderung' : '' }}
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="E-Mail-Benutzerdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Name" :model-value="benutzerState.benutzerEMailDaten.name" @change="name => benutzerState.patchBenutzerEMailDaten({name: name ?? undefined})" type="text" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :model-value="benutzerState.benutzerEMailDaten.address" @change="address => benutzerState.patchBenutzerEMailDaten({address: address ?? undefined})" type="text" />
				<svws-ui-text-input placeholder="SMTP-Username" :model-value="benutzerState.benutzerEMailDaten.usernameSMTP" @change="usernameSMTP => benutzerState.patchBenutzerEMailDaten({usernameSMTP: usernameSMTP ?? undefined})" type="text" />
				<svws-ui-text-input placeholder="SMTP-Passwort" v-model.trim="smtpPassword" type="password" />
				<svws-ui-textarea-input placeholder="Signatur"	:model-value="benutzerState.benutzerEMailDaten.signatur" @change="signatur => benutzerState.patchBenutzerEMailDaten({ signatur: signatur ?? '' })" resizeable="vertical" autoresize />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card v-if="benutzerState.benutzertyp === BenutzerTyp.LEHRER" title="Webnotenmanager-Passwort ändern">
			<div v-if="benutzerState.wenomInitialkennwort !== null" class="min-w-fit max-w-fit bg-ui-5 p-4 border-2 border-dashed border-ui-100 rounded-lg text-2xl font-mono text-center tracking-widest mb-4 select-all flex">
				<!-- {{ wenomInitialkennwort().match(/.{1,4}/g)?.join(' - ') }} -->
				{{ benutzerState.wenomInitialkennwort }}
			</div>
			<div class="flex gap-4">
				<svws-ui-button type="primary" @click="benutzerState.getWenomInitialkennwort()"> Initialpasswort anzeigen </svws-ui-button>
				<svws-ui-button :type="okResetWenom === null ? 'secondary': ok === true ? 'primary' : 'danger'" @click="passwordResetWenom"> Auf Initialpasswort zurücksetzen </svws-ui-button>
			</div>
			{{ okResetWenom === true ? "Das Passwort wurde zurückgesetzt" : okResetWenom === false ? 'Es gab einen Fehler beim Zurücksetzen des Passworts' : '' }}
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
				<ui-color-mode mode="radio" auto />
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { NutzereinstellungenAppProps } from "./SNutzereinstellungenAppProps";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";

	const props = defineProps<NutzereinstellungenAppProps>();
	const benutzerState = useBenutzerState();

	const erstesPasswort = ref('');
	const zweitesPasswort = ref('');
	const passwortAnzeigen = ref(false);

	const ok = ref<boolean | null>(null);

	const okWenom = ref<boolean | null>(null);
	const okResetWenom = ref<boolean | null>(null);

	const _smtpPassword = ref<string>('');

	const smtpPassword = computed<string>({
		get: () => _smtpPassword.value,
		set: (passwordSMTP) => void benutzerState.patchBenutzerEMailDaten({ passwordSMTP }),
	});

	watch(() => benutzerState.benutzerEMailDaten, async () => await decryptSMTPPassword());

	async function password() {
		ok.value = await benutzerState.patchBenutzerpasswort(erstesPasswort.value, zweitesPasswort.value);
		erstesPasswort.value = "";
		zweitesPasswort.value = "";
	}

	async function passwordResetWenom() {
		okWenom.value = null;
		okResetWenom.value = await benutzerState.passwordResetWenom();
	}

	async function decryptSMTPPassword() {
		try {
			const password = benutzerState.benutzerEMailDaten.passwordSMTP;
			if (password === "") {
				_smtpPassword.value = "";
				return;
			}
			const encoded = await benutzerState.aes.decryptBase64(password);
			_smtpPassword.value = new TextDecoder().decode(encoded);
		} catch {
			_smtpPassword.value = "";
		}
	}

	const fontSize = ref<string>('default');
	const updateFontSize = (size: string | null) => {
		if (size === null) {
			return;
		}
		document.documentElement.classList.remove('font-size-small', 'font-size-large');
		if (size !== 'default') {
			document.documentElement.classList.add(`font-size-${size}`);
		}
		localStorage.setItem('fontSize', size);
		fontSize.value = size;
	};

	updateFontSize(localStorage.getItem('fontSize'));

</script>
