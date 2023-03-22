<template>
	<div class="app-container">
		<svws-ui-content-card title="Platzhalter" class="opacity-50">
			<div class="input-wrapper">
				<p>Hier werden zukünftig die Unterrichtsdaten angezeigt.</p>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { LehrerUnterrichtsdatenProps } from './SLehrerUnterrichtsdatenProps';

	const props = defineProps<LehrerUnterrichtsdatenProps>();

</script>
