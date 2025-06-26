<template>
	<enm-teilleistungen-auswahl :enm-manager :set-auswahl-einzel :auswahl-einzel :set-auswahl-mehrfach :auswahl-mehrfach />
</template>

<script setup lang="ts">

	import type { NotenmodulTeilleistungenAuswahlProps } from './NotenmodulTeilleistungenAuswahlProps';

	const props = defineProps<NotenmodulTeilleistungenAuswahlProps>();

</script>