<template>
	<div class="page--content">
		<div class="enm-leistung-uebersicht-bereich">
			<enm-leistungen-uebersicht :manager :patch-leistung :columns-visible :set-columns-visible />
		</div>
		<div class="enm-floskel-editor-bereich">
			<enm-floskeleditor />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { EnmLeistungenProps } from './EnmLeistungenProps';

	const props = defineProps<EnmLeistungenProps>();

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply flex flex-row gap-2;
		@apply h-full w-full overflow-hidden;
		@apply max-w-full;
	}

	.enm-leistung-uebersicht-bereich {
		@apply w-full h-full overflow-hidden;
		@apply flex-grow;
		@apply max-w-[100rem];
	}

	.enm-floskel-editor-bereich {
		@apply w-full h-full overflow-hidden;
		@apply max-w-196;
	}

</style>