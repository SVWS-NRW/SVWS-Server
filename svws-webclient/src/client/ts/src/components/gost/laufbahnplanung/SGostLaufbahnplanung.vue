<template>
	<div class="app-container">
		<s-card-gost-laufbahnplanung :laufbahnplanungsergebnisse="laufbahnplanungsergebnisse" :config="config" />
	</div>
</template>

<script setup lang="ts">

	import type { GostLaufbahnplanungProps } from "./SGostLaufbahnplanungProps";

	defineProps<GostLaufbahnplanungProps>();

</script>
