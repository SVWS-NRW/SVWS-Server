<template>
	<div class="page--content">
		<s-card-pausenzeit-daten :auswahl :patch />
	</div>
</template>

<script setup lang="ts">

	import type { PausenzeitDatenProps } from "./SPausenzeitDatenProps";

	const props = defineProps<PausenzeitDatenProps>();

</script>
