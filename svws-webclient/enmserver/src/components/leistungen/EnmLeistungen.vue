<template>
	<div class="page--content h-full w-full overflow-hidden">
		<div class="w-full h-full overflow-hidden">
			<enm-leistungen-uebersicht :manager :patch-leistung />
		</div>
		<!-- <div class="w-full h-full overflow-hidden">
			<enm-floskeleditor />
		</div> -->
	</div>
</template>

<script setup lang="ts">

	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply inline-block;
	}

</style>