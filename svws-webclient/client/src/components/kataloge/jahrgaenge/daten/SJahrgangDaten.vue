<template>
	<div class="page--content">
		<s-card-jahrgang-basisdaten :data="data" :map-jahrgaenge="mapKatalogeintraege" @patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { JahrgangDatenProps } from "./SJahrgangDatenProps";

	const props = defineProps<JahrgangDatenProps>();

</script>
