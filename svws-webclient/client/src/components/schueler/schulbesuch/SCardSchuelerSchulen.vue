<template>
	<svws-ui-content-card title="Alle bisher besuchten Schulen">
		<div>
			<svws-ui-todo />
			<div v-for="schule in data.alleSchulen" :key="schule.schulnummer" class="opacity-50">
				<!-- TODO <svws-zu-table> für die besuchten Schulen -->
				<p> {{ `${schule.schulnummer} ... (${schule.datumVon}-${schule.datumBis})` }} </p>
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
