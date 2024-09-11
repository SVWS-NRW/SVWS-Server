<template>
	<div class="content">
		<svws-ui-table :columns="cols" :items="undefined" has-background class="col-span-2 -mt-1">
			<template #header>
				<div role="row" class="svws-ui-tr">
					<div role="columnheader" :class="{ 'col-span-5': hatUpdateKompetenz, 'col-span-4': !hatUpdateKompetenz }" aria-label="Fach" />
					<div role="columnheader" class="svws-ui-td svws-align-center col-span-2" aria-label="Noten"> Noten </div>
				</div>
				<div role="row" class="svws-ui-tr">
					<div v-if="hatUpdateKompetenz" role="columnheader" class="svws-ui-td svws-align-center" aria-label="Alle auswählen">
						<svws-ui-checkbox :model-value="leistungen.size() === auswahl.size" :indeterminate="someSelected()" @update:model-value="updateAuswahl" headless />
					</div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Fach"> Fach </div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Kurs"> Kurs </div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Kursart"> Kursart </div>
					<div role="columnheader" class="svws-ui-td svws-align-left" aria-label="Lehrer"> Lehrer </div>
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Quartalsnote"> Quartal </div>
					<div role="columnheader" class="svws-ui-td svws-align-center" aria-label="Halbjahresnote"> Halbjahr </div>
				</div>
			</template>
			<template #body="">
				<template v-for="leistung in leistungen" :key="leistung.id">
					<div v-if="leistung.id !== null" class="svws-ui-tr" role="row" :style="{ '--background-color': manager().fachFarbeGetByLeistungsIdOrDefault(leistung.id) }">
						<div v-if="hatUpdateKompetenz" class="svws-ui-td svws-align-center cursor-pointer" role="cell">
							<svws-ui-checkbox :model-value="auswahl.has(leistung)" @update:model-value="auswahl.has(leistung) ? auswahl.delete(leistung) : auswahl.add(leistung)" headless />
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select v-if="hatUpdateKompetenz" title="—" :items="props.manager().fachGetMenge()" :item-text="fach => ((fach === null) || (fach.bezeichnung === null)) ? '—' : fach.bezeichnung"
								:model-value="manager().fachGetByLeistungIdOrException(leistung.id)"
								@update:model-value="(value : FachDaten | null) => void patchFach(value, leistung)"
								class="w-full" headless use-null />
							<div v-else>{{ manager().fachGetByLeistungIdOrException(leistung.id)?.bezeichnung ?? '—' }}</div>
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select v-if="hatUpdateKompetenz" title="—" :items="manager().kursGetMengeFilteredByLeistung(leistung.id)" :item-text="kurs => (kurs === null) ? '—' : kurs.kuerzel"
								:model-value="manager().kursGetByLeistungIdOrNull(leistung.id)"
								@update:model-value="(value : KursDaten | null) => void patchKurs(value, leistung)"
								class="w-full" headless removable use-null />
							<div v-else>{{ manager().kursGetByLeistungIdOrNull(leistung.id)?.kuerzel ?? '—' }}</div>
						</div>
						<div class="svws-ui-td" role="cell">
							<!-- TODO In Gesamtschulen kann bei Klassenunterricht neben PUK noch E oder G als Kursart vorkommen -->
							<template v-if="(manager().kursGetByLeistungIdOrNull(leistung.id) === null) || ZulaessigeKursart.getByAllgemeinerKursart(schuljahr, manager().kursGetByLeistungIdOrNull(leistung.id)!.kursartAllg).size() === 1">
								<span>{{ leistung.kursart }}</span>
							</template>
							<template v-else>
								<svws-ui-select v-if="hatUpdateKompetenz" title="—" :items="ZulaessigeKursart.getByAllgemeinerKursart(schuljahr, manager().kursGetByLeistungIdOrNull(leistung.id)!.kursartAllg)" :item-text="zk => zk.daten(schuljahr)?.kuerzel ?? '—'"
									:model-value="(leistung.kursart === null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart)"
									@update:model-value="value => patchLeistung({ kursart: value?.daten(schuljahr)?.kuerzel ?? null }, leistung.id)"
									class="w-full" headless />
								<div v-else>{{ (leistung.kursart === null) ? null : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
							</template>
						</div>
						<div class="svws-ui-td svws-divider" role="cell">
							<svws-ui-select v-if="hatUpdateKompetenz" title="—" :items="manager().lehrerGetMenge()" :item-text="lehrer => textLehrer(lehrer)"
								:model-value="manager().lehrerGetByLeistungIdOrNull(leistung.id)"
								@update:model-value="value => patchLeistung({ lehrerID: ((value === null) || (value === undefined)) ? null : value.id }, leistung.id)"
								class="w-full" headless />
							<div v-else>{{ textLehrer(manager().lehrerGetByLeistungIdOrNull(leistung.id)) }}</div>
						</div>
						<div class="svws-ui-td svws-divider" role="cell">
							<svws-ui-select v-if="hatFachlehrerKompetenz(leistung.lehrerID)" title="—" :items="Note.values()" :item-text="(item: Note) => item?.daten(schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.noteQuartal)"
								@update:model-value="value => patchLeistung({ noteQuartal: value?.daten(schuljahr)?.kuerzel ?? null }, leistung.id)"
								headless class="w-full" />
							<div v-else>{{ Note.fromKuerzel(leistung.noteQuartal).daten(schuljahr)?.kuerzel }}</div>
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-select v-if="hatFachlehrerKompetenz(leistung.lehrerID)" title="—" :items="Note.values()" :item-text="(item: Note) => item?.daten(schuljahr)?.kuerzel ?? '—'"
								:model-value="Note.fromKuerzel(leistung.note)"
								@update:model-value="value => patchLeistung({ note: value?.daten(schuljahr)?.kuerzel ?? null }, leistung.id)"
								headless class="w-full" />
							<div v-else>{{ Note.fromKuerzel(leistung.note).daten(schuljahr)?.kuerzel }}</div>
						</div>
					</div>
				</template>
			</template>
			<template v-if="hatUpdateKompetenz" #footer>
				<div class="svws-ui-tr flex flex-row" role="row">
					<div class="svws-ui-td" role="cell" v-if="auswahl.size > 0">
						{{ auswahl.size }} ausgewählt
					</div>
					<div class="svws-ui-td flex-grow justify-end" role="cell">
						<svws-ui-button @click="deleteAuswahl()" type="trash" :disabled="auswahl.size === 0" />
						<svws-ui-button v-if="props.manager().fachGetMenge().size() > 0" @click="addLeistung(props.manager().fachGetMenge().get(0).id)" type="icon" title="Neue Leistungsdaten hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</div>
				</div>
			</template>
		</svws-ui-table>
		<svws-ui-spacing :size="2" />
		<svws-ui-content-card>
			<svws-ui-input-wrapper :grid="2" v-if="hatLernbereichsnote">
				<span class="font-bold col-span-full">Lernbereichsnoten</span>
				<svws-ui-select v-if="hatUpdateKompetenz" title="Gesellschaftswissenschaft" :items="getLernbereichsnoten()" :item-text="i => `${i.daten(schuljahr)?.kuerzel}`" autocomplete v-model="lernbereichsnoteGSbzwAL" />
				<div v-else>{{ lernbereichsnoteGSbzwAL?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				<svws-ui-select v-if="hatUpdateKompetenz" title="Naturwissenschaft" :items="getLernbereichsnoten()" :item-text="i => `${i.daten(schuljahr)?.kuerzel}`" autocomplete v-model="lernbereichsnoteNW" />
				<div v-else>{{ lernbereichsnoteNW?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" v-if="hatLernbereichsnote" />
			<svws-ui-input-wrapper class="col-span-full items-center" :grid="4">
				<span class="font-bold col-span-full">Fehlstunden (Summe)</span>
				<svws-ui-input-number :disabled="!hatUpdateKompetenz" placeholder="Maximal" :min="0" :model-value="manager().lernabschnittGet().fehlstundenGrenzwert"
					@change="fehlstundenGrenzwert => patch({ fehlstundenGrenzwert })" />
				<svws-ui-input-number :disabled="!hatUpdateKompetenz" placeholder="Gesamt" :min="0" :model-value="manager().lernabschnittGet().fehlstundenGesamt"
					@change="fehlstundenGesamt => patch({ fehlstundenGesamt: fehlstundenGesamt ?? undefined })" />
				<svws-ui-input-number :disabled="!hatUpdateKompetenz" placeholder="Unentschuldigt" :min="0" :model-value="manager().lernabschnittGet().fehlstundenUnentschuldigt"
					@change="fehlstundenUnentschuldigt => patch({ fehlstundenUnentschuldigt: fehlstundenUnentschuldigt ?? undefined })" />
			</svws-ui-input-wrapper>
			<!-- <svws-ui-todo title="Fehlzeiten" class="mt-10">
				Hier könnte demnächst die Übersicht über die Fehlzeiten implementiert werden.
			</svws-ui-todo> -->
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { SchuelerLernabschnittLeistungenProps } from "./SSchuelerLernabschnittLeistungenProps";
	import type { SchuelerLeistungsdaten, List, KursDaten, FachDaten, LehrerListeEintrag} from "@core";
	import { Note, ZulaessigeKursart, ArrayList, Fach, BenutzerKompetenz, BenutzerTyp } from "@core";

	const props = defineProps<SchuelerLernabschnittLeistungenProps>();

	const cols = computed(() => {
		const result = [];
		if (hatUpdateKompetenz.value)
			result.push({ key: "auswahl", label: "Auswahl", fixedWidth: 1.5 });
		result.push({ key: "fachID", label: "Fach", span: 3, sortable: false, minWidth: 10 });
		result.push({ key: "kursID", label: "Kurs", span: 1, sortable: false, minWidth: 10 });
		result.push({ key: "kursart", label: "Kursart", span: 1, sortable: false, fixedWidth: 6 });
		result.push({ key: "lehrerID", label: "Lehrer", span: 2, sortable: false, minWidth: 10 });
		result.push({ key: "noteQuartal", label: "Quartalsnote", tooltip: "Quartalsnote", sortable: false, fixedWidth: 5 });
		result.push({ key: "note", label: "Note", sortable: false, fixedWidth: 5 });
		return result;
	});

	/**
	 * Gibt an, ob der angemeldete Benutzer eine Kompetenz, ggf. auch eine funktionsbezogene, zum Anpassen
	 * der Leistungsdaten hat oder nicht. Dabei werden Fachlehrer-Kompetenzen auf spezielle Leistungsdaten
	 * nicht mit einbezogen.
	 */
	const hatUpdateKompetenz = computed<boolean>(() => {
		return (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))
			|| ((props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN))
				&& props.benutzerKompetenzenKlassen.has(props.schuelerListeManager().auswahl().idKlasse));
	});

	/// Das Schuljahr der Lernabschnittsdaten
	const schuljahr = computed<number>(() => props.manager().schuljahrGet());

	/**
	 * Prüft, ob der angemeldete Benutzer eine Kompetenz zum Ändern von Leistungsdaten in Bezug
	 * auf Noten und Fehlstunden hat. Dabei werden Fachlehrer-Kompetenzen beachtet.
	 *
	 * @param idFachlehrer   die ID des Fachlehrers
	 */
	function hatFachlehrerKompetenz(idFachlehrer : number | null) : boolean {
		// Prüfe, ob der Benutzer sowieso eine übergeordnete funktionsbezogene Kompetenz hat
		if (hatUpdateKompetenz.value)
			return true;
		// Prüfe, ob es sich um das aktuelle Schuljahr handelt. Wenn nicht, so hat ein Fachlehrer keine besonderen Kompetenzen
		if (props.schule.idSchuljahresabschnitt !== props.manager().lernabschnittGet().schuljahresabschnitt)
			return false;
		// Prüfe, ob der aktuelle Benutzer der Fachlehrer mit der übergebenen ID ist
		return (props.benutzerdaten.typ === BenutzerTyp.LEHRER.id) && (props.benutzerdaten.typID === idFachlehrer);
	}

	const leistungen = computed<List<SchuelerLeistungsdaten>>(() => {
		return props.manager().leistungGetMengeAsListSortedByFach();
	});

	watch(leistungen, (newLeistungen, oldLeistungen) => {
		if (newLeistungen.size() === oldLeistungen.size()) {
			const tmpSetIDs = new Set<number>();
			for (const l of oldLeistungen)
				tmpSetIDs.add(l.id);
			let changed : boolean = false;
			for (const l of newLeistungen) {
				if (!tmpSetIDs.has(l.id)) {
					changed = true;
					break;
				}
			}
			if (!changed)
				return;
		}
		auswahl.value.clear();
	});

	const auswahl = ref<Set<SchuelerLeistungsdaten>>(new Set());

	function textLehrer(lehrer : LehrerListeEintrag | null) : string {
		if (lehrer === null)
			return '—';
		return lehrer.kuerzel + ' (' + lehrer.nachname + ', ' + lehrer.vorname + ')';
	}

	function updateAuswahl() {
		const allSelected = (leistungen.value.size() === auswahl.value.size);
		if (allSelected) {
			auswahl.value.clear();
		} else {
			for (const leistung of leistungen.value)
				auswahl.value.add(leistung);
		}
	}

	function someSelected() : boolean {
		return (auswahl.value.size > 0) && (auswahl.value.size < leistungen.value.size());
	}

	const deleteAuswahl = async () => {
		if (auswahl.value.size === 0)
			return;
		const leistungenIDs = new ArrayList<number>();
		for (const leistung of auswahl.value)
			leistungenIDs.add(leistung.id);
		await props.deleteLeistungen(leistungenIDs);
	};

	function getLernbereichsnoten() : Note[] {
		return [ Note.KEINE, Note.SEHR_GUT, Note.GUT, Note.BEFRIEDIGEND, Note.AUSREICHEND, Note.MANGELHAFT, Note.UNGENUEGEND ];
	}

	const lernbereichsnoteGSbzwAL = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(props.manager().lernabschnittGet().noteLernbereichGSbzwAL);
			return note === null ? undefined : note;
		},
		set: (value) => void props.patch({ noteLernbereichGSbzwAL: value === undefined || value === Note.KEINE ? null : value.getNoteSekI(schuljahr.value) })
	});

	const lernbereichsnoteNW = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(props.manager().lernabschnittGet().noteLernbereichNW);
			return note === null ? undefined : note;
		},
		set: (value) => void props.patch({ noteLernbereichNW: value === undefined || value === Note.KEINE ? null : value.getNoteSekI(schuljahr.value) })
	});

	async function patchFach(fach: FachDaten | null, leistung: SchuelerLeistungsdaten) {
		// Fach-Eintrag bei den Leistungsdaten wird entfernt
		if (fach === null) {
			await props.patchLeistung({ fachID: -1, kursID: null }, leistung.id);
			return;
		}
		// Spezialfälle
		const f : Fach = Fach.getBySchluesselOrDefault(fach.kuerzelStatistik);
		if (f === Fach.VX) { // Speziallfall Gymnasiale Oberstufe - Vertiefungsfach
			await props.patchLeistung({ fachID: fach.id, kursID: null, kursart: ZulaessigeKursart.VTF.daten(schuljahr.value)?.kuerzel }, leistung.id);
		} else if (f === Fach.PX) { // Speziallfall Gymnasiale Oberstufe - Projektkursfach
			await props.patchLeistung({ fachID: fach.id, kursID: null, kursart: ZulaessigeKursart.PJK.daten(schuljahr.value)?.kuerzel }, leistung.id);
		} else { // Allgemeiner Fall: Entfernen des Kurses und setzen einer speziellen Kursart, wenn die kursart der Leistung null ist
			let kursart = (leistung.kursart === null) ? null : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart);
			if (kursart === null)
				kursart = ZulaessigeKursart.PUK;
			await props.patchLeistung({ fachID: fach.id, kursID: null, kursart: kursart.daten(schuljahr.value)?.kuerzel }, leistung.id);
		}
	}

	async function patchKurs(kurs: KursDaten | null, leistung: SchuelerLeistungsdaten) {
		if (kurs === null) {
			await props.patchLeistung({ kursID: null, kursart: ZulaessigeKursart.PUK.daten(schuljahr.value)?.kuerzel, abifach: null }, leistung.id);
			return;
		}
		const kursart = (leistung.kursart === null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart);
		if (kurs.kursartAllg !== kursart?.daten(schuljahr.value)?.kuerzelAllg) {
			const kursarten : List<ZulaessigeKursart> = ZulaessigeKursart.getByAllgemeinerKursart(schuljahr.value, kurs.kursartAllg);
			let neueKursart : ZulaessigeKursart | null = kursart;
			let neuesAbifach : number | null = leistung.abifach;
			if (kurs.kursartAllg === ZulaessigeKursart.E.daten(schuljahr.value)?.kuerzel) { // Speziallfall Gesamtschule E-Kurs
				neueKursart = ZulaessigeKursart.E;
			} else if (kurs.kursartAllg === ZulaessigeKursart.G.daten(schuljahr.value)?.kuerzel) { // Speziallfall Gesamtschule G-Kurs
				neueKursart = ZulaessigeKursart.G;
			} else if (kurs.kursartAllg === ZulaessigeKursart.E.daten(schuljahr.value)?.kuerzelAllg) { // Spezialfall Gesamtschule DK-Kurs -> nehme G als Default
				neueKursart = ZulaessigeKursart.G;
			} else if (kurs.kursartAllg === ZulaessigeKursart.GKM.daten(schuljahr.value)?.kuerzelAllg) { // Spezialfall Gymnasiale Oberstufe GK -> Berücksichtige Abiturfach, Default GKM
				neueKursart = ZulaessigeKursart.GKM;
				if ((leistung.abifach === 1) || (leistung.abifach === 2))
					neuesAbifach = null;
				if (leistung.abifach === 3)
					neueKursart = ZulaessigeKursart.AB3;
				else if (leistung.abifach === 4)
					neueKursart = ZulaessigeKursart.AB4;
			} else if (kurs.kursartAllg === ZulaessigeKursart.LK1.daten(schuljahr.value)?.kuerzelAllg) { // Spezialfall Gymnasiale Oberstufe LK -> Berücksichtige Abiturfach, Default LK1
				// TODO Prüfen, ob das Fach für LK1 zulässig ist -> wenn nicht immer LK2, ansonsten prüfen, ob LK1 bereits bei den Lernabschnittsdaten zugeordnet ist und LK2 nicht. Ist dies der Fall -> LK2, sonst LK1
				neueKursart = ZulaessigeKursart.LK1;
				if (leistung.abifach === 2)
					neueKursart = ZulaessigeKursart.LK2;
				if (leistung.abifach === null)
					neuesAbifach = 1;
			} else {
				neueKursart = kursarten.isEmpty() ? null : kursarten.get(0);
			}
			await props.patchLeistung({
				kursID: kurs.id,
				lehrerID: kurs.lehrer,
				kursart: neueKursart?.daten(schuljahr.value)?.kuerzel ?? null,
				abifach: neuesAbifach,
			}, leistung.id);
		} else {
			await props.patchLeistung({ kursID: kurs.id, lehrerID: kurs.lehrer }, leistung.id);
		}
	}

	const lernbereichsnote1Bezeichnung = computed<string | null>(() => props.manager().lernabschnittGetLernbereichsnote1Bezeichnung());
	const lernbereichsnote2Bezeichnung = computed<string | null>(() => props.manager().lernabschnittGetLernbereichsnote2Bezeichnung());
	const hatLernbereichsnote = computed<boolean>(() => (lernbereichsnote1Bezeichnung.value !== null) || lernbereichsnote2Bezeichnung.value !== null);

</script>

<style lang="postcss" scoped>

	.content {
		@apply w-full h-full grid grid-cols-2;
	}

</style>

