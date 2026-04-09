<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" class="contentFocusField"
						v-model="model.proxy.kuerzel"
						@commit="model.patch"
						:validation="() => model.getFehler('kuerzel')"
						:max-len="20" required :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Bezeichnung"
						v-model="model.proxy.bezeichnung"
						@commit="model.patch"
						:validation="() => model.getFehler('bezeichnung')"
						:max-len="255" required :readonly="!hatKompetenzUpdate" />
					<ui-select label="Fach ASD-Schlüssel"
						v-model="model.selectedFach.value"
						:manager="fachKuerzelSelectManager"
						:validation="() => model.getFehler('kuerzelStatistik')"
						:readonly="!hatKompetenzUpdate" required :removable="false" statistics searchable />
					<ui-select label="Fach ASD-Text"
						v-model="model.selectedFach.value"
						:manager="fachTextSelectManager"
						:readonly="!hatKompetenzUpdate" required :removable="false" statistics searchable />
					<ui-select label="Bilinguale Sachfachsprache"
						v-model="model.selectedSachfachsprache.value"
						:manager="sachfachspracheManager"
						:readonly="!hatKompetenzUpdate" statistics searchable />
					<svws-ui-text-input placeholder="Fachgruppe"
						:model-value="fachgruppe"
						readonly />
					<ui-select label="Aufgabenfeld" v-if="istBerufskolleg"
						v-model="model.selectedAufgabenfeld.value"
						:manager="aufgabenfeldManager"
						:readonly="!hatKompetenzUpdate" searchable />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Zeugnis">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-checkbox v-model="model.proxy.aufZeugnis" :readonly="!hatKompetenzUpdate" focus-class-content>
						Auf Zeugnis
					</svws-ui-checkbox>
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Bezeichnung (Zeugnis)"
						v-model="model.proxy.bezeichnungZeugnis"
						@commit="model.patch"
						:validation="() => model.getFehler('bezeichnungZeugnis')"
						:max-len="255" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Bezeichnung (Überweisungszeugnis)"
						v-model="model.proxy.bezeichnungUeberweisungszeugnis"
						@commit="model.patch"
						:validation="() => model.getFehler('bezeichnungUeberweisungszeugnis')"
						:max-len="255" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Sonstiges">
				<svws-ui-input-wrapper :grid="1">
					<template v-if="hatGymnasialeOberstufe">
						<svws-ui-checkbox v-model="model.proxy.istOberstufenFach" :readonly="!hatKompetenzUpdate">
							Fach der Oberstufe
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.istPruefungsordnungsRelevant" :readonly="!hatKompetenzUpdate">
							Ist Prüfungsordnungs-relevant (z.B. bei Belegprüfungen)
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.istMoeglichAlsNeueFremdspracheInSekII" :readonly="!hatKompetenzUpdate">
							Ist in der Oberstufe eine neu einsetzende Fremdsprache
						</svws-ui-checkbox>
					</template>
					<svws-ui-checkbox v-model="model.proxy.istFremdsprache" :readonly="!hatKompetenzUpdate">
						Ist eine Fremdsprache
					</svws-ui-checkbox>
					<template v-if="!istGrundschule">
						<svws-ui-checkbox v-model="model.proxy.istNachpruefungErlaubt" :readonly="!hatKompetenzUpdate">
							Nachprüfung erlaubt
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.istSchriftlichZK" :readonly="!hatKompetenzUpdate">
							Schriftliches Fach für ZK
						</svws-ui-checkbox>
						<svws-ui-checkbox v-model="model.proxy.holeAusAltenLernabschnitten" :readonly="!hatKompetenzUpdate">
							Berücksichtigen beim Holen von abgeschlossenen Fächern
						</svws-ui-checkbox>
					</template>
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-input-number placeholder="maximale Zeichenanzahl in Fachbemerkungen"
							v-model="model.proxy.maxZeichenInFachbemerkungen"
							@commit="model.patch"
							:validation="() => model.getFehler('maxZeichenInFachbemerkungen')"
							:max="JavaInteger.MAX_VALUE" :readonly="!hatKompetenzUpdate" />
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Ansicht & Sortierung">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@commit="model.patch"
						:min="0" :max="32000" required :removable="false" :readonly="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly="!hatKompetenzUpdate">
						Sichtbar
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { BilingualeSprache, Fach, Schulform, BenutzerKompetenz, JavaInteger } from "@core";
	import type { FaecherDatenProps } from "./FaecherDatenProps";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { FachModelProxy } from "~/components/schule/kataloge/faecher/modelproxy/FachModelProxy";

	const props = defineProps<FaecherDatenProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const model = new FachModelProxy(
		() => props.manager().daten(),
		() => props.manager().liste.list(),
		props.schuljahr,
		props.patch
	);
	const istGrundschule = computed(() => props.manager().schulform() === Schulform.G);
	const istBerufskolleg = computed(() => props.manager().schulform() === Schulform.BK || props.manager().schulform() === Schulform.SB);
	const hatGymnasialeOberstufe = computed(() => props.manager().schulform().daten(props.schuljahr)?.hatGymOb ?? false);

	const fachgruppe = computed<string>(
		() => Fach.getBySchluesselOrDefault(model.proxy.kuerzelStatistik).getFachgruppe(props.schuljahr)?.daten(props.schuljahr)?.text ?? '—'
	);

	const fachKuerzelSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: v => v.schluessel,
		selectionDisplayText: v => v.schluessel,
	});

	const fachTextSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const sachfachspracheManager = new CoreTypeSelectManager({
		clazz: BilingualeSprache.class,
		schuljahr: props.schuljahr,
		schulformen: props.schulform,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const aufgabenfeldManager = new SelectManager({
		options: ["1", "2", "3"],
		optionDisplayText: (v: string) => getTextAufgabenfeld(v),
		selectionDisplayText: (v: string) => getTextAufgabenfeld(v),
	});

	function getTextAufgabenfeld(aufgabenfeld: string | null): string {
		switch (aufgabenfeld) {
			case '1': return 'Aufgabenfeld I';
			case '2': return 'Aufgabenfeld II';
			case '3': return 'Aufgabenfeld III';
			default: return '';
		}
	}

</script>
