<template>
	<div class="app-container">
		<s-card-stundenplan-daten :data="data" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

</script>
