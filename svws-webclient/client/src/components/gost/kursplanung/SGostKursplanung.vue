<template>
	<div class="page page-flex-row">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button-select v-if="hatBlockung" type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon i-ri-printer-line" v-else /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-kursplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<template v-if="hatBlockung">
			<Teleport to=".svws-sub-nav-target" v-if="isMounted">
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
					<svws-ui-button :disabled="apiStatus.pending" type="transparent" title="Erstelle eine Blockung aus der Persistierung in den Leistungsdaten" @click.stop="restoreBlockung">
						<span class="icon i-ri-arrow-turn-back-line" /> Wiederherstellen einer Blockung aus den bestehenden Leistungsdaten
					</svws-ui-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, onMounted } from "vue";
	import type { GostKursplanungProps } from "./SGostKursplanungProps";
	import type { DownloadPDFTypen } from "./DownloadPDFTypen";
	import { BenutzerKompetenz, GostHalbjahr, HashSet, SetUtils } from "@core";
	import { useRegionSwitch } from "@ui";

	const props = defineProps<GostKursplanungProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN)
		|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN)
			&& props.benutzerKompetenzenAbiturjahrgaenge.has(props.jahrgangsdaten().abiturjahr)));

	const aktuellesHalbjahr = computed<GostHalbjahr | null>(() => GostHalbjahr.fromJahrgangUndHalbjahr(props.jahrgangsdaten().jahrgang, props.jahrgangsdaten().halbjahr));

	const collapsed = ref<boolean>(true);
	const regelzahl = computed<number>(() => props.hatBlockung ? props.getDatenmanager().regelGetAnzahl() : 0);
	const blockungsname = computed<string>(() => props.getDatenmanager().daten().name);
	const istVorlage = computed<boolean>(() => props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1);
	const vergangenheit = computed<boolean>(() => {
		const jgdaten = props.jahrgangsdaten();
		if (jgdaten.istAbgeschlossen)
			return true;
		if (aktuellesHalbjahr.value === null)
			return false;
		return props.halbjahr.id < aktuellesHalbjahr.value.id;
	});
	const persistiert = computed<boolean>(() => props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id]);
	const hatNoten = computed<boolean>(() => props.jahrgangsdaten().existierenNotenInLeistungsdaten[props.halbjahr.id]);
	const aktivieren_moeglich = computed<boolean>(() => !vergangenheit.value && !persistiert.value && props.existiertSchuljahresabschnitt);
	const synchronisieren_moeglich = computed<boolean>(() => !vergangenheit.value && !hatNoten.value && persistiert.value);

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	function onToggle() {
		return collapsed.value = !collapsed.value;
	}

	const dropdownList = [
		{ text: "Schülerliste markierte Kurse", action: () => downloadPDF("Schülerliste markierte Kurse"), default: true },
		{ text: "Kurse mit Statistikwerten", action: () => downloadPDF("Kurse mit Statistikwerten") },
		{ text: "Kurse-Schienen-Zuordnung", action: () => downloadPDF("Kurse-Schienen-Zuordnung") },
		{ text: "Kurse-Schienen-Zuordnung markierter Schüler", action: () => downloadPDF("Kurse-Schienen-Zuordnung markierter Schüler") },
		{ text: "Kurse-Schienen-Zuordnung gefilterte Schüler", action: () => downloadPDF("Kurse-Schienen-Zuordnung gefilterte Schüler") },
		{ text: "Kursbelegung markierter Schüler", action: () => downloadPDF("Kursbelegung markierter Schüler") },
		{ text: "Kursbelegung gefilterte Schüler", action: () => downloadPDF("Kursbelegung gefilterte Schüler") },
	];

	async function downloadPDF(title: DownloadPDFTypen) {
		const { data, name } = await props.getPDF(title);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	const actionsKursSchuelerzuordnung = computed(() => {
		const filter = props.schuelerFilter();
		const result: Array<{ text: string; action: () => Promise<void|boolean>; default?: boolean; separator?: true }> = [];
		// result.push({ text: "Kurse leeren ...", action: async () => {}, default: true });
		result.push({ text: "Leere alle Kurse, beachte Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01_LEERE_ALLE_KURSE(false)) });
		result.push({ text: "Leere alle Kurse, ignoriere Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01_LEERE_ALLE_KURSE(true)) });
		if ((props.getKursauswahl().size() !== 0) && (props.getDatenmanager().kursGetAnzahl() !== props.getKursauswahl().size())) {
			result.push({ text: "Kursauswahl: Leere Kurse, beachte Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(props.getKursauswahl(), false)) });
			result.push({ text: "Kursauswahl: Leere Kurse, ignoriere Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(props.getKursauswahl(), true)) });
		}
		if (filter.kurs !== undefined) {
			const idSet = SetUtils.create1(filter.kurs.id);
			result.push({ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Leere Kurs, beachte Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(idSet, false)) });
			result.push({ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Leere Kurs, ignoriere Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(idSet, true)) });
		}
		if (filter.fach !== undefined) {
			const idSet = new HashSet<number>();
			let namen = "";
			for (const k of props.getErgebnismanager().getOfFachKursmenge(filter.fach)) {
				const kursart = filter.kursart;
				if ((kursart !== undefined) && (k.kursart !== kursart.id))
					continue;
				idSet.add(k.id);
				namen += props.getErgebnismanager().getOfKursName(k.id) + ', ';
			}
			namen = namen.slice(0, -2);
			if (idSet.size() > 0) {
				result.push({ text: `${namen}: Leere Kurse, beachte Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(idSet, false)) });
				result.push({ text: `${namen}: Leere Kurse, ignoriere Fixierungen`, action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(idSet, true)) });
			}
		}
		return result;
	});

	const actionsRegeln = computed(() => {
		const kursauswahl = props.getKursauswahl();
		const allSelected = (props.getDatenmanager().kursGetAnzahl() === kursauswahl.size());
		const hatAbiturkurse = (props.halbjahr.halbjahr > 1);
		const filter = props.schuelerFilter();
		const result: Array<{ text: string; action: () => Promise<void>; default?: boolean; separator?: true }> = [];
		result.push({ text: "Fixiere alle Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN())});
		result.push({ text: "Löse alle fixierten Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_02c_KURS_FIXIERE_ALLE_IN_IHREN_SCHIENEN())});
		result.push({ text: "", action: async () => {}, separator: true });
		if ((props.getKursauswahl().size() === 0) || allSelected) {
			result.push({ text: "Fixiere alle Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04c_SCHUELER_FIXIEREN_IN_ALLEN_KURSEN())});
			result.push({ text: "Löse alle fixierten Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_04c_SCHUELER_FIXIEREN_IN_ALLEN_KURSEN())});
			if (hatAbiturkurse) {
				result.push({ text: "Fixiere alle Schüler mit Abiturkursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB())});
				result.push({ text: "Fixiere alle Schüler in Leistungskursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK())});
				result.push({ text: "Fixiere alle Schüler im dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3())});
				result.push({ text: "Fixiere alle Schüler im Leistungskurs und dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3())});
				result.push({ text: "Fixiere alle Schüler im vierten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4())});
				result.push({ text: "Fixiere alle Schüler in schriftlichen Kursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH())});
			}
		} else {
			result.push({ text: "Kursauswahl: Fixiere Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(props.getKursauswahl()))});
			result.push({ text: "Kursauswahl: Löse fixierte Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(props.getKursauswahl()))});
			result.push({ text: "Kursauswahl: Fixiere Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(props.getKursauswahl()))});
			result.push({ text: "Kursauswahl: Löse fixierte Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(props.getKursauswahl()))});
			if (hatAbiturkurse) {
				result.push({ text: "Kursauswahl: Fixiere Schüler mit Abiturkursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04i_SCHUELER_FIXIEREN_TYP_AB_DER_KURSMENGE(kursauswahl))});
				result.push({ text: "Kursauswahl: Fixiere Schüler in Leistungskursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04e_SCHUELER_FIXIEREN_TYP_LK_DER_KURSMENGE(kursauswahl))});
				result.push({ text: "Kursauswahl: Fixiere Schüler im dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04f_SCHUELER_FIXIEREN_TYP_AB3_DER_KURSMENGE(kursauswahl))});
				result.push({ text: "Kursauswahl: Fixiere Schüler im Leistungskurs und dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04g_SCHUELER_FIXIEREN_TYP_LK_UND_AB3_DER_KURSMENGE(kursauswahl))});
				result.push({ text: "Kursauswahl: Fixiere Schüler im vierten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04h_SCHUELER_FIXIEREN_TYP_AB4_DER_KURSMENGE(kursauswahl))});
				result.push({ text: "Kursauswahl: Fixiere Schüler in schriftlichen Kursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04j_SCHUELER_FIXIEREN_TYP_SCHRIFTLICH_DER_KURSMENGE(kursauswahl))});
			}
		}
		if (filter.kurs !== undefined) {
			const kurseSet = new HashSet<number>();
			kurseSet.add(filter.kurs.id);
			result.push({ text: "", action: async () => {}, separator: true });
			result.push({ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Fixiere Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(kurseSet))});
			result.push({ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Löse Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(kurseSet))});
		}
		if (filter.fach !== undefined) {
			const kursart = filter.kursart;
			const kurse = props.getErgebnismanager().getOfFachKursmenge(filter.fach);
			const kurseSet = new HashSet<number>();
			let namen = "";
			for (const k of kurse) {
				if ((kursart !== undefined) && (k.kursart !== kursart.id))
					continue;
				kurseSet.add(k.id);
				namen += props.getErgebnismanager().getOfKursName(k.id) + ', ';
			}
			namen = namen.slice(0, -2);
			if (kurseSet.size() > 0) {
				result.push({ text: "", action: async () => {}, separator: true });
				result.push({ text: `${namen}: Fixiere Kurse`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(kurseSet))});
				result.push({ text: `${namen}: Löse Kurse`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(kurseSet))});
				result.push({ text: `${namen}: Fixiere Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(kurseSet))});
				result.push({ text: `${namen}: Löse Schüler`, action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(kurseSet))});
			}
		}
		return result;
	});

</script>
