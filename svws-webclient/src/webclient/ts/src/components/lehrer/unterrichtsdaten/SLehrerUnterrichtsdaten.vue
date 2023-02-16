<template>
	<div>
		<svws-ui-content-card title="Platzhalter">
			<div class="input-wrapper">
				<p>Hier werden zukünftig die Unterrichtsdaten angezeigt.</p>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { LehrerStammdaten } from '@svws-nrw/svws-core-ts';

	const props = defineProps<{
		stammdaten: LehrerStammdaten;
	}>();

</script>
