<template>
	<div class="app-container">
		<s-card-zeitraster-daten :add-zeitraster="addZeitraster" :list-katalogeintraege="listKatalogeintraege" :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" />
	</div>
</template>

<script setup lang="ts">

	import type { ZeitrasterDatenProps } from "./SZeitrasterDatenProps";

	const props = defineProps<ZeitrasterDatenProps>();
</script>
