<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					:readonly :max-len="50" :min-len="1" @change="patchBezeichnung" :valid="bezeichnungIsValid" />
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung" :model-value="manager().daten().sortierung" :readonly :min="0" :max="32000"
						@change="value => patch({ sortierung: value === null ? 32000 : value })" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SportbefreiungenDatenProps } from "~/components/schule/kataloge/sportbefreiungen/daten/SSportbefreiungenDatenProps";
	import { BenutzerKompetenz, JavaString } from "@core";
	import { computed } from "vue";

	const props = defineProps<SportbefreiungenDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

	async function patchBezeichnung(v: string | null) {
		if (bezeichnungIsValid(v))
			void props.patch({bezeichnung: v});
	}

	function bezeichnungIsValid(v: string | null) {
		if ((v === null) || JavaString.isBlank(v) || (v.length > 50))
			return false;
		for (const sportbefreiung of props.manager().liste.list())
			if (JavaString.equalsIgnoreCase(v, sportbefreiung.bezeichnung) && sportbefreiung.id !== props.manager().auswahl().id)
				return false;
		return true;
	}

</script>
