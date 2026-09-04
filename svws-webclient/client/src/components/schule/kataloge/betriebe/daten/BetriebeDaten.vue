<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="1">
			<!-- Allgemein -->
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Name" class="contentFocusField"
						v-model="model.proxy.name"
						:validation="() => model.getFehler('name')"
						@change="model.patch"
						:max-len="50" :readonly required />
					<svws-ui-text-input placeholder="Namensergänzung"
						v-model="model.proxy.nameZusatz"
						:validation="() => model.getFehler('nameZusatz')"
						@change="model.patch"
						:max-len="50" :readonly />
					<ui-select label="Betriebsart"
						v-model="model.betriebsart.value"
						:manager="betriebsartenManager"
						:readonly />
					<svws-ui-text-input placeholder="Branche"
						v-model="model.proxy.branche"
						:validation="() => model.getFehler('branche')"
						@change="model.patch"
						:max-len="50" :readonly />
					<svws-ui-textarea-input placeholder="Bemerkungen"
						v-model="model.proxy.bemerkungen"
						@change="model.patch"
						:validation="() => model.getFehler('bemerkungen')"
						resizeable="none" :max-len="255" />
					<div>
						<svws-ui-input-wrapper :grid="1">
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="model.proxy.istAusbildungsbetrieb"
								:readonly>
								Ausbildungsbetrieb
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.istMassnahmentraeger"
								:readonly>
								Maßnahmenträger
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.belehrungNachISGErforderlich"
								:readonly>
								Belehrung nach ISG notwendig
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.bietetPraktikumsplaetzeAn"
								:readonly>
								Bietet Praktikumsplätze
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.erweitertesFuehrungszeugnisErforderlich"
								:readonly>
								Erweitertes Führungszeugnis notwendig
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Adresse -->
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße"
						v-model="model.adresse.value"
						:validation="() => model.getFehler('strasse')"
						@change="model.patch"
						:readonly />
					<ui-select label="Wohnort"
						v-model="model.wohnort.value"
						:manager="wohnortManager"
						:readonly />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="model.proxy.telefon1"
						:validation="() => model.getFehler('telefon1')"
						@change="model.patch"
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="2. Telefon" type="tel"
						v-model="model.proxy.telefon2"
						:validation="() => model.getFehler('telefon2')"
						@change="model.patch"
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="model.proxy.eMail"
						:validation="() => model.getFehler('eMail')"
						@change="model.patch"
						:max-len="100" :readonly />
					<svws-ui-text-input placeholder="Fax" type="tel"
						v-model="model.proxy.fax"
						:validation="() => model.getFehler('fax')"
						@change="model.patch"
						:max-len="20" :readonly />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<!-- Ansprechpartner -->
			<betriebe-ansprechpartner :manager :add-ansprechpartner :patch-ansprechpartner :delete-ansprechpartner :hat-kompetenz-update />
			<svws-ui-spacing :size="2" />
			<!-- Ansicht & Sortierung -->
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0"
						:readonly
						:removable="false" required />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly>
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import type { BetriebeDatenProps } from "~/components/schule/kataloge/betriebe/daten/BetriebeDatenProps";
	import { computed } from "vue";
	import { BetriebModelProxy } from "~/components/schule/kataloge/betriebe/modelproxy/BetriebModelProxy";
	import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
	import type { Betriebsart } from "@core/core/data/schule/Betriebsart";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<BetriebeDatenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new BetriebModelProxy(() => props.manager().daten(), () => props.manager(), props.patch);
	const readonly = computed(() => !hatKompetenzUpdate.value);
	const betriebsartenById = computed<Map<number, Betriebsart>>(() => props.manager().betriebsartenById);
	const orteById = computed<Map<number, OrtKatalogEintrag>>(() => props.manager().orteById);
	const betriebsarten = computed(() => betriebsartenById.value.values());
	const orte = computed(() => orteById.value.values());

	const betriebsartenManager = new SelectManager({
		options: betriebsarten,
		optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	const wohnortManager = new SelectManager({
		options: orte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

	async function patchBemerkung(v: string | null) {
		model.proxy.bemerkungen = v;
		await model.patch();
	}

</script>
