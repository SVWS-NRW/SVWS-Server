<template>
	<div class="page--content">
		<s-card-lehrer-personaldaten-allgemein :personaldaten="personaldaten" @patch="patch" />
		<s-card-lehrer-personaldaten-lehraemter :personaldaten="personaldaten" @patch="patch" />
		<s-card-lehrer-personaldaten-beschaeftigung :personaldaten="personaldaten" @patch="patch" />
		<s-card-lehrer-personaldaten-anrechnung :personaldaten="personaldaten" @patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';

	const props = defineProps<LehrerPersonaldatenProps>();
</script>
