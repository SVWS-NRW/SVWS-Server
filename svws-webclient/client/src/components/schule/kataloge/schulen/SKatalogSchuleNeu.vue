<template>
	<div class="page--content">
		<svws-ui-todo title="Schule anlegen">
			Dieser Bereich ist noch in Entwicklung. Hier werden später alle initialen Informationen zur Anlage einer neuen Schulen eingepflegt.
		</svws-ui-todo>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { KatalogSchuleNeuProps } from "./SKatalogSchuleNeuProps";

	const props = defineProps<KatalogSchuleNeuProps>();
</script>
