<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Schulgliederung" span="full"
						:model-value="model.bezeichnungSchulgliederung.value"
						readonly />
					<svws-ui-text-input placeholder="Fachklasse" span="full"
						:model-value="model.bezeichnungFachklasse.value"
						readonly />
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="100" :readonly />
					<svws-ui-text-input placeholder="Fachklassenschlüssel"
						:model-value="model.schluesselFachklasse.value"
						readonly />
					<svws-ui-text-input placeholder="Bezeichnung" span="full"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="100" required
						:readonly />
					<svws-ui-text-input placeholder="Bezeichnung (weibliche Form)" span="full"
						v-model="model.proxy.bezeichnungWeiblich"
						:validation="() => model.getFehler('bezeichnungWeiblich')"
						@change="model.patch"
						:max-len="100" required
						:readonly />
					<div class="flex col-span-full">
						<svws-ui-text-input placeholder="Berufsebene 1"
							v-model="model.proxy.berufsebene1"
							:validation="() => model.getFehler('berufsebene1')"
							@change="model.patch"
							:max-len="255" required
							:readonly />
						<svws-ui-text-input placeholder="Berufsebene 2"
							v-model="model.proxy.berufsebene2"
							:validation="() => model.getFehler('berufsebene2')"
							@change="model.patch"
							:max-len="255" required
							:readonly />
						<svws-ui-text-input placeholder="Berufsebene 3"
							v-model="model.proxy.berufsebene3"
							:validation="() => model.getFehler('berufsebene3')"
							@change="model.patch"
							:max-len="255" required
							:readonly />
					</div>
					<ui-select label="DQR-Niveau" class="col-span-full"
						v-model="model.dqrNiveau.value"
						:manager="dqrNiveauManager"
						:validation="() => model.getFehler('idDqrNiveau')"
						:readonly />
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
	import { DQRNiveau } from "@core/asd/types/schule/DQRNiveau";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	const props = defineProps<FachklassenDatenProps>();
	const schuleState = useSchuleState();
	const benutzerState = useBenutzerState();
	const model = new FachklassenModelProxy(() => props.manager().daten(), () => props.manager(), schuleState.abschnitt.schuljahr, props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const dqrNiveauManager = new CoreTypeSelectManager({
		clazz: DQRNiveau.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>
