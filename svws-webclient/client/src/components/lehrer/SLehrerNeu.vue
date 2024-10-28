<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20" />
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import type { LehrerNeuProps } from "~/components/lehrer/SLehrerNeuProps";

	const props = defineProps<LehrerNeuProps>();

</script>
