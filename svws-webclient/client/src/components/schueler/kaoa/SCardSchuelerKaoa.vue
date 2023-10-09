<template>
	<svws-ui-todo title="KAoA">
		Dieser Bereich ist noch in Entwicklung. Hier werden die KAoA-Daten abgelegt.
	</svws-ui-todo>
</template>

<script setup lang="ts">

	import type { SchuelerKAoADaten } from "@core";

	defineProps<{
		data: SchuelerKAoADaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerKAoADaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerKAoADaten>) {
		emit('patch', data);
	}

</script>
