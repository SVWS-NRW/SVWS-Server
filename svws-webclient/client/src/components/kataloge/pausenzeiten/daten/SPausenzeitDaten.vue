<template>
	<div class="app-container">
		<s-card-pausenzeit-daten :data="data" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { PausenzeitDatenProps } from "./SPausenzeitDatenProps";

	const props = defineProps<PausenzeitDatenProps>();

</script>
