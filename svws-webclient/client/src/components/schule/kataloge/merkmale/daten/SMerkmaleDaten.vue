<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					:readonly :max-len="100" @change="v => patch({ bezeichnung: v ?? undefined })" />
				<svws-ui-text-input required placeholder="Kürzel" :model-value="manager().daten().kuerzel"
					:readonly :max-len="10" :min-len="1" @change="patchKuerzel" />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="manager().daten().istSchuelermerkmal" :readonly
					@update:model-value="istSchuelermerkmal => patch({ istSchuelermerkmal })">
					Schülermerkmal
				</svws-ui-checkbox>
				<div />
				<svws-ui-checkbox :model-value="manager().daten().istSchulmerkmal" :readonly
					@update:model-value="istSchulmerkmal => patch({ istSchulmerkmal })">
					Schulmerkmal
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { MerkmaleDatenProps } from "~/components/schule/kataloge/merkmale/daten/SMerkmaleDatenProps";
	import { BenutzerKompetenz, JavaString } from "@core";
	import { computed } from "vue";

	const props = defineProps<MerkmaleDatenProps>()
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

	function patchKuerzel(v: string | null) {
		if (v !== null && !JavaString.isBlank(v))
			void props.patch({kuerzel: v})
	}

</script>
