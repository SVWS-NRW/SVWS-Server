<template>
	<div class="page--content">
		<h2> TODO </h2>
		{{ props.data?.name }}
	</div>
</template>

<script setup lang="ts">

	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";

	const props = defineProps<SchemaUebersichtProps>();

</script>
