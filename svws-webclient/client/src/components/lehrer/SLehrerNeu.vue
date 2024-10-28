<template>
	<div class="page--content">
		<svws-ui-todo title="Lehrer anlegen">
			Dieser Bereich ist noch in Entwicklung. Hier werden später alle initialen Informationen zur Anlage eines neuen Lehrer eingepflegt.
		</svws-ui-todo>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { LehrerNeuProps } from "~/components/lehrer/SLehrerNeuProps";

	const props = defineProps<LehrerNeuProps>();

</script>
