<template>
	<svws-ui-content-card title="Besondere Merkmale für die Statistik">
		<div>
			<svws-ui-todo />
			<div v-for="merkmal in data.merkmale" :key="merkmal.id">
				<!-- TODO <svws-zu-table> für die Merkmale -->
				<p> {{ merkmal.id + " (" + merkmal.datumVon + "-" + merkmal.datumBis + ")" }} </p>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@core";

	defineProps<{
		data: SchuelerSchulbesuchsdaten;
	}>();

</script>
