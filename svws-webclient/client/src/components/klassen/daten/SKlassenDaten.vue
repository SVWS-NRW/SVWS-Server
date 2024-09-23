<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<template #actions>
					<svws-ui-checkbox :model-value="data.istSichtbar" :disabled="!hatKompetenzUpdate" @update:model-value="istSichtbar => patchPartial({ istSichtbar })"> Ist sichtbar </svws-ui-checkbox>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :disabled="!hatKompetenzUpdate" :required="true" :max-len="15" :valid="validateKuerzel" :model-value="data.kuerzel"
						@change="kuerzel => patchPartial({ kuerzel }, validateKuerzel(kuerzel))" type="text" />
					<svws-ui-text-input placeholder="Beschreibung" :disabled="!hatKompetenzUpdate" :max-len="150" :valid="validateBeschreibung" :model-value="data.beschreibung"
						@change="beschreibung => patchPartial({ beschreibung }, validateBeschreibung(beschreibung))" type="text" />
					<svws-ui-spacing />
					<svws-ui-select title="Klassen-Jahrgang" :disabled="!hatKompetenzUpdate" v-model="jahrgang" :items="jahrgaenge" :item-text="textJahrgang" :empty-text="() => 'JU - Jahrgangsübergreifend'" removable />
					<svws-ui-select title="Parallelität" :disabled="!hatKompetenzUpdate" :model-value="data.parallelitaet ?? '---'"
						@update:model-value="value => patchPartial({ parallelitaet: value === '---' ? null : value })"
						:items="['---','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']" :item-text="p => p" />
					<!-- TODO Select mit der Liste der Teilstandorte für diese Schule (:disabled="!hatKompetenzUpdate" ) -->
					<svws-ui-text-input placeholder="Teilstandort" disabled :model-value="data.teilstandort" type="text" />
					<div class="flex flex-row">
						<svws-ui-input-number placeholder="Sortierung" :disabled="!hatKompetenzUpdate" :required="true" :min="0" :model-value="data.sortierung"
							@change="sortierung => patchPartial({ sortierung }, validateSortierung(sortierung))" />
					</div>
					<svws-ui-spacing />
					<svws-ui-select v-if="!listeVorgaengerklassen.isEmpty()" title="Vorgängerklasse" :disabled="!hatKompetenzUpdate" :model-value="data.idVorgaengerklasse === null ? null : mapKlassenVorigerAbschnitt().get(data.idVorgaengerklasse)"
						@update:model-value="value => patchPartial({ idVorgaengerklasse: value?.id ?? null })"
						:items="listeVorgaengerklassen" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Vorgängerklasse" :model-value="data.kuerzelVorgaengerklasse === null ? '&nbsp;' : data.kuerzelVorgaengerklasse" type="text" disabled />
					<svws-ui-select v-if="!listeFolgeklassen.isEmpty()" title="Folgeklasse" :disabled="!hatKompetenzUpdate" :model-value="data.idFolgeklasse === null ? null : mapKlassenFolgenderAbschnitt().get(data.idFolgeklasse)"
						@update:model-value="value => patchPartial({ idFolgeklasse: value?.id ?? null })"
						:items="listeFolgeklassen" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Folgeklasse" :model-value="data.kuerzelFolgeklasse === null ? '&nbsp;' : data.kuerzelFolgeklasse" type="text" disabled />
					<svws-ui-spacing />
					<svws-ui-select title="Schulgliederung" :disabled="!hatKompetenzUpdate" :model-value="Schulgliederung.data().getWertByID(data.idSchulgliederung)"
						@update:model-value="value => patchPartial({ idSchulgliederung: value?.daten(schuljahr)?.id ?? -1 })"
						:items="schulgliederungen" :item-text="f => (f.daten(schuljahr)?.kuerzel ?? '—') + ' - ' + (f.daten(schuljahr)?.text ?? '—')" />
					<!-- TODO Auswahl der Prüfungsordnungen und :disabled="!hatKompetenzUpdate" -->
					<svws-ui-text-input placeholder="Prüfungsordnung" disabled :model-value="data.pruefungsordnung" type="text" />
					<svws-ui-select title="Klassenart" :disabled="!hatKompetenzUpdate" :model-value="Klassenart.data().getWertByID(data.idKlassenart)"
						@update:model-value="value => patchPartial({ idKlassenart: value?.daten(schuljahr)?.id ?? -1 })"
						:items="Klassenart.data().getWerteBySchuljahr(schuljahr)" :item-text="f => (f.daten(schuljahr)?.kuerzel ?? '—') + ' - ' + (f.daten(schuljahr)?.text ?? '—')" />
					<svws-ui-select v-if="data.idAllgemeinbildendOrganisationsform !== null" title="Organisationsform" :disabled="!hatKompetenzUpdate" :model-value="AllgemeinbildendOrganisationsformen.data().getWertByID(data.idAllgemeinbildendOrganisationsform)"
						@update:model-value="value => patchPartial({ idAllgemeinbildendOrganisationsform: value?.daten(schuljahr)?.id ?? -1 })"
						:items="AllgemeinbildendOrganisationsformen.values()" :item-text="f => (f.daten(schuljahr)?.kuerzel ?? '—') + ' - ' + (f.daten(schuljahr)?.text ?? '—')" />
					<svws-ui-select v-if="data.idBerufsbildendOrganisationsform !== null" title="Organisationsform" :disabled="!hatKompetenzUpdate" :model-value="BerufskollegOrganisationsformen.data().getWertByID(data.idBerufsbildendOrganisationsform)"
						@update:model-value="value => patchPartial({ idBerufsbildendOrganisationsform: value?.daten(schuljahr)?.id ?? -1 })"
						:items="BerufskollegOrganisationsformen.values()" :item-text="f => (f.daten(schuljahr)?.kuerzel ?? '—') + ' - ' + (f.daten(schuljahr)?.text ?? '—')" />
					<svws-ui-select v-if="data.idWeiterbildungOrganisationsform !== null" title="Organisationsform" :disabled="!hatKompetenzUpdate" :model-value="WeiterbildungskollegOrganisationsformen.data().getWertByID(data.idWeiterbildungOrganisationsform)"
						@update:model-value="value => patchPartial({ idWeiterbildungOrganisationsform: value?.daten(schuljahr)?.id ?? -1 })"
						:items="WeiterbildungskollegOrganisationsformen.values()" :item-text="f => (f.daten(schuljahr)?.kuerzel ?? '—') + ' - ' + (f.daten(schuljahr)?.text ?? '—')" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing :size="2" />
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-checkbox :model-value="data.noteneingabeGesperrt" :disabled="!hatKompetenzUpdate" @update:model-value="noteneingabeGesperrt => patchPartial({ noteneingabeGesperrt })"> Noteneingabe gesperrt </svws-ui-checkbox>
					<svws-ui-checkbox v-if="schulform === Schulform.G" :disabled="!hatKompetenzUpdate" :model-value="data.verwendungAnkreuzkompetenzen" @update:model-value="verwendungAnkreuzkompetenzen => patchPartial({ verwendungAnkreuzkompetenzen })"> In dieser Klasse werden Ankreuzkompetenzen verwendet </svws-ui-checkbox>
					<svws-ui-checkbox v-if="schulform === Schulform.WB" :disabled="!hatKompetenzUpdate" :model-value="data.beginnSommersemester" @update:model-value="beginnSommersemester => patchPartial({ beginnSommersemester })"> Beginn im Sommersemester </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Klassenleitung">
				<svws-ui-table :columns="columnsKlassenleitungen" :items="listeKlassenlehrer" :clickable="hatKompetenzUpdate" :clicked="klassenleitungAuswahl" v-model:clicked="klassenleitungClicked">
					<template #header(linkToLehrer)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #cell(linkToLehrer)="{rowData}">
						<svws-ui-button type="icon" @click="gotoLehrer(rowData)">
							<span class="icon i-ri-link" />
						</svws-ui-button>
					</template>
					<template v-if="hatKompetenzUpdate" #cell(aktionen)="{ rowData }">
						<div style="vertical-align: center; display: flex;">
							<div class="w-6">
								<svws-ui-button  type="icon" @click.stop="removeKlassenleitungHandler(rowData)">
									<span class="icon i-ri-delete-bin-line" />
								</svws-ui-button>
							</div>
						</div>
					</template>
					<template #footer v-if="hatKompetenzUpdate">
						<s-klassen-daten-lehrer-zuweisung-modal v-slot="{openModal}" :klassen-liste-manager="klassenListeManager" :add-klassenleitung="addKlassenleitung">
							<div style="vertical-align: center; display: flex; float: right; margin-right: 5.7pt">
								<div v-if="klassenleitungAuswahl !== null" class="w-6 me-1">
									<svws-ui-button v-if="showPfeilHoch" type="icon" @click="erhoeheReihenfolge()">
										<span class="icon i-ri-arrow-up-line" />
									</svws-ui-button>
									<svws-ui-button v-else-if="showPfeilRunter" type="icon" @click="reduziereReihenfolge()">
										<span class="icon i-ri-arrow-down-line" />
									</svws-ui-button>
								</div>

								<div style="display: flex; justify-content: flex-end">
									<svws-ui-button type="icon" @click="openModal().value = true">
										<span class="icon i-ri-add-line" />
									</svws-ui-button>
								</div>
							</div>
						</s-klassen-daten-lehrer-zuweisung-modal>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Klassenliste">
			<svws-ui-multi-select v-model="filterSchuelerStatus" title="Status" :items="klassenListeManager().schuelerstatus.list()" :item-text="status => status.daten(schuljahr)?.text ?? '—'" class="col-span-full" />
			<svws-ui-table :columns="colsSchueler" :items="klassenListeManager().getSchuelerListe()">
				<template #cell(status)="{ value } : { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.data().getWertByID(value)?.daten(schuljahr) || "" }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<span class="icon i-ri-link" />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { KlassenDatenProps } from "./SKlassenDatenProps";
	import type { LehrerListeEintrag, KlassenDaten, JahrgangsDaten, List } from "@core";
	import { SchuelerStatus, Schulform, Schulgliederung, Klassenart, AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, WeiterbildungskollegOrganisationsformen, ArrayList, BenutzerKompetenz } from "@core";

	const props = defineProps<KlassenDatenProps>();

	const schuljahr = computed<number>(() => props.klassenListeManager().getSchuljahr());

	// TODO auch UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN verwenden und hier unterscheiden zu UNTERRICHTSVERTEILUNG_ANSEHEN
	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	// TODO auch UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN berücksichtigen in Bezug auf Abteilungsleitungen / Koordinationen (API muss dafür noch erweitert werden)
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const klassenleitungAuswahl = ref<LehrerListeEintrag | null>(null);
	const klassenleitungClicked = computed<LehrerListeEintrag | null>({
		get: () => {
			klassenleitungAuswahl.value = props.klassenListeManager().getAuswahlKlassenLeitung();
			return klassenleitungAuswahl.value;
		},
		set: (value) => {
			klassenleitungAuswahl.value = value;
			props.klassenListeManager().setAuswahlKlassenLeitung(value);
		}
	});

	const data = computed<KlassenDaten>(() => props.klassenListeManager().daten());

	function textJahrgang(jg : JahrgangsDaten) : string {
		if (jg.kuerzel === null)
			return 'JU - Jahrgangsübergreifend';
		if (jg.kuerzel === 'E1')
			return '1E' + ' - ' + jg.bezeichnung;
		if (jg.kuerzel === 'E2')
			return '2E' + ' - ' + jg.bezeichnung;
		return jg.kuerzel + ' - ' + jg.bezeichnung;
	}

	const ersteKlassenleitungId = computed<number | undefined>(() =>
		listeKlassenlehrer.value.length > 0 ? listeKlassenlehrer.value[0].id : undefined
	);

	const letzteKlassenleitungId = computed<number | undefined>(() =>
		listeKlassenlehrer.value.length > 0 ? listeKlassenlehrer.value[data.value.klassenLeitungen.size()-1].id : undefined
	);

	const showPfeilHoch = computed<boolean>(() => {
		if (!klassenleitungClicked.value)
			return false;

		return (listeKlassenlehrer.value.length > 0)
			&& (klassenleitungClicked.value.id !== ersteKlassenleitungId.value)
			&& data.value.klassenLeitungen.contains(klassenleitungClicked.value.id);
	});

	const showPfeilRunter = computed<boolean>(() => {
		if (!klassenleitungClicked.value)
			return false;

		return (listeKlassenlehrer.value.length > 0)
			&& (klassenleitungClicked.value.id !== letzteKlassenleitungId.value)
			&& data.value.klassenLeitungen.contains(klassenleitungClicked.value.id);
	});

	async function removeKlassenleitungHandler(rowData : LehrerListeEintrag) : Promise<void> {
		await props.removeKlassenleitung(rowData);
		if ((klassenleitungClicked.value !== null) && (klassenleitungClicked.value.id === rowData.id))
			klassenleitungClicked.value = null;
	}

	async function erhoeheReihenfolge(): Promise<void> {
		if (!klassenleitungClicked.value)
			return;
		await props.updateReihenfolgeKlassenleitung(klassenleitungClicked.value.id, true);
	}

	async function reduziereReihenfolge() : Promise<void> {
		if (!klassenleitungClicked.value)
			return;
		await props.updateReihenfolgeKlassenleitung(klassenleitungClicked.value.id, false);
	}

	const jahrgang = computed<JahrgangsDaten | null>({
		get: () => (data.value.idJahrgang === null) ? null : props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang),
		set: (value) => void props.patch({ idJahrgang: value?.id ?? null })
	});

	const jahrgaenge = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of props.klassenListeManager().jahrgaenge.list()) {
			if (jg.kuerzel !== "E3") // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
		}
		return result;
	});


	const filterSchuelerStatus = computed<SchuelerStatus[]>({
		get: () => [...props.klassenListeManager().schuelerstatus.auswahl()],
		set: (value) => {
			props.klassenListeManager().schuelerstatus.auswahlClear();
			for (const v of value)
				props.klassenListeManager().schuelerstatus.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const listeFolgeklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();
		if (data.value.idJahrgang === null) {
			for (const kl of props.mapKlassenFolgenderAbschnitt().values())
				result.add(kl);
			return result;
		}
		const jg = props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang);
		if (jg === null)
			return result;
		for (const kl of props.mapKlassenFolgenderAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.klassenListeManager().jahrgaenge.get(kl.idJahrgang);
				if (jg.idFolgejahrgang === jgKl?.id)
					result.add(kl);
			}
		}
		return result;
	});

	const listeVorgaengerklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();
		if (data.value.idJahrgang === null) {
			for (const kl of props.mapKlassenVorigerAbschnitt().values())
				result.add(kl);
			return result;
		}
		const jg = props.klassenListeManager().jahrgaenge.get(data.value.idJahrgang);
		if (jg === null)
			return result;
		for (const kl of props.mapKlassenVorigerAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.klassenListeManager().jahrgaenge.get(kl.idJahrgang);
				if (jg.id === jgKl?.idFolgejahrgang)
					result.add(kl);
			}
		}
		return result;
	});

	const listeKlassenlehrer = computed<LehrerListeEintrag[]>(() => {
		const a : LehrerListeEintrag[] = [];
		for (const klassenLeitung of props.klassenListeManager().daten().klassenLeitungen) {
			const lehrer : LehrerListeEintrag | null = props.klassenListeManager().lehrer.get(klassenLeitung);
			if (lehrer !== null)
				a.push(lehrer);
		}
		return a;
	});

	const columnsKlassenleitungen = computed<Array<DataTableColumn>>(() => {
		const result = new Array<DataTableColumn>();
		result.push({ key: "linkToLehrer", label: " ", fixedWidth: 1.75, align: "center" });
		result.push({ key: "kuerzel", label: "Kürzel", span: 1, sortable: false });
		result.push({ key: "nachname", label: "Nachname", span: 2, sortable: false });
		result.push({ key: "vorname", label: "Vorname", span: 2, sortable: false });
		if (hatKompetenzUpdate.value)
			result.push({ key: "aktionen", label: "", span: 2, sortable: false, align: "right" });
		return result;
	});


	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Vorname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

	const validateKuerzel = (kuerzel: string | null): boolean => props.klassenListeManager().validateKuerzel(kuerzel);
	const validateBeschreibung = (beschreibung: string | null): boolean => props.klassenListeManager().validateBeschreibung(beschreibung);
	const validateSortierung = (sortierung: number | null): boolean => props.klassenListeManager().validateSortierung(sortierung);

	async function patchPartial(data: Partial<KlassenDaten>, isValid?: boolean): void {
		if (isValid === undefined || isValid)
			await props.patch(data);
	}

</script>
