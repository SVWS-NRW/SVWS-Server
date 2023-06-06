<template>
	<svws-ui-content-card title="Texte für die Laufbahnplanung">
		<svws-ui-textarea-input placeholder="Beratungsbögen" :model-value="jahrgangsdaten().textBeratungsbogen"
			@update:model-value="doPatch({ textBeratungsbogen: String($event) })" resizeable="vertical" autoresize />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostJahrgangsdaten } from "@core";

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: () => GostJahrgangsdaten;
	}>();

	async function doPatch(data: Partial<GostJahrgangsdaten>) {
		return await props.patchJahrgangsdaten(data, props.jahrgangsdaten().abiturjahr);
	}

</script>
