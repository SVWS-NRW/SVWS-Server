<template>
	<enm-teilleistungen :enm-manager :auswahl :patch-leistung :patch-teilleistung :columns-visible :set-columns-visible />
</template>

<script setup lang="ts">

	import type { NotenmodulTeilleistungenProps } from './NotenmodulTeilleistungenProps';

	const props = defineProps<NotenmodulTeilleistungenProps>();

</script>