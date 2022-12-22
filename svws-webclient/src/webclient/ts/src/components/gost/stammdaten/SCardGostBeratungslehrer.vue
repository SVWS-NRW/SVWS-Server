<template>
	<svws-ui-content-card :title="'Beratungslehrer'">
		<div class="content-wrapper">
			<div class="input-wrapper">Hier kommen die Beratungslehrer hin</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostJahrgangsdaten } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef } from 'vue';
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';

	const props = defineProps<{ jahrgangsdaten: DataGostJahrgang }>();

	const daten: ComputedRef<GostJahrgangsdaten> = computed(() => props.jahrgangsdaten.daten || new GostJahrgangsdaten());

</script>
