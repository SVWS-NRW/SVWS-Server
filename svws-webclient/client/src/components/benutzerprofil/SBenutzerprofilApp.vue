<template>
	<svws-ui-header>
		<div>
			<span class="inline-block mr-3">{{ benutzer().anzeigename }}</span>
			<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
				ID: {{ benutzer().id }}
			</svws-ui-badge>
		</div>
	</svws-ui-header>
	<div class="page--content">
		<svws-ui-content-card title="Passwort ändern">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Erste Eingabe neues Passwort" v-model.trim="erstesPasswort" type="password" />
				<svws-ui-text-input placeholder="Zweite Eingabe neues Passwort" v-model.trim="zweitesPasswort" type="password" />
				<svws-ui-button :type="ok === null ? 'secondary': ok === true ? 'primary' : 'danger'" @click="password" :disabled="erstesPasswort !== zweitesPasswort"> Passwort ändern </svws-ui-button>
				{{ ok === true ? "Das Passwort wurde geändert, bitte melden Sie sich neu an" : ok === false ? 'Es gab einen Fehler bei der Passwortänderung' : '' }}
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Benutzereinstellungen">
			<svws-ui-checkbox type="toggle" v-model="toggleBackticks">Fehlermeldungen mit Backticks kopieren</svws-ui-checkbox>
		</svws-ui-content-card>
		<svws-ui-content-card title="EMail-Benutzerdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" :model-value="benutzerEMailDaten().name" @change="name => patchBenutzerEMailDaten({name})" type="text" />
				<svws-ui-text-input placeholder="EMail-Adresse" :model-value="benutzerEMailDaten().address" @change="address => patchBenutzerEMailDaten({address})" type="text" />
				<svws-ui-text-input placeholder="SMTP-Username" :model-value="benutzerEMailDaten().usernameSMTP" @change="usernameSMTP => patchBenutzerEMailDaten({usernameSMTP})" type="text" />
				<svws-ui-text-input placeholder="SMTP-Passwort" v-model="smtpPassword" type="text" />
				<svws-ui-textarea-input placeholder="Signatur"	:model-value="benutzerEMailDaten().signatur"
					@change="signatur => patchBenutzerEMailDaten({ signatur: signatur ?? '' })" resizeable="vertical" autoresize /> :{{ smtpPassword }}
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { BenutzerprofilAppProps } from "./SBenutzerprofilAppProps";

	const props = defineProps<BenutzerprofilAppProps>();

	const erstesPasswort = ref('');
	const zweitesPasswort = ref('');
	const ok = ref<boolean | null>(null);

	const _smtpPassword = ref<string>('');

	const smtpPassword = computed<string>({
		get: () => _smtpPassword.value,
		set: (passwordSMTP) => void props.patchBenutzerEMailDaten({passwordSMTP})
	})

	watch(()=>props.benutzerEMailDaten(), async () => await decryptSMTPPassword())

	async function password() {
		ok.value = await props.patchPasswort(erstesPasswort.value, zweitesPasswort.value);
	}

	const toggleBackticks = computed<boolean>({
		get: () => props.backticks(),
		set: (value) => props.setBackticks(value)
	});

	async function decryptSMTPPassword() {
		const password = props.benutzerEMailDaten().passwordSMTP;
		const encoded = await props.aes.decryptBase64(password);
		_smtpPassword.value = new TextDecoder().decode(encoded);
	}

</script>
