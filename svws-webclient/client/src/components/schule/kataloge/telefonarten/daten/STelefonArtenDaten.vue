<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Telefonarten" class="w-full">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Bezeichnung" :model-value="telefonArtListeManager().auswahl().bezeichnung"
					@change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { TelefonArtenDatenProps } from "~/components/schule/kataloge/telefonarten/daten/STelefonArtenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<TelefonArtenDatenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

</script>
