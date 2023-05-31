<template>
	<div class="page--content">
		<s-card-schueler-kaoa :data="data" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';

	const props = defineProps<SchuelerKAoAProps>();
	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
