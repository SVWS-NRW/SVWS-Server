<template>
	<svws-ui-content-card title="KAoA" class="opacity-50">
		<div class="input-wrapper">
			<div class="col-span-2">
				Hier werden die KAoA-Daten abgelegt
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerKAoADaten } from "@svws-nrw/svws-core";

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
