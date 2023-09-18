<template>
	<svws-ui-content-card title="KAoA" class="opacity-50">
		<svws-ui-input-wrapper>
			<div class="opacity-50">
				Hier werden die KAoA-Daten abgelegt
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
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
