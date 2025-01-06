<template>
	<div class="page--content h-full w-full overflow-hidden">
		<enm-leistungen-uebersicht :manager :patch-leistung />
	</div>
</template>

<script setup lang="ts">

	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

</script>

<style lang="postcss" scoped>

	.page--content {
		display: inline-block;
	}

</style>