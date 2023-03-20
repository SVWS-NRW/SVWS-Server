<template>
	<svws-ui-content-card :title="'Beratungslehrer'">
		<div class="content-wrapper">
			<div class="input-wrapper">Hier kommen die Beratungslehrer hin</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostJahrgangsdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		jahrgangsdaten: () => GostJahrgangsdaten;
	}>();

</script>
