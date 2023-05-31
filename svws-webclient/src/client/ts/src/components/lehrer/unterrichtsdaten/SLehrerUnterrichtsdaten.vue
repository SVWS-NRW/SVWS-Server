<template>
	<div class="page--content">
		<svws-ui-content-card title="Platzhalter" class="opacity-50">
			<svws-ui-input-wrapper :grid="2">
				<p>Hier werden zukünftig die Unterrichtsdaten angezeigt.</p>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { LehrerUnterrichtsdatenProps } from './SLehrerUnterrichtsdatenProps';

	const props = defineProps<LehrerUnterrichtsdatenProps>();

</script>
