<template>
	<svws-ui-table clickable :clicked="state.halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()" />
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@core";
	import { useGostKlausurplanungState } from "@ui";

	defineProps<{
		gotoHalbjahr: (value: GostHalbjahr) => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();

</script>
