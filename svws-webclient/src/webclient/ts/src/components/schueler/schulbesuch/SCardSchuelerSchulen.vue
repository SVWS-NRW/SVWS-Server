<template>
	<svws-ui-content-card title="Alle bisher besuchten Schulen">
		<div>
			Hier wird demnächst die Übersicht über die besuchten Schulen implementiert
			<div v-for="schule in props.data.daten?.alleSchulen">
				<!-- TODO <svws-zu-table> für die besuchten Schulen -->
				<p> {{ schule.schulnummer + " ... (" + schule.datumVon + "-" + schule.datumBis + ")" }} </p>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";

	const props = defineProps<{ data: DataSchuelerSchulbesuchsdaten }>();

</script>
