<template>
	<svws-ui-table clickable :clicked="halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]"
		:items="GostHalbjahr.values()" />
	<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
		<svws-ui-sub-nav type="tabs">
			<svws-ui-router-tab-bar-button v-for="c in children" :route="c" :selected="child"
				:hidden="false" @select="setChild(c)" :key="c.name" />
		</svws-ui-sub-nav>
	</Teleport>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@core";
	import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import { ref, onMounted } from 'vue';

	const props = defineProps<GostKlausurplanungAuswahlProps>();

	const cols = [
		{ key: "text", label: "Ansicht", sortable: false },
	];

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss">
.svws-ui-tabs--secondary {
  @apply flex gap-1;
}
</style>
