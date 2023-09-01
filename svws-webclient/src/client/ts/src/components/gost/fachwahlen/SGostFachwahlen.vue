<template>
	<div class="page--content page--content--full">
		<s-gost-fachwahlen-uebersicht :fachwahlstatistik="fachwahlstatistik" :do-select="doSelect" />
		<router-view />
	</div>
</template>

<script setup lang="ts">

	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";

	defineProps<GostFachwahlenProps>();

</script>

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: 1fr minmax(20rem, 0.5fr);
}
</style>
