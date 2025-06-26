<template>
	<enm-klassenleitung :enm-manager :auswahl :patch-bemerkungen :patch-lernabschnitt :columns-visible :set-columns-visible :floskel-editor-visible :set-floskel-editor-visible />
</template>

<script setup lang="ts">

	import type { NotenmodulKlassenleitungProps } from './NotenmodulKlassenleitungProps';

	const props = defineProps<NotenmodulKlassenleitungProps>();

</script>