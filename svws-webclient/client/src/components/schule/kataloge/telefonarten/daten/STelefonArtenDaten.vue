<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="telefonArtListeManager().auswahl().bezeichnung"
					:readonly :max-len="30" :min-len="1" @change="v => patch({ bezeichnung: v?.trim() ?? undefined })" />
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
