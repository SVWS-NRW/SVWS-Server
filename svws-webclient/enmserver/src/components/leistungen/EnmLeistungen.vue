<template>
	<div class="page--content h-full w-full overflow-hidden">
		<enm-leistungen-uebersicht :manager :patch-leistung />
		<enm-floskeleditor />
	</div>
</template>

<script setup lang="ts">

	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid;
		grid-template-columns: 2fr 1fr;
	}

</style>