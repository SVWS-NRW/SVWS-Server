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
		<svws-ui-content-card title="Benutzereinstellungen" class="opacity-50">
			Hier können die Benutzereinstellungen für den Client angepasst werden, sobald diese implementiert wurden
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerprofilConfigProps } from './SBenutzerProfilConfigProps';
	const props = defineProps<BenutzerprofilConfigProps>();

</script>
