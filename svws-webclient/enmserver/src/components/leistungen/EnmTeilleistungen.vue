<template>
	<div class="page page-flex-row">
		<enm-teilleistungen-uebersicht :manager :patch-leistung :patch-teilleistung :columns-visible :set-columns-visible :auswahlmanager />
	</div>
</template>

<script setup lang="ts">

	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';

	const props = defineProps<EnmTeilleistungenProps>();


</script>
