<template>
	<svws-ui-content-card title="Grunddaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<!-- <svws-ui-text-input placeholder="Nachname" :model-value="data().nachname"
					@update:model-value="doPatch({ nachname: String($event) })" type="text" /> -->
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { Geschlecht } from "@svws-nrw/svws-core";

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
