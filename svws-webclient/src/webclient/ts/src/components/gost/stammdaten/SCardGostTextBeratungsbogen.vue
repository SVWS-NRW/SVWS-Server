<template>
	<svws-ui-content-card title="Texte für die Laufbahnplanung">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Beratungsbögen" :model-value="props.jahrgangsdaten.textBeratungsbogen"
					@update:model-value="doPatch({ textBeratungsbogen: String($event) })" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostJahrgangsdaten } from '@svws-nrw/svws-core-ts';

	const props = defineProps<{
		patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => Promise<boolean>;
		jahrgangsdaten: GostJahrgangsdaten;
	}>();

	async function doPatch(data: Partial<GostJahrgangsdaten>) {
		return await props.patchJahrgangsdaten(data, props.jahrgangsdaten.abiturjahr);
	}

</script>
