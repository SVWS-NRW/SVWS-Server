<template>
	<svws-ui-table clickable :clicked="halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]" :items="GostHalbjahr.values()" />
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@core";
	import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';

	const props = defineProps<GostKlausurplanungAuswahlProps>();

</script>