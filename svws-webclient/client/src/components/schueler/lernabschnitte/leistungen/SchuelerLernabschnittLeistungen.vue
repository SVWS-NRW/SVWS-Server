<template>
	<div class="w-340">
		<ui-table-grid name="Leistungen" :manager="() => gridManager" :header-count>
			<template #header="{ i }">
				<template v-if="(headerCount === 2) && (i === 1)">
					<div class="col-span-7" />
					<th scope="col" class="flex items-center justify-center col-span-2">
						Noten
					</th>
				</template>
				<template v-if="((headerCount === 1) && (i === 1)) || ((headerCount === 2) && (i === 2))">
					<template v-for="col of gridManager.cols.values()" :key="col.kuerzel">
						<template v-if="col.kuerzel === 'auswahl'">
							<th scope="col" class="flex items-start justify-center" aria-label="Alle auswählen">
								<svws-ui-checkbox :model-value="(leistungen.size() === selectedLeistungen.size) && !leistungen.isEmpty()"
									@update:model-value="updateAuswahl"
									:indeterminate="someSelected"
									headless :autofocus="!leistungen.isEmpty()" />
							</th>
						</template>
						<template v-else-if="col.kuerzel === 'abifach'">
							<th v-if="istGymOb" scope="col" class="flex items-center justify-center" :aria-label="col.name">
								{{ col.name }}
							</th>
						</template>
						<template v-else-if="col.kuerzel === 'kursart' || col.kuerzel === 'wochenstunden' || col.kuerzel === 'note' || col.kuerzel === 'noteQuartal'">
							<th scope="col" class="flex items-center justify-center" :aria-label="col.name">
								{{ col.name }}
							</th>
						</template>
						<template v-else>
							<th scope="col" class="flex items-start justify-center" :aria-label="col.name">
								{{ col.name }}
							</th>
						</template>
					</template>
				</template>
			</template>

			<template #default="{ row: leistung }">
				<td class="cursor-pointer flex items-center justify-center"
					:style="getCellStyle(leistung)">
					<svws-ui-checkbox :model-value="selectedLeistungen.has(leistung)"
						@update:model-value="selectedLeistungen.has(leistung) ? selectedLeistungen.delete(leistung) : selectedLeistungen.add(leistung)"
						headless />
				</td>
				<td :style="getCellStyle(leistung)">
					<ui-select title="Fach"
						:manager="fachSelectManager"
						:model-value="manager().fachGetByLeistungIdOrException(leistung.id)"
						@update:model-value="(value : FachDaten | null) => void patchFach(value, leistung)"
						headless :removable="false" :readonly="!hatUpdateKompetenz" />
				</td>
				<td :style="getCellStyle(leistung)">
					<ui-select title="Kurs"
						:manager="getKursSelectManagerByIdLeistung(leistung.id)"
						:model-value="manager().kursGetByLeistungIdOrNull(leistung.id)"
						@update:model-value="(value : KursDaten | undefined) => void patchKurs(value, leistung)"
						headless :readonly="!hatUpdateKompetenz" />
				</td>
				<td :style="getCellStyle(leistung)">
					<!-- TODO In Gesamtschulen kann bei Klassenunterricht neben PUK noch E oder G als Kursart vorkommen -->
					<span v-if="(manager().kursGetByLeistungIdOrNull(leistung.id) === null)
						|| ZulaessigeKursart.getByAllgemeinerKursart(schuljahr, manager().kursGetByLeistungIdOrNull(leistung.id)!.kursartAllg).size() === 1">
						{{ leistung.kursart }}
					</span>
					<ui-select v-else title="Kursart"
						:manager="getKursartSelectManagerByIdLeistung(leistung.id)"
						:model-value="(leistung.kursart === null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart)"
						@update:model-value="value => patchLeistung({ kursart: value?.daten(schuljahr)?.kuerzel ?? null }, leistung.id)"
						headless :removable="false" :readonly="!hatUpdateKompetenz" />
				</td>
				<template v-if="istGymOb">
					<td :style="getCellStyle(leistung)">
						{{ leistung.abifach ?? "" }}
					</td>
				</template>
				<td :style="getCellStyle(leistung)">
					{{ leistung.wochenstunden ?? "" }}
				</td>
				<td :style="getCellStyle(leistung)">
					<ui-select title="Lehrer"
						:manager="lehrerSelectManager"
						:items="manager().lehrerGetMengeAktiv()"
						:model-value="manager().lehrerGetByLeistungIdOrNull(leistung.id)"
						@update:model-value="value => patchLeistung({ lehrerID: value?.id ?? null }, leistung.id)"
						headless :removable="false" :readonly="!hatUpdateKompetenz" />
				</td>
				<td class="border-s" :style="getCellStyle(leistung)">
					<ui-select title="Quartalsnote"
						:manager="notenSelectManager"
						:model-value="Note.fromKuerzel(leistung.noteQuartal)"
						@update:model-value="value => patchLeistung({ noteQuartal: value?.daten(schuljahr)?.kuerzel ?? null }, leistung.id)"
						headless :removable="false" :readonly="!hatFachlehrerKompetenz(leistung.lehrerID)" />
				</td>
				<td class="border-s" :style="getCellStyle(leistung)">
					<ui-select title="Halbjahresnote"
						:manager="notenSelectManager"
						:model-value="Note.fromKuerzel(leistung.note)"
						@update:model-value="value => patchLeistung({ note: value?.daten(schuljahr)?.kuerzel ?? null }, leistung.id)"
						headless :removable="false" :readonly="!hatFachlehrerKompetenz(leistung.lehrerID)" />
				</td>
			</template>

			<template #footer>
				<div class="col-span-full flex justify-between items-center my-1 px-1 py-2 border-b">
					<div>{{ (selectedLeistungen.size > 0) ? (selectedLeistungen.size + " ausgewählt") : " " }}</div>
					<ui-table-actions v-if="hatUpdateKompetenz" :actions="tableBulkActions" :items="selectedLeistungen" always-visible />
				</div>
			</template>
		</ui-table-grid>

		<svws-ui-content-card class="min-h-fit grow">
			<svws-ui-input-wrapper :grid="2" v-if="false">
				<span class="font-bold col-span-full">Lernbereichsnoten</span>
				<ui-select title="Gesellschaftswissenschaft"
					v-model="lernbereichsnoteGSbzwAL"
					:manager="lernbereichsnotenSelectManager"
					:readonly="!hatUpdateKompetenz" />
				<ui-select title="Naturwissenschaft"
					v-model="lernbereichsnoteNW"
					:manager="lernbereichsnotenSelectManager"
					:readonly="!hatUpdateKompetenz" />
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" v-if="hatLernbereichsnote" />
			<svws-ui-input-wrapper class="col-span-full items-center" :grid="4">
				<span class="font-bold col-span-full">Fehlstunden (Summe)</span>
				<svws-ui-input-number placeholder="Maximal"
					:model-value="manager().lernabschnittGet().fehlstundenGrenzwert"
					@change="fehlstundenGrenzwert => patch({ fehlstundenGrenzwert })"
					:disabled="!hatUpdateKompetenz"
					:min="0" />
				<svws-ui-input-number placeholder="Gesamt"
					:model-value="manager().lernabschnittGet().fehlstundenGesamt"
					@change="fehlstundenGesamt => patch({ fehlstundenGesamt: fehlstundenGesamt ?? undefined })"
					:disabled="!hatUpdateKompetenz"
					:min="0" />
				<svws-ui-input-number placeholder="Unentschuldigt"
					:model-value="manager().lernabschnittGet().fehlstundenUnentschuldigt"
					@change="fehlstundenUnentschuldigt => patch({ fehlstundenUnentschuldigt: fehlstundenUnentschuldigt ?? undefined })"
					:disabled="!hatUpdateKompetenz"
					:min="0" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { SchuelerLernabschnittLeistungenProps } from "./SchuelerLernabschnittLeistungenProps";
	import type { SchuelerLeistungsdaten, List, KursDaten, FachDaten, LehrerListeEintrag } from "@core";
	import { Note, ZulaessigeKursart, ArrayList, Fach, BenutzerKompetenz, BenutzerTyp, Jahrgaenge } from "@core";
	import { GridManager, SelectManager, useBenutzerState, useSchuleState } from "@ui";

	const props = defineProps<SchuelerLernabschnittLeistungenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const selectedLeistungen = ref<Set<SchuelerLeistungsdaten>>(new Set());

	/// Das Schuljahr der ausgewählten Lernabschnittsdaten
	const schuljahr = computed<number>(() => props.manager().schuljahrGet());
	const leistungen = computed<List<SchuelerLeistungsdaten>>(() => props.manager().leistungGetMengeAsListSortedByFach());
	const istGymOb = computed<boolean>(() => Jahrgaenge.data().getWertBySchluessel(props.schuelerListeManager().auswahl().jahrgang)?.istGymOb() ?? false);
	const lernbereichsnote1Bezeichnung = computed<string | null>(() => props.manager().lernabschnittGetLernbereichsnote1Bezeichnung());
	const lernbereichsnote2Bezeichnung = computed<string | null>(() => props.manager().lernabschnittGetLernbereichsnote2Bezeichnung());
	const hatLernbereichsnote = computed<boolean>(() => (lernbereichsnote1Bezeichnung.value !== null) || (lernbereichsnote2Bezeichnung.value !== null));
	const hatLernabschnittFaecher = computed<boolean>(() => props.manager().fachGetMenge().size() > 0);
	const someSelected = computed<boolean>(() => (selectedLeistungen.value.size > 0) && (selectedLeistungen.value.size < leistungen.value.size()));

	const tableBulkActions = computed(() => {
		return [
			{
				label: "Selektierte Leistungsdaten entfernen",
				action: () => deleteAuswahl(),
				disabled: selectedLeistungen.value.size === 0,
				iconClasses: "i-ri-delete-bin-line icon-ui-danger",
			},
			{
				label: "Neue Leistungsdaten hinzufügen",
				action: () => props.addLeistung(props.manager().fachGetMenge().get(0).id),
				disabled: !hatLernabschnittFaecher.value,
				iconClasses: "i-ri-add-line",
			},
		];
	});

	const colsVisible = computed<Map<string, boolean | null>>({
		get: () => {
			const map = new Map();
			map.set("auswahl", hatUpdateKompetenz.value);
			map.set("abifach", istGymOb.value);
			return map;
		},
		set: (_) => {},
	});

	const headerCount = computed<number>(() => istGymOb.value ? 2 : 1);

	const gridManager = new GridManager<string, SchuelerLeistungsdaten, List<SchuelerLeistungsdaten>>({
		daten: leistungen,
		getRowKey: row => `${row.id}`,
		columns: [
			{ kuerzel: "auswahl", name: "Auswahl", width: "1.5rem" },
			{ kuerzel: "fachID", name: "Fach", width: "minmax(10rem, 2.5fr)" },
			{ kuerzel: "kursID", name: "Kurs", width: "minmax(10rem, 1fr)" },
			{ kuerzel: "kursart", name: "Kursart", width: "5rem" },
			{ kuerzel: "abifach", name: "Abitur", width: "5rem" },
			{ kuerzel: "wochenstunden", name: "WStd", width: "5rem" },
			{ kuerzel: "lehrerID", name: "Lehrer", width: "minmax(10rem, 2fr)" },
			{ kuerzel: "noteQuartal", name: "Quartal", width: "5rem" },
			{ kuerzel: "note", name: "Halbjahr", width: "5rem" },
		],
		colsVisible,
	});

	const getCellStyle = (leistung: SchuelerLeistungsdaten) => {
		return `color: var(--color-text-uistatic); background-color: ${props.manager().fachFarbeGetByLeistungsIdOrDefault(leistung.id)}`;
	};

	/**
	 * Gibt an, ob der angemeldete Benutzer eine Kompetenz, ggf. auch eine funktionsbezogene, zum Anpassen
	 * der Leistungsdaten hat oder nicht. Dabei werden Fachlehrer-Kompetenzen auf spezielle Leistungsdaten
	 * nicht mit einbezogen.
	 */
	const hatUpdateKompetenz = computed<boolean>(() => {
		// Wenn der Benutzer generelle Rechte hat Leistungsdaten zu ändern, dann ist hier keine weitere Prüfung nötig, er hat die allgemeine Update-Kompetenz
		if (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)) {
			return true;
		}
		// Wenn der Benutzer auch keine funktionsbezogenen Rechte hat, dann hat er keine allgemeine Update-Kompetenz
		if (!benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN)) {
			return false;
		}
		// Wenn er keine funktionsbezogenen Rechte auf die Klasse hat, dann hat er keine allgemeine Update-Kompetenz
		if (!benutzerState.kompetenzenKlasse.has(props.schuelerListeManager().auswahl().idKlasse)) {
			return false;
		}
		// Wenn der Lernabschnitt nicht der aktuelle der Schule ist oder in der Zukunft liegt, dann hat er keine allgemeine Update-Kompetenz
		const schuleSchuljahresabschnitt = schuleState.abschnitt;
		const leistungSchuljahresabschnitt = props.manager().schuljahresabschnittGet();
		return (schuleSchuljahresabschnitt.schuljahr < leistungSchuljahresabschnitt.schuljahr)
			|| ((schuleSchuljahresabschnitt.schuljahr === leistungSchuljahresabschnitt.schuljahr)
				&& (schuleSchuljahresabschnitt.abschnitt <= leistungSchuljahresabschnitt.abschnitt));
	});

	const lernbereichsnoteGSbzwAL = computed<Note | null>({
		get: () => Note.fromNoteSekI(props.manager().lernabschnittGet().noteLernbereichGSbzwAL),
		set: (value: Note | null) => {
			const noteLernbereichGSbzwAL = ((value === null) || (value === Note.KEINE)) ? null : value.getNoteSekI(schuljahr.value);
			void props.patch({ noteLernbereichGSbzwAL });
		},
	});

	const lernbereichsnoteNW = computed<Note | null>({
		get: () => Note.fromNoteSekI(props.manager().lernabschnittGet().noteLernbereichNW),
		set: (value: Note | null) => {
			const noteLernbereichNW = ((value === null) || (value === Note.KEINE)) ? null : value.getNoteSekI(schuljahr.value);
			void props.patch({ noteLernbereichNW });
		},
	});

	const lernbereichsnoten = computed<Note[]>(() => [Note.KEINE, Note.SEHR_GUT, Note.GUT, Note.BEFRIEDIGEND, Note.AUSREICHEND, Note.MANGELHAFT, Note.UNGENUEGEND]);
	const lehrer = computed<Iterable<LehrerListeEintrag>>(() => props.manager().lehrerGetMengeAktiv());
	const faecher = computed<Iterable<FachDaten>>(() => props.manager().fachGetMenge());

	const lernbereichsnotenSelectManager = new SelectManager({
		options: lernbereichsnoten,
		optionDisplayText: note => note.daten(schuljahr.value)?.kuerzel ?? '-',
		selectionDisplayText: note => note.daten(schuljahr.value)?.kuerzel ?? '-',
	});

	const notenSelectManager = new SelectManager({
		options: Note.values(),
		optionDisplayText: note => (note === Note.KEINE) ? '—' : note.daten(schuljahr.value)?.kuerzel ?? '-',
		selectionDisplayText: note => (note === Note.KEINE) ? '' : note.daten(schuljahr.value)?.kuerzel ?? '-',
	});

	const lehrerSelectManager = new SelectManager({
		options: lehrer,
		optionDisplayText: lehrer => lehrer.kuerzel + ' (' + lehrer.nachname + ', ' + lehrer.vorname + ')',
		selectionDisplayText: lehrer => lehrer.kuerzel + ' (' + lehrer.nachname + ', ' + lehrer.vorname + ')',
	});

	const fachSelectManager = new SelectManager({
		options: faecher,
		optionDisplayText: fach => fach.bezeichnung,
		selectionDisplayText: fach => fach.bezeichnung,
	});

	const getKursartSelectManagerByIdLeistung = (idLeistung: number) => {
		return new SelectManager({
			options: ZulaessigeKursart.getByAllgemeinerKursart(schuljahr.value, props.manager().kursGetByLeistungIdOrNull(idLeistung)!.kursartAllg),
			optionDisplayText: kursart => kursart.daten(schuljahr.value)?.kuerzel ?? '—',
			selectionDisplayText: kursart => kursart.daten(schuljahr.value)?.kuerzel ?? '—',
		});
	};

	const getKursSelectManagerByIdLeistung = (idLeistung: number) => {
		return new SelectManager({
			options: props.manager().kursGetMengeFilteredByLeistung(idLeistung),
			optionDisplayText: kurs => kurs.kuerzel,
			selectionDisplayText: kurs => kurs.kuerzel,
		});
	};

	/**
	 * Prüft, ob der angemeldete Benutzer eine Kompetenz zum Ändern von Leistungsdaten in Bezug
	 * auf Noten und Fehlstunden hat. Dabei werden Fachlehrer-Kompetenzen beachtet.
	 *
	 * @param idFachlehrer   die ID des Fachlehrers
	 */
	function hatFachlehrerKompetenz(idFachlehrer: number | null): boolean {
		// Prüfe, ob der Benutzer sowieso eine übergeordnete funktionsbezogene Kompetenz hat
		if (hatUpdateKompetenz.value) {
			return true;
		}
		// Prüfe, ob es sich um das aktuelle Schuljahr handelt. Wenn nicht, so hat ein Fachlehrer keine besonderen Kompetenzen
		if (schuleState.abschnitt.id !== props.manager().lernabschnittGet().schuljahresabschnitt) {
			return false;
		}
		// Prüfe, ob der aktuelle Benutzer der Fachlehrer mit der übergebenen ID ist
		return (benutzerState.benutzerdaten.typ === BenutzerTyp.LEHRER.id) && (benutzerState.benutzerdaten.typID === idFachlehrer);
	}

	function updateAuswahl() {
		const allSelected = (leistungen.value.size() === selectedLeistungen.value.size);
		if (allSelected) {
			selectedLeistungen.value.clear();
		} else {
			for (const leistung of leistungen.value) {
				selectedLeistungen.value.add(leistung);
			}
		}
	}

	const deleteAuswahl = async () => {
		if (selectedLeistungen.value.size === 0) {
			return;
		}
		const leistungenIDs = new ArrayList<number>();
		for (const leistung of selectedLeistungen.value) {
			leistungenIDs.add(leistung.id);
		}
		await props.deleteLeistungen(leistungenIDs);
	};

	async function patchFach(fach: FachDaten | null, leistung: SchuelerLeistungsdaten) {
		// Fach-Eintrag bei den Leistungsdaten wird entfernt
		if (fach === null) {
			await props.patchLeistung({ fachID: -1, kursID: null }, leistung.id);
			return;
		}
		// Spezialfälle
		const f: Fach = Fach.getBySchluesselOrDefault(fach.kuerzelStatistik);
		let kursart;
		if (f === Fach.VX) { // Speziallfall Gymnasiale Oberstufe - Vertiefungsfach
			kursart = ZulaessigeKursart.VTF;
		} else if (f === Fach.PX) { // Speziallfall Gymnasiale Oberstufe - Projektkursfach
			kursart = ZulaessigeKursart.PJK;
		} else { // Allgemeiner Fall: Entfernen des Kurses und setzen einer speziellen Kursart, wenn die kursart der Leistung null ist
			kursart = (leistung.kursart === null) ? null : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart);
			if (kursart === null) {
				kursart = ZulaessigeKursart.PUK;
			}
		}

		await props.patchLeistung({ fachID: fach.id, kursID: null, kursart: kursart.daten(schuljahr.value)?.kuerzel }, leistung.id);
	}

	async function patchKurs(kurs: KursDaten | undefined, leistung: SchuelerLeistungsdaten) {
		if (kurs === undefined) {
			await props.patchLeistung({ kursID: null, kursart: ZulaessigeKursart.PUK.daten(schuljahr.value)?.kuerzel, abifach: null }, leistung.id);
			return;
		}

		const kursart = (leistung.kursart === null) ? ZulaessigeKursart.PUK : ZulaessigeKursart.data().getWertByKuerzel(leistung.kursart);
		if (kurs.kursartAllg !== kursart?.daten(schuljahr.value)?.kuerzelAllg) {
			const { kursart, abifach } = bestimmeKursartUndAbifach(kurs, leistung);
			await props.patchLeistung({
				kursID: kurs.id,
				lehrerID: kurs.lehrer,
				kursart: kursart?.daten(schuljahr.value)?.kuerzel ?? null,
				abifach: abifach,
				wochenstunden: kurs.wochenstunden,
			}, leistung.id);
		} else {
			await props.patchLeistung({ kursID: kurs.id, lehrerID: kurs.lehrer, wochenstunden: kurs.wochenstunden }, leistung.id);
		}
	}

	function bestimmeKursartUndAbifach(kurs: KursDaten, leistung: SchuelerLeistungsdaten) {
		const kursarten: List<ZulaessigeKursart> = ZulaessigeKursart.getByAllgemeinerKursart(schuljahr.value, kurs.kursartAllg);
		let kursart: ZulaessigeKursart | null;
		let abifach: number | null = leistung.abifach;

		if (kurs.kursartAllg === ZulaessigeKursart.E.daten(schuljahr.value)?.kuerzel) { // Speziallfall Gesamtschule E-Kurs
			kursart = ZulaessigeKursart.E;
		} else if (kurs.kursartAllg === ZulaessigeKursart.G.daten(schuljahr.value)?.kuerzel) { // Speziallfall Gesamtschule G-Kurs
			kursart = ZulaessigeKursart.G;
		} else if (kurs.kursartAllg === ZulaessigeKursart.E.daten(schuljahr.value)?.kuerzelAllg) { // Spezialfall Gesamtschule DK-Kurs -> nehme G als Default
			kursart = ZulaessigeKursart.G;
		} else if (kurs.kursartAllg === ZulaessigeKursart.GKM.daten(schuljahr.value)?.kuerzelAllg) { // Spezialfall Gymnasiale Oberstufe GK -> Berücksichtige Abiturfach, Default GKM
			kursart = ZulaessigeKursart.GKM;
			if ((leistung.abifach === 1) || (leistung.abifach === 2)) {
				abifach = null;
			}
			if (leistung.abifach === 3) {
				kursart = ZulaessigeKursart.AB3;
			} else if (leistung.abifach === 4) {
				kursart = ZulaessigeKursart.AB4;
			}
		} else if (kurs.kursartAllg === ZulaessigeKursart.LK1.daten(schuljahr.value)?.kuerzelAllg) { // Spezialfall Gymnasiale Oberstufe LK -> Berücksichtige Abiturfach, Default LK1
			// TODO Prüfen, ob das Fach für LK1 zulässig ist -> wenn nicht immer LK2, ansonsten prüfen, ob LK1 bereits bei den Lernabschnittsdaten zugeordnet ist und LK2 nicht. Ist dies der Fall -> LK2, sonst LK1
			kursart = ZulaessigeKursart.LK1;
			if (leistung.abifach === 2) {
				kursart = ZulaessigeKursart.LK2;
			}
			if (leistung.abifach === null) {
				abifach = 1;
			}
		} else {
			kursart = kursarten.isEmpty() ? null : kursarten.get(0);
		}
		return { kursart, abifach };
	}

	watch(leistungen, (newLeistungen, oldLeistungen) => {
		if (newLeistungen.size() === oldLeistungen.size()) {
			const tmpSetIDs = new Set<number>();
			for (const l of oldLeistungen) {
				tmpSetIDs.add(l.id);
			}
			let changed: boolean = false;
			for (const l of newLeistungen) {
				if (!tmpSetIDs.has(l.id)) {
					changed = true;
					break;
				}
			}
			if (!changed) {
				return;
			}
		}
		selectedLeistungen.value.clear();
	});

</script>

<!--<style scoped>-->

<!--	.svws-ui-tr {-->
<!--		grid-template-columns: v-bind(gridcolumns);-->
<!--	}-->

<!--</style>-->
