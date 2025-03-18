<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Lernplattform" class="w-full">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Bezeichnung" :model-value="lernplattformListeManager().auswahl().bezeichnung"
					@change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { LernplattformenDatenProps } from "~/components/schule/kataloge/lernplattformen/daten/SLernplattformenDatenProps";

	defineProps<LernplattformenDatenProps>();

</script>
