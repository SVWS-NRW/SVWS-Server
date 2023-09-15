<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<s-card-klasse-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" @patch="patch" />
			<s-card-klasse-klassenleitungen :data="data" :map-lehrer="mapLehrer" @patch="patch" />
		</div>
		<s-card-klasse-schueler :list-schueler="data.schueler" :goto-schueler="gotoSchueler" />
	</div>
</template>

<script setup lang="ts">
	import type { KlassenDatenProps } from "./SKlassenDatenProps";

	const props = defineProps<KlassenDatenProps>();

</script>
