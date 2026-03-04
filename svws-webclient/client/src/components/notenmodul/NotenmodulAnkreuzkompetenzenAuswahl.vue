<template>
	<enm-ankreuzkompetenzen-auswahl :enm-manager :set-auswahl-einzel :auswahl-einzel :set-auswahl-mehrfach :auswahl-mehrfach />
</template>

<script setup lang="ts">

	import type { NotenmodulAnkreuzkompetenzenAuswahlProps } from './NotenmodulAnkreuzkompetenzenAuswahlProps';

	const props = defineProps<NotenmodulAnkreuzkompetenzenAuswahlProps>();

</script>