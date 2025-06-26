<template>
	<div class="page page-flex-row">
		<enm-teilleistungen-uebersicht :enm-manager :patch-leistung :patch-teilleistung :columns-visible :set-columns-visible :auswahl />
	</div>
</template>

<script setup lang="ts">

	import type { EnmTeilleistungenProps } from './EnmTeilleistungenProps';

	const props = defineProps<EnmTeilleistungenProps>();

</script>
