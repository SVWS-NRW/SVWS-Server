<template>
	<div class="content">
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-todo>TODO: istAbschlussPrognose</svws-ui-todo>
				<svws-ui-todo>TODO: versetzungsvermerk</svws-ui-todo>
				<svws-ui-todo>TODO: abschlussart</svws-ui-todo>
				<svws-ui-select title="Folgeklasse" v-model="folgeklasse" :items="props.manager().klasseGetMenge()" :item-text="i => `${i.kuerzel}`" autocomplete />
			</svws-ui-input-wrapper>
			<svws-ui-spacing />
			<svws-ui-input-wrapper>
				<svws-ui-todo> TODO: abschluss </svws-ui-todo>
				<svws-ui-todo> TODO: abschlussBerufsbildend </svws-ui-todo>
			</svws-ui-input-wrapper>
			<svws-ui-spacing />
			<svws-ui-textarea-input placeholder="Text für Abschluss-Berechnung" :model-value="manager().lernabschnittGet().textErgebnisPruefungsalgorithmus"
				@change="textErgebnisPruefungsalgorithmus => patch({ textErgebnisPruefungsalgorithmus })"
				resizeable="vertical" :autoresize="true" />
			<svws-ui-spacing :size="2" />
			<div class="col-span-full flex gap-4">
				<svws-ui-button type="primary"> Versetzungs-/Abschluss-Berechnung </svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { KlassenListeEintrag } from "@core";
	import type { SchuelerLernabschnittVersetzungAbschlussProps } from "./SSchuelerLernabschnittVersetzungAbschlussProps";

	const props = defineProps<SchuelerLernabschnittVersetzungAbschlussProps>();

	const folgeklasse = computed<KlassenListeEintrag | undefined>({
		get: () => {
			const id = props.manager().lernabschnittGet().folgeklassenID;
			if (id === null)
				return undefined;
			return props.manager().klasseGetByIdOrException(id);
		},
		set: (value) => void props.patch({ folgeklassenID: (value === undefined) ? null : value.id })
	});

</script>

<style lang="postcss" scoped>

	.content {
		@apply w-full h-full grid grid-cols-2;
	}

</style>
