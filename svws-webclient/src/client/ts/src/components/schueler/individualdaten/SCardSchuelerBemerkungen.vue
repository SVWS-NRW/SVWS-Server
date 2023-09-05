<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="data().bemerkungen" resizeable="vertical" autoresize
			@change="bemerkungen => doPatch({ bemerkungen: bemerkungen === null ? '' : bemerkungen })" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerStammdaten } from "@core";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

</script>
