<template>
	<svws-ui-content-card title="Fehlzeiten" class="opacity-50">
		<span>Hier wird demnächst die Übersicht über die Fehlzeiten implementiert.</span>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittManager, SchuelerLernabschnittsdaten } from "@core";

	const props = defineProps<{
		manager: () => SchuelerLernabschnittManager;
		patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	}>();

</script>
