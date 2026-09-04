<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<!-- Allgemein -->
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" class="contentFocusField" span="full"
						v-model="model.proxy.bezeichnung"
						:validation="() => model.getFehler('bezeichnung')"
						@change="model.patch"
						:max-len="100" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Bemerkung" span="full"
						v-model="model.proxy.bemerkung"
						:validation="() => model.getFehler('bemerkung')"
						@change="model.patch"
						:max-len="50" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="model.proxy.tel"
						:validation="() => model.getFehler('tel')"
						@change="model.patch"
						:max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="model.proxy.email"
						:validation="() => model.getFehler('email')"
						@change="model.patch"
						:max-len="40" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Adresse -->
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße" span="full"
						v-model="model.adresse.value"
						:validation="() => model.getFehler('strassenname')"
						@change="model.patch"
						:max-len="55" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="PLZ"
						v-model="model.proxy.plz"
						:validation="() => model.getFehler('plz')"
						@change="model.patch"
						:max-len="10" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Wohnort"
						v-model="model.proxy.ort"
						:validation="() => model.getFehler('ort')"
						@change="model.patch"
						:max-len="30" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Ansicht & Sortierung -->
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :max="32000" :readonly="!hatKompetenzUpdate" :removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { KindergaertenDatenProps } from "~/components/schule/kataloge/kindergaerten/daten/KindergaertenDatenProps";
	import { KindergaertenModelProxy } from "../modelproxy/KindergaertenModelProxy";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<KindergaertenDatenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new KindergaertenModelProxy(() => props.manager().daten(), () => props.manager().liste.list(), props.patch);
</script>
