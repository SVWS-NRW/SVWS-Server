<template>
	<div class="page--content">
		<s-card-foerderschwerpunkt-daten :data="data" :patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { FoerderschwerpunktDatenProps } from "./SFoerderschwerpunktDatenProps";

	const props = defineProps<FoerderschwerpunktDatenProps>();

</script>
