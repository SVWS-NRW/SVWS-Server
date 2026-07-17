<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="100" :readonly />
					<svws-ui-text-input placeholder="Bezeichnung" span="full"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="100" required :readonly />
					<ui-select label="Fachklasse" class="col-span-full"
						v-model="model.fachklasse.value"
						:manager="fachklassenManager"
						:readonly required />
					<svws-ui-spacing />
					Die Lernfelder sind zur Zeit nur in Schild3 einsehbar und editiertbar.
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :max="32000"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar"
						:readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { FachklassenDatenProps } from "~/components/schule/kataloge/fachklassen/daten/FachklassenDatenProps";
	import { FachklassenModelProxy } from "~/components/schule/kataloge/fachklassen/modelproxy/FachklassenModelProxy";
	import { computed } from "vue";
	import type { FachklasseKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Fachklasse, HashSet, Schulgliederung } from "@core";
	import { SelectManager, useBenutzerState, useSchuleState } from "@ui";

	const props = defineProps<FachklassenDatenProps>();
	const schuleState = useSchuleState();
	const benutzerState = useBenutzerState();
	const model = new FachklassenModelProxy(() => props.manager().daten(), () => props.manager(), props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const fachklassen = computed<Iterable<FachklasseKatalogEintrag>>(() => {
		const bkIndizes = new HashSet<number>();
		const schulgliederungen = Schulgliederung.getEintraegeBySchuljahrAndSchulform(schuleState.abschnitt.schuljahr, schuleState.schulform);
		for (const s of schulgliederungen) {
			if (s.bkIndex !== null) {
				bkIndizes.add(s.bkIndex);
			}
		}
		return Fachklasse.getBySchuljahrAndBKIndizes(schuleState.abschnitt.schuljahr, bkIndizes);
	});

	const fachklassenManager = new SelectManager<FachklasseKatalogEintrag>({
		options: fachklassen,
		optionDisplayText: f => f.kuerzel,
		selectionDisplayText: f => f.kuerzel,
	});

</script>
