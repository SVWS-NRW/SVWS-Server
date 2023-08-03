<template>
	<div class="page--content">
		<s-card-religion-daten :auswahl="auswahl" @patch="patch" />
	</div>
</template>

<script setup lang="ts">
	import type { ReligionDatenProps } from "./SReligionDatenProps";

	const props = defineProps<ReligionDatenProps>();
</script>
