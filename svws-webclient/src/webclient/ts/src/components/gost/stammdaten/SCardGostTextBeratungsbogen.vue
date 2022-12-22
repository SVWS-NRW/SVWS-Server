<template>
	<svws-ui-content-card title="Laufbahnplanung: Text für Beratungsbögen">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Text für Beratungsbögen" v-model="inputTextBeratungsbogen" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { GostJahrgangsdaten } from '@svws-nrw/svws-core-ts';
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';

	const props = defineProps<{ jahrgangsdaten: DataGostJahrgang }>();

	const daten: ComputedRef<GostJahrgangsdaten> = computed(() => props.jahrgangsdaten.daten || new GostJahrgangsdaten());

	const inputTextBeratungsbogen: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.textBeratungsbogen?.toString();
		},
		set(val) {
			props.jahrgangsdaten.patch({ textBeratungsbogen: val });
		}
	});

</script>
