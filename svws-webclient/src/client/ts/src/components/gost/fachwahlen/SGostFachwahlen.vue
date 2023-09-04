<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <hilfe-gost-fachwahlen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--full">
		<s-gost-fachwahlen-uebersicht :fachwahlstatistik="fachwahlstatistik" :do-select="doSelect" />
		<router-view />
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref } from "vue";
	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";

	defineProps<GostFachwahlenProps>();

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid;
		grid-template-columns: 1fr minmax(20rem, 0.5fr);
	}

</style>
