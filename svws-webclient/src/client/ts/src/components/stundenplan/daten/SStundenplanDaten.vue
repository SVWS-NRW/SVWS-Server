<template>
	<div class="app-container">
		<s-card-stundenplan-daten />
	</div>
</template>

<script setup lang="ts">

	import { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

</script>
