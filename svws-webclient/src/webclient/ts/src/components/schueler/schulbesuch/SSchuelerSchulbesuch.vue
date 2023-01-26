<template>
	<div class="app-container">
		<s-card-schueler-herkunft :data="data" @patch="(data) => doPatch(data)" />
		<s-card-schueler-entlassung :data="data" @patch="(data) => doPatch(data)" />
		<s-card-schueler-wechsel :data="data" @patch="(data) => doPatch(data)" />
		<s-card-schueler-grundschulbesuch :data="data" @patch="(data) => doPatch(data)" />
		<s-card-schueler-sekundarstufe1 :data="data" @patch="(data) => doPatch(data)" />
		<s-card-schueler-merkmale :data="data" />
		<s-card-schueler-schulen :data="data" />
	</div>
</template>

<script setup lang="ts">

	import { SchuelerSchulbesuchsdaten } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		data: SchuelerSchulbesuchsdaten;
		patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
	}>();

	function doPatch(data: Partial<SchuelerSchulbesuchsdaten>) {
		void props.patch(data);
	}

</script>
