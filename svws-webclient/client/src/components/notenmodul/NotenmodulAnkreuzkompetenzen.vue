<template>
	<enm-ankreuzkompetenzen :enm-manager :auswahl :patch-leistung :patch-ankreuzkompetenz :patch-bemerkungen />
</template>

<script setup lang="ts">

	import type { NotenmodulAnkreuzkompetenzenProps } from './NotenmodulAnkreuzkompetenzenProps';

	const props = defineProps<NotenmodulAnkreuzkompetenzenProps>();

</script>