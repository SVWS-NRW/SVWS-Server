<template>
	<enm-klassenleitung-auswahl :enm-manager :set-auswahl-einzel :auswahl-einzel :set-auswahl-mehrfach :auswahl-mehrfach />
</template>

<script setup lang="ts">

	import type { NotenmodulKlassenleitungAuswahlProps } from './NotenmodulKlassenleitungAuswahlProps';

	const props = defineProps<NotenmodulKlassenleitungAuswahlProps>();

</script>