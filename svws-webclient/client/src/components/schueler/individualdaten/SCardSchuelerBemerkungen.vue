<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="data.bemerkungen" resizeable="vertical" autoresize
			@change="bemerkungen => patch({ bemerkungen: bemerkungen === null ? '' : bemerkungen })" />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuelerListeManager, SchuelerStammdaten } from "@core";

	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

</script>
