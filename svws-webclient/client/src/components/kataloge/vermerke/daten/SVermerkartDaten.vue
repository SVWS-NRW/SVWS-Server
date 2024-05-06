<template>
	<div class="page--content">
		<s-card-vermerkart-daten :data="auswahl" :patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { VermerkartDatenProps } from "./SVermerkartDatenProps";
	const props = defineProps<VermerkartDatenProps>();
</script>
