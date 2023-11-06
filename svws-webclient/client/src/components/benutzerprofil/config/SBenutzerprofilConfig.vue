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
</template>

<script setup lang="ts">

	import type { BenutzerprofilConfigProps } from './SBenutzerProfilConfigProps';
	const props = defineProps<BenutzerprofilConfigProps>();

</script>
