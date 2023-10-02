<template>
	<svws-ui-content-card title="Abschluss Berechnung">
		<svws-ui-input-wrapper>
			<svws-ui-textarea-input placeholder="Text" :model-value="manager().lernabschnittGet().textErgebnisPruefungsalgorithmus"
				@change="textErgebnisPruefungsalgorithmus => patch({ textErgebnisPruefungsalgorithmus })"
				resizeable="vertical" :autoresize="true" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittManager, SchuelerLernabschnittsdaten } from "@core";

	const props = defineProps<{
		manager: () => SchuelerLernabschnittManager;
		patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
	}>();

</script>
