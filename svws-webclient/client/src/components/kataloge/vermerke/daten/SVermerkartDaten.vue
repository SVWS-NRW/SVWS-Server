<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="auswahl().bezeichnung" @change="bezeichnung => patch({ bezeichnung })" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { VermerkartDatenProps } from "./SVermerkartDatenProps";
	const props = defineProps<VermerkartDatenProps>();

</script>
