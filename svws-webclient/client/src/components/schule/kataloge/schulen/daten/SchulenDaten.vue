<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Schulangaben" v-if="manager().hasDaten()">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Schulform" v-if="isInternal"
						:model-value="model.schulformInternal.value"
						readonly />
					<ui-select label="Schulform" v-else-if="!isInternal"
						class="contentFocusField"
						v-model="model.selectedSchulformSonstigeSchule.value"
						:manager="schulformSelectManager"
						:readonly="isInternal" />
					<svws-ui-text-input placeholder="Statistik-Schulnummer"
						:model-value="model.proxy.schulnummerStatistik"
						readonly statistics />
					<svws-ui-text-input placeholder="Kürzel"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="10" :readonly />
					<svws-ui-text-input placeholder="Schulname"
						v-model="model.proxy.name"
						:validation="() => model.getFehler('name')"
						@change="model.patch"
						:max-len="120" :readonly required />
					<svws-ui-text-input placeholder="Kurzbezeichnung"
						v-model="model.proxy.kurzbezeichnung"
						:validation="() => model.getFehler('kurzbezeichnung')"
						@change="model.patch"
						:max-len="40" :readonly required />
					<svws-ui-text-input placeholder="Schulleitung"
						v-model="model.proxy.schulleiter"
						:validation="() => model.getFehler('schulleiter')"
						@change="model.patch"
						:max-len="40" :readonly />
					<svws-ui-text-input placeholder="Straße"
						v-model="model.adresse.value"
						:validation="() => model.getFehler('strassenname')"
						@change="model.patch"
						:max-len="55" :readonly />
					<svws-ui-text-input placeholder="PLZ"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						@change="model.patch"
						:max-len="10" :readonly />
					<svws-ui-text-input placeholder="Ort"
						v-model="model.proxy.ort"
						:validation="() => model.getFehler('ort')"
						@change="model.patch"
						:max-len="50" :readonly />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="model.proxy.telefon"
						:validation="() => model.getFehler('telefon')"
						@change="model.patch"
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="Fax" type="tel"
						v-model="model.proxy.fax"
						:validation="() => model.getFehler('fax')"
						@change="model.patch"
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="model.proxy.email"
						:validation="() => model.getFehler('email')"
						@change="model.patch"
						:max-len="40" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
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
	import { BenutzerKompetenz, HerkunftSchulform } from "@core";
	import { CoreTypeSelectManager, useBenutzerState, useSchuleState } from "@ui";
	import { SchuleModelProxy } from "~/components/schule/kataloge/schulen/modelproxy/SchuleModelProxy";

	const props = defineProps<SchulenDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const model = new SchuleModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);
	const isInternal = computed<boolean>(() => model.proxy.schulnummerStatistik?.charAt(0) === "1");

	const schulformSelectManager = new CoreTypeSelectManager({
		clazz: HerkunftSchulform.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

</script>
