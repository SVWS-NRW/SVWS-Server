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
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerprofilDatenProps } from './SBenutzerProfilDatenProps';
	import { ref } from 'vue';

	const props = defineProps<BenutzerprofilDatenProps>();

	const altesPasswort = ref('');
	const neuesPasswort = ref('');

</script>
