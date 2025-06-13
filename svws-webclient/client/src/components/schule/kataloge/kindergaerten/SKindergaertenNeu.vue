<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Kindergarten anlegen">
			<svws-ui-input-wrapper :grid="2">
				ToDo: Neu
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { MerkmaleNeuProps } from "~/components/schule/kataloge/merkmale/SMerkmaleNeuProps";

	const props = defineProps<MerkmaleNeuProps>();

</script>
