<template>
	<div class="page--content">
		<s-card-schueler-herkunft :data="data" :patch="patch" />
		<s-card-schueler-entlassung :data="data" :patch="patch" />
		<s-card-schueler-wechsel :data="data" :patch="patch" />
		<s-card-schueler-grundschulbesuch :data="data" :patch="patch" />
		<s-card-schueler-sekundarstufe1 :data="data" :patch="patch" />
		<s-card-schueler-merkmale :data="data" />
		<s-card-schueler-schulen :data="data" />
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerSchulbesuchProps } from './SSchuelerSchulbesuchProps';

	const props = defineProps<SchuelerSchulbesuchProps>();

</script>
