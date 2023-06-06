<template>
	<svws-ui-content-card :title="'Beratungslehrer'" class="opacity-50">
		<span>Hier kommen die Beratungslehrer hin.</span>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostJahrgangsdaten } from "@core";

	const props = defineProps<{
		jahrgangsdaten: () => GostJahrgangsdaten;
	}>();

</script>
