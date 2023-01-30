<template>
	<svws-ui-content-card title="Texte für die Laufbahnplanung">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Beratungsbögen" v-model="inputTextBeratungsbogen" resizeable="vertical" />
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
		get: () => daten.value.textBeratungsbogen ?? undefined,
		set: (value) => void props.jahrgangsdaten.patch({ textBeratungsbogen: value })
	});

</script>
