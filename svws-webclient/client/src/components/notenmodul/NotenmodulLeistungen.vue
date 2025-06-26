<template>
	<enm-leistungen :enm-manager :auswahl :patch-leistung :columns-visible :set-columns-visible :floskel-editor-visible :set-floskel-editor-visible />
</template>

<script setup lang="ts">

	import type { NotenmodulLeistungenProps } from './NotenmodulLeistungenProps';

	const props = defineProps<NotenmodulLeistungenProps>();

</script>