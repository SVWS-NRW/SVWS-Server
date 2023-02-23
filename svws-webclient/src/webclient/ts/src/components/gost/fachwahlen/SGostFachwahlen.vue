<template>
	<div class="app-container">
		<s-card-gost-fachwahlen :jahrgang="jahrgang" :fachwahlen="fachwahlen" />
	</div>
</template>

<script setup lang="ts">

	import { GostFachwahlenProps } from "./SGostFachwahlenProps";

	defineProps<GostFachwahlenProps>();

</script>
