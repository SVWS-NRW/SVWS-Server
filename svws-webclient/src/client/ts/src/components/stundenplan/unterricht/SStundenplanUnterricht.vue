<template>
	<div class="app-container">
		<s-card-stundenplan-unterricht />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanUnterrichtProps } from "./SStundenplanUnterrichtProps";

	const props = defineProps<StundenplanUnterrichtProps>();

</script>
