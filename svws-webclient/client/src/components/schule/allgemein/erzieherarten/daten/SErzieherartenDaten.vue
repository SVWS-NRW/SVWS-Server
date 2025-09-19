<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().auswahl().bezeichnung" :readonly
					:max-len="30" :min-len="1" @change="v => patch({ bezeichnung: v?.trim() ?? undefined })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { ErzieherartenDatenProps } from "~/components/schule/allgemein/erzieherarten/daten/SErzieherartenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<ErzieherartenDatenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	// patching these entries is not aloud according to SchILDzentral
	const idsOfNonPatchableEntries = new Set([1,2,3,4,5])
	const readonly = computed(() => (!hatKompetenzUpdate.value) || (idsOfNonPatchableEntries.has(props.manager().auswahl().id)));

</script>
