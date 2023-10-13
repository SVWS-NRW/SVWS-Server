<template>
	<svws-ui-table clickable :clicked="halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]"
		:items="GostHalbjahr.values()" />
	<Teleport to=".svws-sub-nav-target" v-if="isMounted">
		<svws-ui-sub-nav>
			<nav class="svws-ui-title-tabs" v-if="children.length > 1">
				<svws-ui-router-tab-bar-button v-for="(c, index) in children" :route="c" :selected="child"
					:hidden="false" @select="setChild(c)" :key="index" />
			</nav>
		</svws-ui-sub-nav>
	</Teleport>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@core";
	import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import { ref, onMounted } from 'vue';

	const props = defineProps<GostKlausurplanungAuswahlProps>();

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
