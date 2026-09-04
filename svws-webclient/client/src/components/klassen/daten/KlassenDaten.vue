<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-klassen-daten /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :disabled="!hatKompetenzUpdate" :required="true" :max-len="15" v-model="modelProxy.proxy.kuerzel" @change="modelProxy.patch"
						:validation="() => modelProxy.getFehler('kuerzel')" focus />
					<svws-ui-text-input placeholder="Beschreibung" :disabled="!hatKompetenzUpdate" :max-len="150" v-model="modelProxy.proxy.beschreibung" @change="modelProxy.patch"
						:validation="() => modelProxy.getFehler('beschreibung')" />
					<svws-ui-spacing />

					<svws-ui-select title="Klassen-Jahrgang" v-model="modelProxy.jahrgang.value" :items="modelProxy.jahrgaenge.value" :item-text="getSelectTextJahrgang"
						:empty-text="() => 'JU - Jahrgangsübergreifend'" :disabled="!hatKompetenzUpdate" removable />
					<svws-ui-select title="Parallelität" v-model="modelProxy.parallelitaet.value" :item-text="p => p" :disabled="!hatKompetenzUpdate"
						:items="['---','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']" />
					<svws-ui-spacing />

					<!-- TODO Select mit der Liste der Teilstandorte für diese Schule (:disabled="!hatKompetenzUpdate" ) -->
					<svws-ui-text-input placeholder="Teilstandort" disabled v-model="modelProxy.proxy.teilstandort" @change="modelProxy.patch" />
					<div class="flex flex-row">
						<svws-ui-input-number placeholder="Sortierung" :disabled="!hatKompetenzUpdate" :required="true"
							v-model="modelProxy.proxy.sortierung" @change="modelProxy.patch" :validation="() => modelProxy.getFehler('sortierung')" />
					</div>
					<svws-ui-spacing />

					<svws-ui-select v-if="zeigeVorgaengerklassen()" title="Vorgängerklasse" v-model="modelProxy.vorgaengerklasse.value" :disabled="!hatKompetenzUpdate"
						:items="modelProxy.listeVorgaengerklassen.value" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Vorgängerklasse" :model-value="modelProxy.proxy.kuerzelVorgaengerklasse ?? '—'" disabled />
					<svws-ui-select v-if="zeigeFolgeklassen()" title="Folgeklasse" v-model="modelProxy.folgeklasse.value" :items="modelProxy.listeFolgeklassen.value"
						:item-text="f => f.kuerzel ?? '---'" removable :disabled="!hatKompetenzUpdate" />
					<svws-ui-text-input v-else placeholder="Folgeklasse" :model-value="modelProxy.proxy.kuerzelFolgeklasse ?? '—'" disabled />
					<svws-ui-spacing />

					<svws-ui-select title="Schulgliederung" v-model="modelProxy.schulgliederung.value" :disabled="!hatKompetenzUpdate" :items="modelProxy.schulgliederungen.value" :item-text="getSelectText" />
					<svws-ui-text-input placeholder="Prüfungsordnung" :model-value="modelProxy.proxy.pruefungsordnung ?? '—'" @change="modelProxy.patch" disabled />
					<svws-ui-select v-if="schuleState.schulform.istAllgemeinbildend() || schuleState.schulform.istWeiterbildung()" title="Klassenart" v-model="modelProxy.klassenart.value" :disabled="!hatKompetenzUpdate" :items="modelProxy.klassenarten.value" :item-text="getSelectText" />
					<svws-ui-select v-if="schuleState.schulform.istAllgemeinbildend()" title="Organisationsform" v-model="modelProxy.organisationsformAllgemeinbildend.value" :disabled="!hatKompetenzUpdate" :items="modelProxy.organisationsformenAllgemeinbildend.value" :item-text="getSelectText" />
					<svws-ui-select v-if="schuleState.schulform.istBerufsbildend()" title="Organisationsform" v-model="modelProxy.organisationsformBerufsbildend.value" :disabled="!hatKompetenzUpdate" :items="modelProxy.organisationsformenBerufsbildend.value" :item-text="getSelectText" />
					<svws-ui-select v-if="schuleState.schulform.istWeiterbildung()" title="Organisationsform" v-model="modelProxy.organisationsformWeiterbildend.value" :disabled="!hatKompetenzUpdate" :items="modelProxy.organisationsformenWeiterbildend.value" :item-text="getSelectText" />
				</svws-ui-input-wrapper>

				<svws-ui-spacing :size="2" />

				<svws-ui-input-wrapper :grid="1">
					<svws-ui-checkbox v-model="modelProxy.proxy.noteneingabeGesperrt" :disabled="!hatKompetenzUpdate"> Noteneingabe gesperrt </svws-ui-checkbox>
					<svws-ui-checkbox v-if="schuleState.schulform === Schulform.G" :disabled="!hatKompetenzUpdate" v-model="modelProxy.proxy.verwendungAnkreuzkompetenzen">In dieser Klasse werden Ankreuzkompetenzen verwendet </svws-ui-checkbox>
					<svws-ui-checkbox v-if="schuleState.schulform === Schulform.WB" :disabled="!hatKompetenzUpdate" v-model="modelProxy.proxy.beginnSommersemester"> Beginn im Sommersemester </svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Klassenleitung">
				<svws-ui-table :columns="columnsKlassenleitungen" :items="listeKlassenlehrer" :clickable="hatKompetenzUpdate" :clicked="klassenleitungClicked" @update:clicked="setKlassenleitungClicked">
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
								<svws-ui-button type="icon" @click.stop="removeKlassenleitungHandler(rowData)">
									<span class="icon i-ri-delete-bin-line" />
								</svws-ui-button>
							</div>
						</div>
					</template>
					<template #footer v-if="hatKompetenzUpdate">
						<div style="vertical-align: center; display: flex; float: right; margin-right: 5.7pt">
							<div v-if="manager().getAuswahlKlassenLeitung() !== null" class="w-6 me-1">
								<svws-ui-button v-if="showPfeilHoch" type="icon" @click="erhoeheReihenfolge">
									<span class="icon i-ri-arrow-up-line" />
								</svws-ui-button>
								<svws-ui-button v-else-if="showPfeilRunter" type="icon" @click="reduziereReihenfolge">
									<span class="icon i-ri-arrow-down-line" />
								</svws-ui-button>
							</div>
							<div style="display: flex; justify-content: flex-end">
								<klassen-daten-lehrer-zuweisung-modal v-slot="{openModal}" :manager :add-klassenleitung>
									<svws-ui-button type="icon" @click="openModal"> <span class="icon i-ri-add-line" /> </svws-ui-button>
								</klassen-daten-lehrer-zuweisung-modal>
							</div>
						</div>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Klassenliste">
			<svws-ui-multi-select v-model="filterSchuelerStatus" title="Status" :items="manager().schuelerstatus.list()" :item-text="status => status.daten(schuljahr)?.text ?? '—'" class="col-span-full" />
			<svws-ui-spacing />
			<svws-ui-table :columns="colsSchueler" :items="manager().getSchuelerListe()" count>
				<template #cell(status)="{ value } : { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.data().getWertByID(value)?.daten(schuljahr)?.text ?? "—" }}</span>
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

	import { computed, ref, watch } from "vue";
	import type { KlassenDatenProps } from "./KlassenDatenProps";
	import { KlassenDatenModelProxy } from "../KlassenDatenModelProxy";
	import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
	import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
	import type { Klassenart } from "@core/asd/types/klassen/Klassenart";
	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import type { AllgemeinbildendOrganisationsformen } from "@core/asd/types/schule/AllgemeinbildendOrganisationsformen";
	import type { BerufskollegOrganisationsformen } from "@core/asd/types/schule/BerufskollegOrganisationsformen";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { WeiterbildungskollegOrganisationsformen } from "@core/asd/types/schule/WeiterbildungskollegOrganisationsformen";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import type { DataTableColumn } from "@ui/types";

	const props = defineProps<KlassenDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const abschnittState = useAbschnittState();

	const listAndereKlassen = computed(() => {
		const arr = [];
		for (const k of props.manager().liste.list()) {
			if (k.id !== props.manager().auswahlID()) {
				arr.push(k);
			}
		}
		return arr;
	});

	const dataNotPatched = () => props.manager().daten();
	const listOfAutopatchProps: Iterable<keyof KlassenDaten> = ["idJahrgang", "parallelitaet", "idVorgaengerklasse",
		"idFolgeklasse", "idSchulgliederung", "idKlassenart", "idBerufsbildendOrganisationsform", "idAllgemeinbildendOrganisationsform",
		"idWeiterbildungOrganisationsform", "noteneingabeGesperrt", "verwendungAnkreuzkompetenzen", "beginnSommersemester"];
	const modelProxy = new KlassenDatenModelProxy(
		() => dataNotPatched(),
		() => listAndereKlassen.value,
		props.manager,
		listOfAutopatchProps,
		props.patch
	);

	watch(() => props.manager().daten().klassenLeitungen, () => {
		klassenleitungClicked.value = null;
	});

	const schuljahr = computed<number>(() => schuleState.schuljahr);

	function getSelectText(value: Klassenart | Schulgliederung | AllgemeinbildendOrganisationsformen | BerufskollegOrganisationsformen | WeiterbildungskollegOrganisationsformen) {
		return value.daten(schuleState.schuljahr)?.kuerzel + ' - ' + value.daten(schuleState.schuljahr)?.text;
	}

	function getSelectTextJahrgang(jg: JahrgangsDaten): string {
		switch (jg.kuerzel) {
			case null:
				return 'JU - Jahrgangsübergreifend';
			case 'E1':
				return '1E' + ' - ' + jg.bezeichnung;
			case 'E2':
				return '2E' + ' - ' + jg.bezeichnung;
			default:
				return jg.kuerzel + ' - ' + jg.bezeichnung;
		}
	}

	// TODO auch UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN verwenden und hier unterscheiden zu UNTERRICHTSVERTEILUNG_ANSEHEN
	const hatKompetenzAnsehen = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	// TODO auch UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN berücksichtigen in Bezug auf Abteilungsleitungen / Koordinationen (API muss dafür noch erweitert werden)
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const klassenleitungClicked = ref<LehrerListeEintrag | null>(null);

	function setKlassenleitungClicked(value: LehrerListeEintrag | null) {
		props.manager().setAuswahlKlassenLeitung(value);
		klassenleitungClicked.value = value;
	}

	const ersteKlassenleitungId = computed<number | undefined>(() =>
		listeKlassenlehrer.value.length > 0 ? listeKlassenlehrer.value[0].id : undefined
	);

	const letzteKlassenleitungId = computed<number | undefined>(() =>
		listeKlassenlehrer.value.length > 0 ? listeKlassenlehrer.value[modelProxy.proxy.klassenLeitungen.size() - 1].id : undefined
	);

	const showPfeilHoch = computed<boolean>(() => {
		if (!klassenleitungClicked.value) {
			return false;
		}

		return (listeKlassenlehrer.value.length > 0)
			&& (klassenleitungClicked.value.id !== ersteKlassenleitungId.value)
			&& modelProxy.proxy.klassenLeitungen.contains(klassenleitungClicked.value.id);
	});

	const showPfeilRunter = computed<boolean>(() => {
		if (!klassenleitungClicked.value) {
			return false;
		}

		return (listeKlassenlehrer.value.length > 0)
			&& (klassenleitungClicked.value.id !== letzteKlassenleitungId.value)
			&& modelProxy.proxy.klassenLeitungen.contains(klassenleitungClicked.value.id);
	});

	const jgWBK = new Set<Jahrgaenge>([
		Jahrgaenge.VORKURS_SEMESTER_1, Jahrgaenge.VORKURS_SEMESTER_2,
		Jahrgaenge.SEMESTER_01, Jahrgaenge.SEMESTER_02, Jahrgaenge.SEMESTER_03, Jahrgaenge.SEMESTER_04, Jahrgaenge.SEMESTER_05, Jahrgaenge.SEMESTER_06,
		Jahrgaenge.REALSCHULE_VORKURS_SEMESTER_1, Jahrgaenge.REALSCHULE_VORKURS_SEMESTER_2,
		Jahrgaenge.REALSCHULE_SEMESTER_01, Jahrgaenge.REALSCHULE_SEMESTER_02, Jahrgaenge.REALSCHULE_SEMESTER_03, Jahrgaenge.REALSCHULE_SEMESTER_04,
	]);

	function istSemesterBetrieb(): boolean {
		if (schuleState.schulform === Schulform.WB) {
			return true;
		}
		const jgdaten = modelProxy.jahrgang.value;
		if ((jgdaten === null) || (jgdaten.idJahrgang === null)) {
			return false;
		}
		const jg = Jahrgaenge.data().getWertByIDOrNull(jgdaten.idJahrgang);
		if (jg === null) {
			return false;
		}
		return jgWBK.has(jg);
	}

	function zeigeVorgaengerklassen(): boolean {
		if (modelProxy.listeVorgaengerklassen.value.isEmpty()) {
			return false;
		}
		if (istSemesterBetrieb()) {
			return true;
		}
		const sja = abschnittState.auswahl;
		return (sja.abschnitt === 1);
	}

	function zeigeFolgeklassen(): boolean {
		if (modelProxy.listeFolgeklassen.value.isEmpty()) {
			return false;
		}
		if (istSemesterBetrieb()) {
			return true;
		}
		const sja = abschnittState.auswahl;
		return (sja.abschnitt === 2);
	}

	async function removeKlassenleitungHandler(rowData: LehrerListeEintrag): Promise<void> {
		await props.removeKlassenleitung(rowData);
		if ((klassenleitungClicked.value !== null) && (klassenleitungClicked.value.id === rowData.id)) {
			klassenleitungClicked.value = null;
		}
	}

	async function erhoeheReihenfolge(): Promise<void> {
		if (!klassenleitungClicked.value) {
			return;
		}
		await props.updateReihenfolgeKlassenleitung(klassenleitungClicked.value.id, true);
	}

	async function reduziereReihenfolge(): Promise<void> {
		if (!klassenleitungClicked.value) {
			return;
		}
		await props.updateReihenfolgeKlassenleitung(klassenleitungClicked.value.id, false);
	}

	const filterSchuelerStatus = computed<SchuelerStatus[]>({
		get: () => [...props.manager().schuelerstatus.auswahl()],
		set: (value) => {
			props.manager().schuelerstatus.auswahlClear();
			for (const v of value) {
				props.manager().schuelerstatus.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	const listeKlassenlehrer = computed<LehrerListeEintrag[]>(() => {
		const a: LehrerListeEintrag[] = [];
		for (const klassenLeitung of props.manager().daten().klassenLeitungen) {
			const lehrer: LehrerListeEintrag | null = props.manager().lehrer.get(klassenLeitung);
			if (lehrer !== null) {
				a.push(lehrer);
			}
		}
		return a;
	});

	const columnsKlassenleitungen = computed<Array<DataTableColumn>>(() => {
		const result = new Array<DataTableColumn>();
		result.push(
			{ key: "linkToLehrer", label: " ", fixedWidth: 1.75, align: "center" },
			{ key: "kuerzel", label: "Kürzel", span: 1, sortable: false, statistic: true },
			{ key: "nachname", label: "Nachname", span: 2, sortable: false },
			{ key: "vorname", label: "Rufname", span: 2, sortable: false });
		if (hatKompetenzUpdate.value) {
			result.push({ key: "aktionen", label: "", span: 2, sortable: false, align: "right" });
		}
		return result;
	});


	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Rufname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 },
	];

</script>
