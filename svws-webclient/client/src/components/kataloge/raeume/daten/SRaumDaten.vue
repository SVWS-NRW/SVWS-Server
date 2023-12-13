<template>
	<div class="page--content">
		<s-card-raum-daten :data="data" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { RaumDatenProps } from "./SRaumDatenProps";

	const props = defineProps<RaumDatenProps>();

</script>
