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
				<svws-ui-text-input placeholder="altes Passwort" v-model="altesPasswort" type="text" />
				<svws-ui-text-input placeholder="neues Passwort" v-model="neuesPasswort" type="text" />
				<svws-ui-button type="secondary" @click="patchPasswort(altesPasswort, neuesPasswort)"> Passwort ändern </svws-ui-button>
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

	const altesPasswort = ref('');
	const neuesPasswort = ref('');
</script>
