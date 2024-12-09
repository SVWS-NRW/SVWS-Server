<template>
	<div class="page--content">
		<svws-ui-todo title="Jahrgang anlegen">
			Dieser Bereich ist noch in Entwicklung. Hier werden später alle initialen Informationen zur Anlage eines neuen Jahrgangs eingepflegt.
		</svws-ui-todo>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuleJahrgangNeuProps } from "./SSchuleJahrgangNeuProps";

	const props = defineProps<SchuleJahrgangNeuProps>();
</script>
