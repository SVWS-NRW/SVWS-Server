<template>
	<div class="h-full w-full grid gap-4 grid-cols-2">
		<svws-ui-content-card>
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Konferenz-Datum" :model-value="manager().lernabschnittGet().datumKonferenz" @change="datumKonferenz=>patch({ datumKonferenz })" type="date" />
				<svws-ui-spacing />
				<svws-ui-textarea-input placeholder="Zeugnisbemerkungen" :model-value="manager().lernabschnittGet().bemerkungen.zeugnisAllgemein"
					@change="zeugnisAllgemein => patchBemerkungen({ zeugnisAllgemein: zeugnisAllgemein === null ? '' : zeugnisAllgemein })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Arbeits- und Sozialverhalten" :model-value="manager().lernabschnittGet().bemerkungen.zeugnisASV"
					@change="zeugnisASV => patchBemerkungen({ zeugnisASV: zeugnisASV === null ? '' : zeugnisASV })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Außerunterrichtliches Engagement" :model-value="manager().lernabschnittGet().bemerkungen.zeugnisAUE"
					@change="zeugnisAUE => patchBemerkungen({ zeugnisAUE: zeugnisAUE === null ? '' : zeugnisAUE })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Bemerkung Versetzung" :model-value="manager().lernabschnittGet().bemerkungen.versetzungsentscheidung"
					@change="versetzungsentscheidung => patchBemerkungen({ versetzungsentscheidung: versetzungsentscheidung === null ? '' : versetzungsentscheidung })"
					resizeable="vertical" :autoresize="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittKonferenzProps } from "./SSchuelerLernabschnittKonferenzProps";

	const props = defineProps<SchuelerLernabschnittKonferenzProps>();

</script>
