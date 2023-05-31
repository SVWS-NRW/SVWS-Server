<template>
	<div class="page--content">
		<s-card-lehrer-personaldaten-allgemein :personaldaten="personaldaten" @patch="doPatch" />
		<s-card-lehrer-personaldaten-lehraemter :personaldaten="personaldaten" @patch="doPatch" />
		<s-card-lehrer-personaldaten-beschaeftigung :personaldaten="personaldaten" @patch="doPatch" />
		<s-card-lehrer-personaldaten-anrechnung :personaldaten="personaldaten" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';
	import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';
	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';

	const props = defineProps<LehrerPersonaldatenProps>();

	const { doPatch } = useDebouncedPatch(computed(() => props.personaldaten), props.patch)
</script>
