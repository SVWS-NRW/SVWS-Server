<template>
	<svws-ui-content-card>
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Mailversand" v-model="inputTextMailversand" resizeable="vertical" />
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

	const inputTextMailversand: WritableComputedRef<string | undefined> = computed({
		get:() => daten.value.textMailversand ?? undefined,
		set: (value) => void props.jahrgangsdaten.patch({ textMailversand: value })
	});

</script>
