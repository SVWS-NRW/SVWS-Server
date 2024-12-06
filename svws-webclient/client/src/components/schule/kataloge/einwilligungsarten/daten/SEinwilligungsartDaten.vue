<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="auswahl().bezeichnung" @change="value => patch({ bezeichnung: value?.trim() })" type="text" />
				<svws-ui-text-input placeholder="Schlüssel" :model-value="auswahl().schluessel" @change="value => patch({ schluessel: value?.trim() })" type="text" />
				<svws-ui-textarea-input placeholder="Beschreibung" :model-value="auswahl().beschreibung" @change="value => patch({ beschreibung: trimBeschreibung(value) })" type="text" />
				<svws-ui-select placeholder="Personentyp" label="Personentyp" v-model="personTypAuswahl" :items="PersonTyp.values()" :item-text="item => item.bezeichnung" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { EinwilligungsartDatenProps } from "./SEinwilligungsartDatenProps";
	import { PersonTyp } from "@core";

	const props = defineProps<EinwilligungsartDatenProps>();

	const personTypAuswahl = computed<PersonTyp | null>({
		get: () => PersonTyp.getByID(props.auswahl().personTyp) ?? null,
		set: (value) => void props.patch({ personTyp: value?.id })
	});

	function trimBeschreibung(beschreibung: string | null) {
		return beschreibung !== null ? beschreibung.trim() : "";
	}

</script>
