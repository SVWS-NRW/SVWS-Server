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
				<svws-ui-text-input placeholder="Erste Eingabe neues Passwort"
					v-model.trim="erstesPasswort"
					:type="passwortAnzeigen ? 'text' : 'password'"
					class="contentFocusField" />
				<svws-ui-text-input placeholder="Zweite Eingabe neues Passwort"
					v-model.trim="zweitesPasswort"
					:validation="() => validate(validatorPasswordsMatch)"
					:type="passwortAnzeigen ? 'text' : 'password'" />
				<svws-ui-checkbox v-model="passwortAnzeigen">Passwort anzeigen</svws-ui-checkbox>
				<div />
				<svws-ui-button :type="ok === null ? 'secondary': ok ? 'primary' : 'danger'" @click="password" :disabled="isChangePasswordDisabled"> Passwort ändern </svws-ui-button>
				{{ ok === true ? "Das Passwort wurde geändert, bitte melden Sie sich neu an" : ok === false ? 'Es gab einen Fehler bei der Passwortänderung' : '' }}
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="E-Mail-Benutzerdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name"
					v-model="modelProxy.proxy.name"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('name')"
					:max-len="255"
					type="text"
					class="contentFocusField" />
				<svws-ui-text-input placeholder="E-Mail-Adresse"
					v-model="modelProxy.proxy.address"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('address')"
					:max-len="100"
					type="text" />
				<svws-ui-text-input placeholder="SMTP-Username"
					v-model="modelProxy.proxy.usernameSMTP"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('usernameSMTP')"
					:max-len="255"
					type="text" />
				<svws-ui-textarea-input placeholder="Signatur"
					v-model="modelProxy.proxy.signatur"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('signatur')"
					:max-len="2047"
					resizeable="vertical" autoresize />
			</svws-ui-input-wrapper>
			<div class="mt-4 flex flex-col gap-4">
				<div class="flex items-center gap-3">
					<div class="text-headline-sm">SMTP-Passwort</div>
					<svws-ui-badge v-if="hasPasswordSMTP" type="success">
						Passwort hinterlegt
					</svws-ui-badge>
					<ui-validation-tooltip v-else-if="validationResultPasswordSMTP.hasFehler" :validation-result="validationResultPasswordSMTP" />
				</div>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Erste Eingabe neues Passwort"
						v-model="erstesSMTPPasswort"
						:max-len="127"
						:validation="() => validate(validatorSMTPPasswordLength)"
						:type="smtpPasswortAnzeigen ? 'text' : 'password'"
						autocomplete="new-password" />
					<svws-ui-text-input placeholder="Zweite Eingabe neues Passwort"
						v-model="zweitesSMTPPasswort"
						:validation="() => validate(validatorSMTPPasswordsMatch)"
						:type="smtpPasswortAnzeigen ? 'text' : 'password'"
						autocomplete="new-password" />
					<svws-ui-checkbox v-model="smtpPasswortAnzeigen">Passwort anzeigen</svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<div class="flex gap-4">
					<svws-ui-button type="secondary" @click="setPasswordSMTP" :disabled="isSetPasswordSMTPDisabled">
						{{ hasPasswordSMTP ? 'Passwort ändern' : 'Passwort setzen' }}
					</svws-ui-button>
					<svws-ui-button v-if="hasPasswordSMTP" type="danger" @click="deletePasswordSMTP" :disabled="(erstesSMTPPasswort !== '') || (zweitesSMTPPasswort !== '')">
						Passwort löschen
					</svws-ui-button>
				</div>
				<div v-if="meldungSMTP !== ''">{{ meldungSMTP }}</div>
			</div>
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

	import { computed, ref } from "vue";

	import type { BasicValidator } from "@core/asd/validate/BasicValidator";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { ValidatorPasswordsMatch } from "@ui/validation/common/ValidatorPasswordsMatch";
	import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
	import { ValidationResult } from "@ui/validation/ValidationResult";

	import { BenutzerEMailDatenModelProxy } from "./modelproxy/BenutzerEMailDatenModelProxy";
	import type { NutzereinstellungenAppProps } from "./SNutzereinstellungenAppProps";

	const props = defineProps<NutzereinstellungenAppProps>();
	const benutzerState = useBenutzerState();

	const modelProxy = new BenutzerEMailDatenModelProxy(
		() => benutzerState.benutzerEMailDaten,
		(data) => benutzerState.patchBenutzerEMailDaten(data)
	);

	const erstesPasswort = ref('');
	const zweitesPasswort = ref('');
	const passwortAnzeigen = ref(false);

	const ok = ref<boolean | null>(null);

	const okWenom = ref<boolean | null>(null);
	const okResetWenom = ref<boolean | null>(null);

	const erstesSMTPPasswort = ref('');
	const zweitesSMTPPasswort = ref('');
	const smtpPasswortAnzeigen = ref(false);
	const meldungSMTP = ref('');

	const hasPasswordSMTP = computed<boolean>(() => benutzerState.benutzerEMailDaten.passwordSMTP !== '');
	const validatorPasswordsMatch = new ValidatorPasswordsMatch(() => erstesPasswort.value, () => zweitesPasswort.value);
	const validatorSMTPPasswordsMatch = new ValidatorPasswordsMatch(() => erstesSMTPPasswort.value, () => zweitesSMTPPasswort.value);
	const validatorSMTPPasswordLength = new ValidatorStringLength(() => erstesSMTPPasswort.value, null, 127);

	const isChangePasswordDisabled = computed<boolean>(() => (erstesPasswort.value === '')
		|| (zweitesPasswort.value === '')
		|| !validatorPasswordsMatch.run());
	const isSetPasswordSMTPDisabled = computed<boolean>(() => (erstesSMTPPasswort.value === '')
		|| (zweitesSMTPPasswort.value === '')
		|| !validatorSMTPPasswordsMatch.run()
		|| !validatorSMTPPasswordLength.run());

	function validate(validator: BasicValidator): List<ValidatorFehler> {
		validator.run();
		return validator.getFehler();
	}

	const validationResultPasswordSMTP = computed(() => new ValidationResult(modelProxy.getFehler('passwordSMTP')));

	async function password() {
		ok.value = await benutzerState.patchBenutzerpasswort(erstesPasswort.value, zweitesPasswort.value);
		erstesPasswort.value = "";
		zweitesPasswort.value = "";
	}

	async function passwordResetWenom() {
		okWenom.value = null;
		okResetWenom.value = await benutzerState.passwordResetWenom();
	}

	async function setPasswordSMTP() {
		const ok = await benutzerState.patchBenutzerEMailDaten({ passwordSMTP: erstesSMTPPasswort.value });
		meldungSMTP.value = ok ? 'Das SMTP-Passwort wurde gesetzt.' : 'Es gab einen Fehler beim Setzen des SMTP-Passworts.';
		erstesSMTPPasswort.value = '';
		zweitesSMTPPasswort.value = '';
	}

	async function deletePasswordSMTP() {
		const ok = await benutzerState.patchBenutzerEMailDaten({ passwordSMTP: '' });
		meldungSMTP.value = ok ? 'Das SMTP-Passwort wurde gelöscht.' : 'Es gab einen Fehler beim Löschen des SMTP-Passworts.';
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
