<template>
	<svws-ui-header>
		<div>
			<span class="inline-block mr-3">{{ benutzer.anzeigename }}</span>
			<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
				ID:
				{{ benutzer.id }}
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
		<svws-ui-content-card title="Benutzereinstellungen" class="opacity-50">
			Hier können die Benutzereinstellungen für den Client angepasst werden, sobald diese implementiert wurden
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { BenutzerprofilAppProps } from "./SBenutzerprofilAppProps";

	const props = defineProps<BenutzerprofilAppProps>();

	const erstesPasswort = ref('');
	const zweitesPasswort = ref('');
	const ok = ref<boolean | null>(null);

	async function password() {
		ok.value = await props.patchPasswort(erstesPasswort.value, zweitesPasswort.value);
	}
</script>
