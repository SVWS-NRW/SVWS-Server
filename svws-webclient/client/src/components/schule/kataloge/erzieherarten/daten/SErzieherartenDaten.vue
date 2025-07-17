<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Erzieherart" class="w-full">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Bezeichnung" :model-value="erzieherartListeManager().auswahl().bezeichnung"
					@change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { ErzieherartenDatenProps } from "~/components/schule/kataloge/erzieherarten/daten/SErzieherartenDatenProps";

	defineProps<ErzieherartenDatenProps>();

</script>
