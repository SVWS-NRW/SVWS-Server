<template>
	<div class="w-250 p-4">
		<svws-ui-content-card>
			<svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Konferenz-Datum" :readonly :model-value="manager().lernabschnittGet().datumKonferenz" @change="datumKonferenz=>patch({ datumKonferenz })" type="date" focus />
				<svws-ui-spacing />
				<svws-ui-textarea-input placeholder="Zeugnisbemerkungen" :readonly :model-value="manager().lernabschnittGet().bemerkungen.zeugnisAllgemein"
					@change="zeugnisAllgemein => patchBemerkungen({ zeugnisAllgemein: zeugnisAllgemein === null ? '' : zeugnisAllgemein })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Arbeits- und Sozialverhalten" :readonly :model-value="manager().lernabschnittGet().bemerkungen.zeugnisASV"
					@change="zeugnisASV => patchBemerkungen({ zeugnisASV: zeugnisASV === null ? '' : zeugnisASV })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Außerunterrichtliches Engagement" :readonly :model-value="manager().lernabschnittGet().bemerkungen.zeugnisAUE"
					@change="zeugnisAUE => patchBemerkungen({ zeugnisAUE: zeugnisAUE === null ? '' : zeugnisAUE })" resizeable="vertical" :autoresize="true" />
				<svws-ui-textarea-input placeholder="Bemerkung Versetzung" :readonly :model-value="manager().lernabschnittGet().bemerkungen.versetzungsentscheidung"
					@change="versetzungsentscheidung => patchBemerkungen({ versetzungsentscheidung: versetzungsentscheidung === null ? '' : versetzungsentscheidung })"
					resizeable="vertical" :autoresize="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuelerLernabschnittKonferenzProps } from "./SchuelerLernabschnittKonferenzProps";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<SchuelerLernabschnittKonferenzProps>();
	const benutzerState = useBenutzerState();

	const readonly = computed<boolean>(() => !(benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)
		|| benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))
	);

</script>
