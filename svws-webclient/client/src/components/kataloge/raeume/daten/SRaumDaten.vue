<template>
	<div class="page--content">
		<s-card-raum-daten :auswahl :patch />
	</div>
</template>

<script setup lang="ts">

	import type { RaumDatenProps } from "./SRaumDatenProps";

	const props = defineProps<RaumDatenProps>();

</script>
