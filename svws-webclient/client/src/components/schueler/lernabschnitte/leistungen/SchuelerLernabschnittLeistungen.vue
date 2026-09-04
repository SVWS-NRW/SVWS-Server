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
								<svws-ui-checkbox :model-value="(leistungen.size() === selectedLeistungenIds.size) && !leistungen.isEmpty()"
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

			<template #default="{ row: leistungModel }">
				<td class="cursor-pointer flex items-center justify-center"
					:style="getCellStyle(leistungModel.proxy)">
					<svws-ui-checkbox :model-value="selectedLeistungenIds.has(leistungModel.proxy.id)"
						@update:model-value="selectedLeistungenIds.has(leistungModel.proxy.id) ? selectedLeistungenIds.delete(leistungModel.proxy.id) : selectedLeistungenIds.add(leistungModel.proxy.id)"
						headless />
				</td>
				<td :style="getCellStyle(leistungModel.proxy)">
					<ui-select title="Fach"
						:manager="fachSelectManager"
						v-model="leistungModel.fach.value"
						:validation="() => leistungModel.getFehler('fachID')"
						headless :removable="false"
						:readonly="!hatUpdateKompetenz" />
				</td>
				<td :style="getCellStyle(leistungModel.proxy)">
					<ui-select title="Kurs"
						:manager="getKursSelectManagerByIdLeistung(leistungModel.proxy.id)"
						v-model="leistungModel.kurs.value"
						:validation="() => leistungModel.getFehler('kursID')"
						headless :removable="leistungModel.kurs.value !== undefined"
						:readonly="!hatUpdateKompetenz" />
				</td>
				<td :style="getCellStyle(leistungModel.proxy)">
					<!-- TODO In Gesamtschulen kann bei Klassenunterricht neben PUK noch E oder G als Kursart vorkommen -->
					<span v-if="(manager().kursGetByLeistungIdOrNull(leistungModel.proxy.id) === null)
						|| ZulaessigeKursart.getByAllgemeinerKursart(schuljahr, manager().kursGetByLeistungIdOrNull(leistungModel.proxy.id)!.kursartAllg).size() === 1">
						{{ leistungModel.proxy.kursart }}
					</span>
					<ui-select v-else title="Kursart"
						:manager="getKursartSelectManagerByIdLeistung(leistungModel.proxy.id)"
						v-model="leistungModel.kursart.value"
						:validation="() => leistungModel.getFehler('kursart')"
						headless :removable="false"
						:readonly="!hatUpdateKompetenz" />
				</td>
				<template v-if="istGymOb">
					<td :style="getCellStyle(leistungModel.proxy)">
						{{ leistungModel.proxy.abifach ?? "" }}
					</td>
				</template>
				<td :style="getCellStyle(leistungModel.proxy)">
					{{ leistungModel.proxy.wochenstunden ?? "" }}
				</td>
				<td :style="getCellStyle(leistungModel.proxy)">
					<ui-select title="Lehrer"
						:manager="lehrerSelectManager"
						v-model="leistungModel.lehrer.value"
						:validation="() => leistungModel.getFehler('lehrerID')"
						headless :removable="leistungModel.lehrer.value !== undefined"
						:readonly="!hatUpdateKompetenz" />
				</td>
				<td class="border-s" :style="getCellStyle(leistungModel.proxy)">
					<ui-select title="Quartalsnote"
						:manager="notenSelectManager"
						v-model="leistungModel.noteQuartal.value"
						:validation="() => leistungModel.getFehler('noteQuartal')"
						headless :removable="leistungModel.noteQuartal.value !== undefined"
						:readonly="!hatFachlehrerKompetenz(leistungModel.proxy.lehrerID)" />
				</td>
				<td class="border-s" :style="getCellStyle(leistungModel.proxy)">
					<ui-select title="Halbjahresnote"
						:manager="notenSelectManager"
						v-model="leistungModel.noteHalbjahr.value"
						:validation="() => leistungModel.getFehler('note')"
						headless :removable="leistungModel.noteHalbjahr.value !== undefined"
						:readonly="!hatFachlehrerKompetenz(leistungModel.proxy.lehrerID)" />
				</td>
			</template>

			<template #footer>
				<div class="col-span-full flex justify-between items-center my-1 px-1 py-2 border-b">
					<div>{{ (selectedLeistungenIds.size > 0) ? (selectedLeistungenIds.size + " ausgewählt") : " " }}</div>
					<ui-table-actions v-if="hatUpdateKompetenz" :actions="tableBulkActions" :items="selectedLeistungenIds" always-visible />
				</div>
			</template>
		</ui-table-grid>

		<svws-ui-content-card class="min-h-fit grow">
			<svws-ui-input-wrapper :grid="2" v-if="hatLernbereichsnote">
				<span class="font-bold col-span-full">Lernbereichsnoten</span>
				<ui-select title="Gesellschaftswissenschaft"
					v-model="allgemeinModel.lernbereichsnoteGSbzwAL.value"
					:manager="lernbereichsnotenSelectManager"
					:validation="() => allgemeinModel.getFehler('noteLernbereichGSbzwAL')"
					:removable="allgemeinModel.lernbereichsnoteGSbzwAL.value !== undefined"
					:readonly="!hatUpdateKompetenz" />
				<ui-select title="Naturwissenschaft"
					v-model="allgemeinModel.lernbereichsnoteNW.value"
					:manager="lernbereichsnotenSelectManager"
					:validation="() => allgemeinModel.getFehler('noteLernbereichNW')"
					:removable="allgemeinModel.lernbereichsnoteNW.value !== undefined"
					:readonly="!hatUpdateKompetenz" />
			</svws-ui-input-wrapper>
			<svws-ui-spacing :size="2" v-if="hatLernbereichsnote" />
			<svws-ui-input-wrapper class="col-span-full items-center" :grid="4">
				<span class="font-bold col-span-full">Fehlstunden (Summe)</span>
				<svws-ui-input-number placeholder="Maximal"
					v-model="allgemeinModel.proxy.fehlstundenGrenzwert"
					@change="allgemeinModel.patch"
					:validation="() => allgemeinModel.getFehler('fehlstundenGrenzwert')"
					:disabled="!hatUpdateKompetenz"
					:min="0" />
				<svws-ui-input-number placeholder="Gesamt"
					v-model="allgemeinModel.proxy.fehlstundenGesamt"
					@change="allgemeinModel.patch"
					:validation="() => allgemeinModel.getFehler('fehlstundenGesamt')"
					:disabled="!hatUpdateKompetenz"
					:min="0" />
				<svws-ui-input-number placeholder="Unentschuldigt"
					v-model="allgemeinModel.proxy.fehlstundenUnentschuldigt"
					@change="allgemeinModel.patch"
					:validation="() => allgemeinModel.getFehler('fehlstundenUnentschuldigt')"
					:disabled="!hatUpdateKompetenz"
					:min="0" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { SchuelerLernabschnittLeistungenProps } from "./SchuelerLernabschnittLeistungenProps";
	import { SchuelerLeistungsdatenModelProxy } from "~/components/schueler/lernabschnitte/leistungen/modelproxy/SchuelerLeistungsdatenModelProxy";
	import { SchuelerLernabschnittAllgemeinModelProxy } from "~/components/schueler/lernabschnitte/allgemein/modelproxy/SchuelerLernabschnittAllgemeinModelProxy";
	import type { SchuelerLeistungsdaten } from "@core/asd/data/schueler/SchuelerLeistungsdaten";
	import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
	import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
	import { ZulaessigeKursart } from "@core/asd/types/kurse/ZulaessigeKursart";
	import { Note } from "@core/asd/types/Note";
	import type { Schulform } from "@core/asd/types/schule/Schulform";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useModelProxyList } from "@ui/model/useModelProxyList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";

	const props = defineProps<SchuelerLernabschnittLeistungenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const selectedLeistungenIds = ref<Set<number>>(new Set());
	const lernabschnittsdaten = computed<SchuelerLernabschnittsdaten>(() => props.manager().lernabschnittGet());
	/// Das Schuljahr der ausgewählten Lernabschnittsdaten
	const schuljahr = computed<number>(() => props.manager().schuljahrGet());
	const schulform = computed<Schulform>(() => schuleState.schulform);
	const leistungen = computed<List<SchuelerLeistungsdaten>>(() => props.manager().leistungGetMengeAsListSortedByFach());
	const istGymOb = computed<boolean>(() => Jahrgaenge.data().getWertBySchluessel(props.schuelerListeManager().auswahl().jahrgang)?.istGymOb() ?? false);
	const lernbereichsnote1Bezeichnung = computed<string | null>(() => props.manager().lernabschnittGetLernbereichsnote1Bezeichnung());
	const lernbereichsnote2Bezeichnung = computed<string | null>(() => props.manager().lernabschnittGetLernbereichsnote2Bezeichnung());
	const hatLernbereichsnote = computed<boolean>(() => (lernbereichsnote1Bezeichnung.value !== null) || (lernbereichsnote2Bezeichnung.value !== null));
	const hatLernabschnittFaecher = computed<boolean>(() => props.manager().fachGetMenge().size() > 0);
	const someSelected = computed<boolean>(() => (selectedLeistungenIds.value.size > 0) && (selectedLeistungenIds.value.size < leistungen.value.size()));

	const tableBulkActions = computed(() => {
		return [
			{
				label: "Selektierte Leistungsdaten entfernen",
				action: deleteAuswahl,
				disabled: selectedLeistungenIds.value.size === 0,
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

	const allgemeinModel = new SchuelerLernabschnittAllgemeinModelProxy(() => lernabschnittsdaten.value,
		props.manager,
		() => schulform.value,
		() => schuljahr.value,
		async (data: Partial<SchuelerLernabschnittsdaten>) => {
			await props.patch(data);
			return true;
		}
	);

	const leistungenModels = useModelProxyList<SchuelerLeistungsdaten, SchuelerLeistungsdatenModelProxy>(
		leistungen,
		leistung => leistung.id,
		leistung => new SchuelerLeistungsdatenModelProxy(
			() => leistung,
			props.manager,
			() => schuljahr.value,
			async (data: Partial<SchuelerLeistungsdaten>) => {
				await props.patchLeistung(data, leistung.id);
				return true;
			})
	);

	const columns = [
		{ kuerzel: "auswahl", name: "Auswahl", width: "1.5rem" },
		{ kuerzel: "fachID", name: "Fach", width: "minmax(10rem, 2.5fr)" },
		{ kuerzel: "kursID", name: "Kurs", width: "minmax(10rem, 1fr)" },
		{ kuerzel: "kursart", name: "Kursart", width: "5rem" },
		{ kuerzel: "abifach", name: "Abitur", width: "5rem" },
		{ kuerzel: "wochenstunden", name: "WStd", width: "5rem" },
		{ kuerzel: "lehrerID", name: "Lehrer", width: "minmax(10rem, 2fr)" },
		{ kuerzel: "noteQuartal", name: "Quartal", width: "5rem" },
		{ kuerzel: "note", name: "Halbjahr", width: "5rem" },
	];

	const gridManager = new GridManager<string, SchuelerLeistungsdatenModelProxy, Array<SchuelerLeistungsdatenModelProxy>>({
		daten: leistungenModels,
		getRowKey: row => `${row.proxy.id}`,
		columns,
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



	const lernbereichsnoten = computed<Note[]>(() => [Note.SEHR_GUT, Note.GUT, Note.BEFRIEDIGEND, Note.AUSREICHEND, Note.MANGELHAFT, Note.UNGENUEGEND]);
	const lehrer = computed<Iterable<LehrerListeEintrag>>(() => props.manager().lehrerGetMengeAktiv());
	const faecher = computed<Iterable<FachDaten>>(() => props.manager().fachGetMenge());

	const lernbereichsnotenSelectManager = new SelectManager({
		options: lernbereichsnoten,
		optionDisplayText: note => note.daten(schuljahr.value)?.kuerzel ?? '-',
		selectionDisplayText: note => note.daten(schuljahr.value)?.kuerzel ?? '-',
	});

	const notenSelectManager = new SelectManager({
		options: Note.values().filter(n => n !== Note.KEINE),
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
		const allSelected = (leistungen.value.size() === selectedLeistungenIds.value.size);
		if (allSelected) {
			selectedLeistungenIds.value.clear();
		} else {
			for (const leistung of leistungen.value) {
				selectedLeistungenIds.value.add(leistung.id);
			}
		}
	}

	const deleteAuswahl = async () => {
		if (selectedLeistungenIds.value.size === 0) {
			return;
		}

		const leistungenIDs = new ArrayList<number>();
		for (const idLeistung of selectedLeistungenIds.value) {
			leistungenIDs.add(idLeistung);
		}

		await props.deleteLeistungen(leistungenIDs);
	};

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
		selectedLeistungenIds.value.clear();
	});

</script>
