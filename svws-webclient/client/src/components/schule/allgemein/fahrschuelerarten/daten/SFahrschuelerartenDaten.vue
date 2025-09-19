<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung" :readonly
					:max-len="30" :min-len="1" @change="v => patch({ bezeichnung: v?.trim() ?? undefined })" />
				<svws-ui-input-number placeholder="Sortierung" :model-value="manager().daten().sortierung" :readonly :min="0" :max="32000"
					@change="v => patch({ sortierung: v === null ? 32000 : v })" />
				<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { FahrschuelerartenDatenProps } from "~/components/schule/allgemein/fahrschuelerarten/daten/SFahrschuelerartenDatenProps";
	import { BenutzerKompetenz } from "@core";
	import { computed } from "vue";

	const props = defineProps<FahrschuelerartenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);


</script>
