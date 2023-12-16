<template>
	<div class="page--content">
		<s-card-kurs-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" :map-lehrer="mapLehrer" :patch="patch" />
		<s-card-kurs-schueler :list-schueler="data.schueler" :goto-schueler="gotoSchueler" />
	</div>
</template>

<script setup lang="ts">
	import type { KursDatenProps } from "./SKursDatenProps";

	const props = defineProps<KursDatenProps>();
</script>
