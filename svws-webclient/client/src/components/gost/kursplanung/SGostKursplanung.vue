<template>
	<div class="page page-flex-row">
		<Teleport to=".svws-ui-header--actions" defer>
			<svws-ui-button-select v-if="hatBlockung" type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon i-ri-printer-line" v-else /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-kursplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<template v-if="hatBlockung">
			<Teleport to=".svws-sub-nav-target" defer>
				<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
					<div v-if="hatUpdateKompetenz" class="flex gap-2 items-center">
						<div class="text-button font-normal">Ergebnis:</div>
						<svws-ui-button type="transparent" @click.stop="ergebnisAbleiten()" title="Eine neue Blockung auf Grundlage dieses Ergebnisses erstellen." class="text-ui-100 subNavigationFocusField">
							<span class="icon-sm i-ri-file-copy-line" /> Ableiten
						</svws-ui-button>
						<s-gost-kursplanung-kursansicht-modal-blockung-hochschreiben :get-datenmanager :ergebnis-hochschreiben v-slot="{ openModal }">
							<svws-ui-button type="transparent" @click="openModal()" title="Überträgt die Daten dieser Blockung in das nächste Halbjahr">
								<span class="icon-sm i-ri-corner-right-up-line" /> Hochschreiben
							</svws-ui-button>
						</s-gost-kursplanung-kursansicht-modal-blockung-hochschreiben>
						<s-gost-kursplanung-kursansicht-modal-blockung-aktivieren v-if="!persistiert" :hat-ungueltige-kurszuordnungen="props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().size() > 0" :ergebnis-aktivieren :blockungsname v-slot="{ openModal }">
							<svws-ui-button type="transparent" :disabled="!aktivieren_moeglich" size="small" @click="openModal()" title="Überträgt die Blockung in die Kurstabelle und in die Leistungsdaten der Schüler">
								<span class="icon-sm i-ri-arrow-right-circle-line" /> Übertragen
							</svws-ui-button>
						</s-gost-kursplanung-kursansicht-modal-blockung-aktivieren>
						<s-gost-kursplanung-kursansicht-modal-ergebnis-synchronisieren v-else :get-datenmanager :ergebnis-synchronisieren :blockungsname v-slot="{ openModal }">
							<svws-ui-button type="transparent" :disabled="!synchronisieren_moeglich" size="small" @click="openModal()" title="Gleicht die Daten dieses Blockungsergebnisses mit den Daten der Kurstabelle und den Leistungsdaten der Schüler ab. Dabei werden ggf. nicht vorhandene Kurs in der Kurstabelle angelegt und die Kurs-Schienen-Zuordnung angepasst. Leere Kurse werden nicht entfernt. Außerdem wird die Kurs-Schüler-Zuordnung in den Leistungsdaten des Schüler aktualisiert. Neue Fächer werden in den Leistungsdaten dabei allerdings nicht hinzugefügt.">
								<span class="icon-sm i-ri-loop-left-line" /> Synchronisieren
							</svws-ui-button>
						</s-gost-kursplanung-kursansicht-modal-ergebnis-synchronisieren>
						<div class="border-l border-ui-10 ml-6 h-5 w-7" />
						<div class="text-button font-normal">Kurse:</div>
						<s-gost-kursplanung-kursansicht-modal-irrlaeufer v-if="getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().size()" :update-kurs-schueler-zuordnungen :get-ergebnismanager v-slot="{ openModal }">
							<svws-ui-button type="error" size="small" @click="openModal()" title="Zeigt ungültige Schüler/Kurs-Zuordnungen, die aufgelöst werden können">
								<span class="icon-sm i-ri-error-warning-line" /> Ungültige Kurszuordnungen
							</svws-ui-button>
						</s-gost-kursplanung-kursansicht-modal-irrlaeufer>
						<s-gost-kursplanung-kursansicht-modal-falscher-abi-jg v-if="getErgebnismanager().getOfSchuelerMengeMitAbweichendemAbijahrgang().size()" :regeln-update :update-kurs-schueler-zuordnungen :get-ergebnismanager v-slot="{ openModal }">
							<svws-ui-button type="error" size="small" @click="openModal()" title="Zeigt Schüler mit ungültigem Abiturjahrgang, die entfernt werden können">
								<span class="icon-sm i-ri-error-warning-line" /> Ungültige Abiturjahrgänge
							</svws-ui-button>
						</s-gost-kursplanung-kursansicht-modal-falscher-abi-jg>
						<s-gost-kursplanung-schueler-auswahl-umkursen-modal v-slot="{ openModal }"
							:get-datenmanager :get-ergebnismanager :update-kurs-schueler-zuordnungen :regeln-update
							:schueler-filter :api-status :fixierte-verschieben :set-fixierte-verschieben :in-zielkurs-fixieren :set-in-zielkurs-fixieren>
							<svws-ui-button size="small" type="transparent" @click="openModal">
								<span class="icon-sm i-ri-group-line" /> Schülerzuordnung
							</svws-ui-button>
						</s-gost-kursplanung-schueler-auswahl-umkursen-modal>
						<svws-ui-button-select type="transparent" :dropdown-actions="actionsKursSchuelerzuordnung" :default-action="{ text: 'Kurse leeren ...', action: async () => {}, default: true }">
							<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon-sm i-ri-delete-bin-line" v-else /> </template>
						</svws-ui-button-select>
						<svws-ui-button-select type="transparent" :dropdown-actions="actionsRegeln">
							<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon-sm i-ri-pushpin-line" v-else /> </template>
						</svws-ui-button-select>
						<template v-if="istVorlage">
							<svws-ui-button @click="removeKurse(getKursauswahl())" :disabled="getKursauswahl().size() < 1" :class="getKursauswahl().size() < 1 ? 'opacity-50' : 'text-ui-danger'" size="small" type="transparent" title="Kurse aus Auswahl löschen">
								<span class="icon-sm i-ri-delete-bin-line" /> Entfernen
							</svws-ui-button>
						</template>
						<template v-else>
							<div class="pl-2">
								<svws-ui-tooltip>
									<span class="icon-sm i-ri-prohibited-line" />
									<template #content>
										Derzeit können nur die Kurse-Schienen-Zuordnung und die Kurs-Schüler-Zuordnung bearbeitet werden.
										Die grundlegenden Daten und die Regeldefinitionen der Blockung können derzeit nicht bearbeitet werden, da mehr als
										ein Ergebnis existiert. Um erneut Änderungen vornehmen zu können, leiten Sie die Blockung ab oder entfernen Sie
										alle Ergebnisse bis auf eines.
									</template>
								</svws-ui-tooltip>
							</div>
						</template>
					</div>
					<div class="flex gap-2 items-center" :class="{ 'ml-2': !hatUpdateKompetenz }">
						<div class="border-l border-ui-10 ml-6 h-5 w-7" />
						<div class="text-button font-normal">Regeln:</div>
						<svws-ui-button @click="onToggle" size="small" type="transparent" title="Alle Regeln anzeigen" :class="{'mr-2': regelzahl}">
							<span class="icon-sm i-ri-settings-3-line" /> Detailansicht&nbsp;<template #badge v-if="regelzahl"> {{ regelzahl }} </template>
						</svws-ui-button>
						<s-gost-kursplanung-kursansicht-modal-regeln-ungueltig v-if="getDatenmanager().regelGetMapUngueltig().size() > 0" :manager="getDatenmanager" :regeln-update v-slot="{ openModal }">
							<svws-ui-button type="error" size="small" @click="openModal()" title="Zeigt ungültige Regeln, die entfernt werden können">
								<span class="icon-sm i-ri-error-warning-line" /> Ungültige Regeln
							</svws-ui-button>
						</s-gost-kursplanung-kursansicht-modal-regeln-ungueltig>
					</div>
				</svws-ui-sub-nav>
			</Teleport>
			<s-gost-kursplanung-kursansicht class="min-w-fit"
				:zeige-schienenbezeichnungen :set-zeige-schienenbezeichnungen
				:halbjahr :faecher-manager :hat-ergebnis :ergebnis-hochschreiben :api-status :set-blockungstabelle-hidden
				:get-datenmanager :get-kursauswahl :set-kursauswahl :get-ergebnismanager :map-fachwahl-statistik :map-lehrer :schueler-filter :kurssortierung
				:regeln-update :update-kurs-schienen-zuordnung :patch-kurs :add-kurs :remove-kurse :add-kurs-lehrer
				:patch-schiene :add-schiene :remove-schiene :remove-kurs-lehrer :ergebnis-aktivieren :existiert-schuljahresabschnitt
				:blockungstabelle-hidden :add-schiene-kurs :remove-schiene-kurs :combine-kurs :split-kurs :hat-update-kompetenz />
			<router-view name="gost_kursplanung_schueler_auswahl" class="min-w-100" />
			<router-view class="min-w-fit" />
			<Teleport to="body">
				<aside class="app-layout--aside max-w-2xl h-auto" v-if="!collapsed">
					<div class="app-layout--aside-container relative h-auto max-h-full">
						<h2 class="text-headline-md flex justify-between">
							<span>Regeln zur Blockung</span>
							<svws-ui-button type="transparent" @click="onToggle"> Schließen </svws-ui-button>
						</h2>
						<s-gost-kursplanung-regelansicht :get-datenmanager :faecher-manager :regeln-update :get-ergebnismanager :api-status :hat-update-kompetenz />
					</div>
				</aside>
			</Teleport>
		</template>
		<div v-else>
			<div class="p-3 border-2 border-dashed border-ui-10 rounded-lg">
				<div class="text-headline-md mb-5">Keine Blockung für dieses Halbjahr vorhanden</div>
				<div v-if="hatUpdateKompetenz">
					<svws-ui-button type="transparent" @click.stop="addBlockung">
						<span class="icon i-ri-add-line" /> Neue Blockung hinzufügen
					</svws-ui-button>
					<div class="h-2" />
					<svws-ui-button v-if="persistiert" :disabled="apiStatus.pending" type="transparent" title="Erstelle eine Blockung aus der Persistierung in den Leistungsdaten" @click.stop="restoreBlockung">
						<span class="icon i-ri-arrow-turn-back-line" /> Wiederherstellen einer Blockung aus den bestehenden Leistungsdaten
					</svws-ui-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import type { GostKursplanungProps } from "./SGostKursplanungProps";
	import type { DownloadPDFTypen } from "./DownloadPDFTypen";
	import type { ReportingParameter } from "@core/core/data/reporting/ReportingParameter";
	import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";
	import { ListUtils } from "@core/core/utils/ListUtils";
	import { ReportingFilterDefinitionGruppeFactory } from "@core/core/utils/reporting/ReportingFilterDefinitionGruppeFactory";
	import { SetUtils } from "@core/core/utils/SetUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { HashSet } from "@core/java/util/HashSet";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useReportingState } from "@ui/states/ReportingState";
	import { useServerState } from "@ui/states/ServerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	const props = defineProps<GostKursplanungProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const abschnittState = useAbschnittState();
	const reportingState = useReportingState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatUpdateKompetenz = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN)
		|| (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)
			&& benutzerState.kompetenzenAbiturjahrgaenge.has(props.jahrgangsdaten().abiturjahr)));

	const aktuellesHalbjahr = computed<GostHalbjahr | null>(() => GostHalbjahr.fromJahrgangUndHalbjahr(props.jahrgangsdaten().jahrgang, props.jahrgangsdaten().halbjahr));

	const collapsed = ref<boolean>(true);
	const regelzahl = computed<number>(() => props.hatBlockung ? props.getDatenmanager().regelGetAnzahl() : 0);
	const blockungsname = computed<string>(() => props.getDatenmanager().daten().name);
	const istVorlage = computed<boolean>(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1);
	const vergangenheit = computed<boolean>(() => {
		const jgdaten = props.jahrgangsdaten();
		if (jgdaten.istAbgeschlossen) {
			return true;
		}
		if (aktuellesHalbjahr.value === null) {
			return false;
		}
		return props.halbjahr.id < aktuellesHalbjahr.value.id;
	});
	const persistiert = computed<boolean>(() => props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id]);
	const hatNoten = computed<boolean>(() => props.jahrgangsdaten().existierenNotenInLeistungsdaten[props.halbjahr.id]);
	const aktivieren_moeglich = computed<boolean>(() => !vergangenheit.value && !persistiert.value && props.existiertSchuljahresabschnitt);
	const synchronisieren_moeglich = computed<boolean>(() => !vergangenheit.value && !hatNoten.value && persistiert.value);

	function onToggle() {
		collapsed.value = !collapsed.value;
	}

	const dropdownList = computed(() => {
		const actions = [{ text: "Schülerliste markierte Kurse", action: () => createReport("Schülerliste markierte Kurse", 'pdf'), default: true }];
		if (serverState.hasAlpha) {
			actions.push({ text: "E-Mail mit Schülerliste markierte Kurse", action: () => createReport("Schülerliste markierte Kurse", 'email'), default: false });
		}
		actions.push(
			{ text: "Kurse mit Statistikwerten", action: () => createReport("Kurse mit Statistikwerten", 'pdf'), default: false },
			{ text: "Kurse-Schienen-Zuordnung", action: () => createReport("Kurse-Schienen-Zuordnung", 'pdf'), default: false },
			{ text: "Kurse-Schienen-Zuordnung markierter Schüler", action: () => createReport("Kurse-Schienen-Zuordnung markierter Schüler", 'pdf'), default: false },
			{ text: "Kurse-Schienen-Zuordnung gefilterte Schüler", action: () => createReport("Kurse-Schienen-Zuordnung gefilterte Schüler", 'pdf'), default: false },
			{ text: "Kursbelegung markierter Schüler", action: () => createReport("Kursbelegung markierter Schüler", 'pdf'), default: false },
			{ text: "Kursbelegung gefilterte Schüler", action: () => createReport("Kursbelegung gefilterte Schüler", 'pdf'), default: false }
		);
		return actions;
	});

	async function createReport(title: DownloadPDFTypen, type: 'pdf' | 'email') {
		const idsKurse = new ArrayList<number>();
		for (const idKurs of props.getKursauswahl()) {
			idsKurse.add(idKurs);
		}
		const idsSchueler = new ArrayList<number>();
		for (const idSchueler of props.schuelerFilter().filtered.value) {
			idsSchueler.add(idSchueler.id);
		}

		let reportingParameter: ReportingParameter;
		switch (title) {
			case "Schülerliste markierte Kurse":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_KURS_MIT_KURSSCHUELERN.getReportingParameter();
				reportingParameter.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppeFactory.gruppeAusIds("Kursauswahl", "ReportingGostKursplanungKurs", false, idsKurse));
				break;
			case "Kurse mit Statistikwerten":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_KURSE_MIT_STATISTIKWERTEN.getReportingParameter();
				reportingParameter.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppeFactory.gruppeAusIds("Kursauswahl", "ReportingGostKursplanungKurs", false, idsKurse));
				break;
			case "Kurse-Schienen-Zuordnung":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN.getReportingParameter();
				break;
			case "Kurse-Schienen-Zuordnung markierter Schüler":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN.getReportingParameter();
				reportingParameter.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppeFactory.gruppeAusIds("Schülerauswahl", "ReportingSchueler", false, ListUtils.create1(props.idSchueler())));
				break;
			case "Kurse-Schienen-Zuordnung gefilterte Schüler":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_SCHIENEN_KURSEN.getReportingParameter();
				reportingParameter.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppeFactory.gruppeAusIds("Schülerauswahl", "ReportingSchueler", false, idsSchueler));
				break;
			case "Kursbelegung markierter Schüler":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN.getReportingParameter();
				reportingParameter.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppeFactory.gruppeAusIds("Schülerauswahl", "ReportingSchueler", false, ListUtils.create1(props.idSchueler())));
				break;
			case "Kursbelegung gefilterte Schüler":
				reportingParameter = ReportingReportvorlage.GOST_KURSPLANUNG_V_SCHUELER_MIT_KURSEN.getReportingParameter();
				reportingParameter.filterDefinitionenGruppen.add(ReportingFilterDefinitionGruppeFactory.gruppeAusIds("Schülerauswahl", "ReportingSchueler", false, idsSchueler));
				break;
			default:
				throw new DeveloperNotificationException(`Es konnte keine Ausgabe für die gewählte Option gefunden werden. Bitte melden Sie diesen Fehler. Die nicht vorhandene Option lautet '${title}'`);
		}
		reportingParameter.idHauptdatenObjekt = props.getErgebnismanager().getErgebnis().id;
		reportingParameter.idSchuljahresabschnitt = abschnittState.auswahl.id;
		if (type === 'pdf') {
			await reportingState.createPDFReport(reportingParameter);
		} else {
			await reportingState.createEMailReport(reportingParameter);
		}
	}

	const actionsKursSchuelerzuordnung = computed(() => {
		const result: Array<{ text: string; action: () => Promise<void | boolean>; default?: boolean; separator?: true }> = [];
		if (!props.hatBlockung) {
			return result;
		}
		const filter = props.schuelerFilter();
		result.push(
			{ text: "Leere alle Kurse, beachte Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereAlleKurse(false)) },
			{ text: "Leere alle Kurse, ignoriere Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereAlleKurse(true)) });
		if ((props.getKursauswahl().size() !== 0) && (props.getDatenmanager().kursGetAnzahl() !== props.getKursauswahl().size())) {
			result.push(
				{ text: "Kursauswahl: Leere Kurse, beachte Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereKursmenge(props.getKursauswahl(), false)) },
				{ text: "Kursauswahl: Leere Kurse, ignoriere Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereKursmenge(props.getKursauswahl(), true)) });
		}
		if (filter.kurs !== undefined) {
			const idSet = SetUtils.create1(filter.kurs.id);
			result.push(
				{ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Leere Kurs, beachte Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereKursmenge(idSet, false)) },
				{ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Leere Kurs, ignoriere Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereKursmenge(idSet, true)) });
		}
		if (filter.fach !== undefined) {
			const idSet = new HashSet<number>();
			let namen = "";
			for (const k of props.getErgebnismanager().getOfFachKursmenge(filter.fach)) {
				const kursart = filter.kursart;
				if ((kursart !== undefined) && (k.kursart !== kursart.id)) {
					continue;
				}
				idSet.add(k.id);
				namen += props.getErgebnismanager().getOfKursName(k.id) + ', ';
			}
			namen = namen.slice(0, -2);
			if (idSet.size() > 0) {
				result.push(
					{ text: `${namen}: Leere Kurse, beachte Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereKursmenge(idSet, false)) },
					{ text: `${namen}: Leere Kurse, ignoriere Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdateLeereKursmenge(idSet, true)) });
			}
		}
		return result;
	});

	const actionsRegeln = computed(() => {
		const kursauswahl = props.getKursauswahl();
		const allSelected = (props.getDatenmanager().kursGetAnzahl() === kursauswahl.size());
		const hatAbiturkurse = (props.halbjahr.halbjahr > 1);
		const result: Array<{ text: string; action: () => Promise<void>; default?: boolean; separator?: true }> = [];
		if (!props.hatBlockung) {
			return result;
		}
		const filter = props.schuelerFilter();
		result.push(
			{ text: "Fixiere alle Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateKursFixiereAlleInIhrenSchienen()) },
			{ text: "Löse alle fixierten Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveKursFixiereAlleInIhrenSchienen()) },
			{ text: "", action: async () => {}, separator: true });
		if ((props.getKursauswahl().size() === 0) || allSelected) {
			result.push(
				{ text: "Fixiere alle Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenInAllenKursen()) },
				{ text: "Löse alle fixierten Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveSchuelerFixierenInAllenKursen()) });
			if (hatAbiturkurse) {
				result.push(
					{ text: "Fixiere alle Schüler mit Abiturkursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypAb()) },
					{ text: "Fixiere alle Schüler in Leistungskursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypLk()) },
					{ text: "Fixiere alle Schüler im dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypAb3()) },
					{ text: "Fixiere alle Schüler im Leistungskurs und dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypLkUndAb3()) },
					{ text: "Fixiere alle Schüler im vierten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypAb4()) },
					{ text: "Fixiere alle Schüler in schriftlichen Kursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypSchriftlich()) });
			}
		} else {
			result.push(
				{ text: "Kursauswahl: Fixiere Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateKursFixiereMengeInIhrenSchienen(props.getKursauswahl())) },
				{ text: "Kursauswahl: Löse fixierte Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveKursFixiereMengeInIhrenSchienen(props.getKursauswahl())) },
				{ text: "Kursauswahl: Fixiere Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenInDenKursen(props.getKursauswahl())) },
				{ text: "Kursauswahl: Löse fixierte Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveSchuelerFixierenInDenKursen(props.getKursauswahl())) });
			if (hatAbiturkurse) {
				result.push(
					{ text: "Kursauswahl: Fixiere Schüler mit Abiturkursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypAbDerKursmenge(kursauswahl)) },
					{ text: "Kursauswahl: Fixiere Schüler in Leistungskursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypLkDerKursmenge(kursauswahl)) },
					{ text: "Kursauswahl: Fixiere Schüler im dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypAb3DerKursmenge(kursauswahl)) },
					{ text: "Kursauswahl: Fixiere Schüler im Leistungskurs und dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge(kursauswahl)) },
					{ text: "Kursauswahl: Fixiere Schüler im vierten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypAb4DerKursmenge(kursauswahl)) },
					{ text: "Kursauswahl: Fixiere Schüler in schriftlichen Kursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge(kursauswahl)) });
			}
		}
		if (filter.kurs !== undefined) {
			const kurseSet = new HashSet<number>();
			kurseSet.add(filter.kurs.id);
			result.push(
				{ text: "", action: async () => {}, separator: true },
				{ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Fixiere Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenInDenKursen(kurseSet)) },
				{ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Löse Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveSchuelerFixierenInDenKursen(kurseSet)) });
		}
		if (filter.fach !== undefined) {
			const kursart = filter.kursart;
			const kurse = props.getErgebnismanager().getOfFachKursmenge(filter.fach);
			const kurseSet = new HashSet<number>();
			let namen = "";
			for (const k of kurse) {
				if ((kursart !== undefined) && (k.kursart !== kursart.id)) {
					continue;
				}
				kurseSet.add(k.id);
				namen += props.getErgebnismanager().getOfKursName(k.id) + ', ';
			}
			namen = namen.slice(0, -2);
			if (kurseSet.size() > 0) {
				result.push(
					{ text: "", action: async () => {}, separator: true },
					{ text: `${namen}: Fixiere Kurse`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateKursFixiereMengeInIhrenSchienen(kurseSet)) },
					{ text: `${namen}: Löse Kurse`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveKursFixiereMengeInIhrenSchienen(kurseSet)) },
					{ text: `${namen}: Fixiere Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreateSchuelerFixierenInDenKursen(kurseSet)) },
					{ text: `${namen}: Löse Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemoveSchuelerFixierenInDenKursen(kurseSet)) });
			}
		}
		return result;
	});

</script>
