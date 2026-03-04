<template>
	<enm-ankreuzkompetenzen :enm-manager :auswahl :patch-leistung :patch-ankreuzkompetenz />
</template>

<script setup lang="ts">

	import type { NotenmodulAnkreuzkompetenzenProps } from './NotenmodulAnkreuzkompetenzenProps';

	const props = defineProps<NotenmodulAnkreuzkompetenzenProps>();

</script>