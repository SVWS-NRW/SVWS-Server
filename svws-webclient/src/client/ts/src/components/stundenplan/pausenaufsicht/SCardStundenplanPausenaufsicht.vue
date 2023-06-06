<template>
	<svws-ui-content-card title="Pausenaufsicht">
		<svws-ui-input-wrapper :grid="2">
			<!-- <svws-ui-text-input placeholder="Nachname" :model-value="data().nachname"
					@update:model-value="doPatch({ nachname: String($event) })" type="text" /> -->
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { Geschlecht } from "@core";

	// const props = defineProps<{
	// 	data: () => SchuelerStammdaten;
	// }>();

	// const emit = defineEmits<{
	// 	(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	// }>()

	// function doPatch(data: Partial<SchuelerStammdaten>) {
	// 	emit('patch', data);
	// }

</script>
