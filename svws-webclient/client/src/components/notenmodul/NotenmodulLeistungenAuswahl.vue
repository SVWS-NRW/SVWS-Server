<template>
	<enm-leistungen-auswahl :enm-manager :set-auswahl-einzel :auswahl-einzel :set-auswahl-mehrfach :auswahl-mehrfach />
</template>

<script setup lang="ts">

	import type { NotenmodulLeistungenAuswahlProps } from './NotenmodulLeistungenAuswahlProps';

	const props = defineProps<NotenmodulLeistungenAuswahlProps>();

</script>