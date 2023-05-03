<template>
	<svws-ui-content-card>
		<svws-ui-textarea-input placeholder="Mailversand" :model-value="jahrgangsdaten().textMailversand"
			@update:model-value="doPatch({ textMailversand: String($event) })" resizeable="vertical" autoresize />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostJahrgangsdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: () => GostJahrgangsdaten;
	}>();

	async function doPatch(data: Partial<GostJahrgangsdaten>) {
		return await props.patchJahrgangsdaten(data, props.jahrgangsdaten().abiturjahr);
	}

</script>
