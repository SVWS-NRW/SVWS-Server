<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Schulangaben" v-if="manager().hasDaten()">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Schulform"
						class="contentFocusField"
						v-model="model.selectedSchulform.value"
						:manager="schulformSelectManager"
						:readonly />
					<svws-ui-text-input placeholder="Statistik-Schulnummer"
						:model-value="model.proxy.schulnummerStatistik"
						readonly statistics />
					<svws-ui-text-input placeholder="Kürzel"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@commit="model.patch"
						:max-len="10" :readonly />
					<svws-ui-text-input placeholder="Schulname"
						v-model="model.proxy.name"
						:validation="() => model.getFehler('name')"
						@commit="model.patch"
						:max-len="120" :readonly required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="model.proxy.kurzbezeichnung"
						:validation="() => model.getFehler('kurzbezeichnung')"
						@commit="model.patch"
						:max-len="40" :readonly required />
					<svws-ui-text-input placeholder="Schulleitung"
						v-model="model.proxy.schulleiter"
						:validation="() => model.getFehler('schulleiter')"
						@commit="model.patch"
						:max-len="40" :readonly />
					<svws-ui-text-input placeholder="Straße"
						v-model="model.adresse.value"
						:validation="() => model.getFehler('strassenname')"
						@commit="model.patch"
						:max-len="55" :readonly />
					<svws-ui-text-input placeholder="PLZ"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						@commit="model.patch"
						:max-len="10" :readonly />
					<svws-ui-text-input placeholder="Ort"
						v-model="model.proxy.ort"
						:validation="() => model.getFehler('ort')"
						@commit="model.patch"
						:max-len="50" :readonly />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="model.proxy.telefon"
						:validation="() => model.getFehler('telefon')"
						@commit="model.patch"
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="Fax" type="tel"
						v-model="model.proxy.fax"
						:validation="() => model.getFehler('fax')"
						@commit="model.patch"
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="model.proxy.email"
						:validation="() => model.getFehler('email')"
						@commit="model.patch"
						:max-len="40" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@commit="model.patch"
						:min="0" :max="32000" :readonly :removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { SchulenDatenProps } from "./SchulenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz, Schulform } from "@core";
	import { CoreTypeSelectManager, useSchuleState } from "@ui";
	import { SchuleModelProxy } from "~/components/schule/kataloge/schulen/modelproxy/SchuleModelProxy";

	const props = defineProps<SchulenDatenProps>();
	const schuleState = useSchuleState();

	const model = new SchuleModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const schulformSelectManager = new CoreTypeSelectManager({
		clazz: Schulform.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>
