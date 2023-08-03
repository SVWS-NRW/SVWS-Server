<template>
	<div class="page--content">
		<s-card-klasse-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" @patch="patch" />
		<s-card-klasse-klassenleitungen :data="data" :map-lehrer="mapLehrer" @patch="patch" />
		<s-card-klasse-schueler :list-schueler="data.schueler" :goto-schueler="gotoSchueler" />
	</div>
</template>

<script setup lang="ts">
	import type { KlassenDatenProps } from "./SKlassenDatenProps";

	const props = defineProps<KlassenDatenProps>();

</script>
