<template>
	<div class="page--content">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="auswahl().bezeichnung" @change="bezeichnung => patch({ bezeichnung })" type="text" />
				<svws-ui-text-input placeholder="Schlüssel" :model-value="auswahl().schluessel" @change="schluessel => patch({ schluessel })" type="text" />
				<svws-ui-select placeholder="Personentyp" label="Personentyp" v-model="personTypAuswahl" :items="PersonTyp.values()"
					:item-text="item => item.bezeichnung" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type {EinwilligungsartDatenProps} from "./SEinwilligungsartDatenProps";
	import {PersonTyp} from "@core";
	import {computed} from "vue";

	const props = defineProps<EinwilligungsartDatenProps>();

	const personTypAuswahl = computed<PersonTyp | undefined>({
		get: () => PersonTyp.getByID(props.auswahl().personTyp) ?? undefined,
		set: (value) => void props.patch({ personTyp: value?.id })
	});

</script>
