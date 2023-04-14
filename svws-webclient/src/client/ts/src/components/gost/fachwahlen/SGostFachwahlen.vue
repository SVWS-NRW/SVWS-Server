<template>
	<div class="app-container">
		<s-card-gost-fachwahlen :fachwahlen="fachwahlen" />
	</div>
</template>

<script setup lang="ts">

	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";

	defineProps<GostFachwahlenProps>();

</script>
