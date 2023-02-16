<template>
	<div class="app-container">
		<s-card-lehrer-personaldaten-allgemein :personaldaten="personaldaten" @patch="doPatch" />
		<s-card-lehrer-personaldaten-lehraemter :personaldaten="personaldaten" @patch="doPatch" />
		<s-card-lehrer-personaldaten-beschaeftigung :personaldaten="personaldaten" @patch="doPatch" />
		<s-card-lehrer-personaldaten-anrechnung :personaldaten="personaldaten" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">
	import { LehrerPersonaldaten } from '@svws-nrw/svws-core-ts';
	import { computed } from 'vue';
	import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';

	const props = defineProps<{
		patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
		personaldaten: LehrerPersonaldaten;
	}>();

	const { doPatch } = useDebouncedPatch(computed(() => props.personaldaten), props.patch)
</script>
