<template>
	<svws-ui-table clickable :clicked="state.halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()" />
</template>

<script setup lang="ts">
	import { GostHalbjahr } from '@core/core/types/gost/GostHalbjahr';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';


	defineProps<{
		gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();

</script>
