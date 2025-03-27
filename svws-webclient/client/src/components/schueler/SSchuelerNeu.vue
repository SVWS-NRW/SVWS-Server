<template>
	<div class="page page-grid-cards">
		<svws-ui-todo title="Schüler anlegen">
			Dieser Bereich ist noch in Entwicklung. Hier werden später alle initialen Informationen zur Anlage eines neuen Schülers eingepflegt.
		</svws-ui-todo>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";

	const props = defineProps<SchuelerNeuProps>();

</script>
