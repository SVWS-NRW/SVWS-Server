<template>
	<div class="app-container">
		<s-card-aufsichtsbereich-daten :data="data" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { AufsichtsbereichDatenProps } from "./SAufsichtsbereichDatenProps";

	const props = defineProps<AufsichtsbereichDatenProps>();

</script>
