<template>
	<div class="page--content h-full w-full overflow-hidden">
		<enm-teilleistungen-uebersicht :manager :patch-leistung :patch-teilleistung :columns-visible :set-columns-visible />
	</div>
</template>

<script setup lang="ts">

	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';

	const props = defineProps<EnmTeilleistungenProps>();

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply inline-block;
	}

</style>