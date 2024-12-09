<template>
	<div class="page--content">
		<svws-ui-todo title="Kurs anlegen">
			Dieser Bereich ist noch in Entwicklung. Hier werden später alle initialen Informationen zur Anlage eines neuen Kurses eingepflegt.
		</svws-ui-todo>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { KurseNeuProps } from "~/components/kurse/SKurseNeuProps";

	const props = defineProps<KurseNeuProps>();

</script>
