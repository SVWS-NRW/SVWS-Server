<template>
	<div class="page--content">
		<svws-ui-todo title="Abschlüsse und Berechtigungen an aktueller Schule">
			Hier werden zukünftig die Informationen zu den Abschlüssen und Berechtigungen an der aktuellen Schule angezeigt.
		</svws-ui-todo>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerAbschluesseProps } from './SchuelerAbschluesseProps';

	const props = defineProps<SchuelerAbschluesseProps>();

</script>
