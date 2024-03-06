<template>
	<div class="page--content page--content--full page--content--gost-grid" :class="{'svws-blockungstabelle-hidden': blockungstabelleHidden()}">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button-select v-if="hatBlockung" type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <i-ri-printer-line v-else /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-kursplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<template v-if="hatBlockung">
			<Teleport to=".svws-sub-nav-target" v-if="isMounted">
				<svws-ui-sub-nav>
					<svws-ui-button type="transparent" @click="toggleBlockungstabelle">
						<template v-if="blockungstabelleHidden()">
							<i-ri-menu-unfold-line />
							Tabelle einblenden
						</template>
						<template v-else>
							<i-ri-menu-fold-line />
							Tabelle ausblenden
						</template>
					</svws-ui-button>
					<div class="flex gap-0.5 items-center leading-none">
						<div class="border-l border-black/10 dark:border-white/10 ml-6 h-5 w-7" />
						<div class="text-button font-normal mr-1 -mt-px">Ergebnis:</div>
						<svws-ui-button type="transparent" @click.stop="ergebnisAbleiten()" title="Eine neue Blockung auf Grundlage dieses Ergebnisses erstellen." class="text-black dark:text-white">
							<i-ri-file-copy-line /> Ableiten
						</svws-ui-button>
						<s-card-gost-kursansicht-blockung-aktivieren-modal v-if="!persistiert" :get-datenmanager="getDatenmanager" :ergebnis-aktivieren="ergebnisAktivieren" :blockungsname="blockungsname" v-slot="{ openModal }">
							<svws-ui-button type="transparent" :disabled="!aktivieren_moeglich" size="small" @click="openModal()" title="Überträgt die Blockung in die Kurstabelle und in die Leistungsdaten der Schüler">
								<i-ri-arrow-right-circle-line /> Übertragen
							</svws-ui-button>
						</s-card-gost-kursansicht-blockung-aktivieren-modal>
						<s-card-gost-kursansicht-ergebnis-synchronisieren-modal v-else :get-datenmanager="getDatenmanager" :ergebnis-synchronisieren="ergebnisSynchronisieren" :blockungsname="blockungsname" v-slot="{ openModal }">
							<svws-ui-button type="transparent" :disabled="!synchronisieren_moeglich" size="small" @click="openModal()" title="Synchronisiert die Daten dieser Blockung mit den in der Kurstabelle und den Leistungsdaten persistierten Daten">
								<i-ri-loop-left-line /> Synchronisieren
							</svws-ui-button>
						</s-card-gost-kursansicht-ergebnis-synchronisieren-modal>
						<s-card-gost-kursansicht-blockung-hochschreiben-modal :get-datenmanager="getDatenmanager" :ergebnis-hochschreiben="ergebnisHochschreiben" v-slot="{ openModal }">
							<svws-ui-button type="transparent" @click="openModal()" title="Überträgt die Daten dieser Blockung in das nächste Halbjahr">
								<i-ri-corner-right-up-line /> Hochschreiben
							</svws-ui-button>
						</s-card-gost-kursansicht-blockung-hochschreiben-modal>
					</div>
					<div class="flex gap-0.5 items-center leading-none">
						<div class="border-l border-black/10 dark:border-white/10 ml-6 h-5 w-7" />
						<div class="text-button font-normal mr-1 -mt-px">Kurse:</div>
						<s-card-gost-kursansicht-irrlaeufer-modal v-if="props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().size()" :get-ergebnismanager="getErgebnismanager" :remove-kurs-schueler-zuordnung="removeKursSchuelerZuordnung" v-slot="{ openModal }">
							<svws-ui-button type="error" size="small" @click="openModal()" title="Zeigt ungültige Schüler/Kurs-Zuordnungen, die aufgelöst werden können"> <i-ri-error-warning-line /> Ungültige Kurszuordnungen </svws-ui-button>
						</s-card-gost-kursansicht-irrlaeufer-modal>
						<s-gost-kursplanung-schueler-auswahl-umkursen-modal :get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
							:remove-kurs-schueler-zuordnung="removeKursSchuelerZuordnung" :update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung"
							:update-kurs-schueler-zuordnungen="updateKursSchuelerZuordnungen" :regeln-update="regelnUpdate" :allow-regeln="allowRegeln"
							:schueler-filter="schuelerFilter" v-slot="{ openModal }" :api-status="apiStatus"
							:fixierte-verschieben="fixierteVerschieben" :set-fixierte-verschieben="setFixierteVerschieben"
							:in-zielkurs-fixieren="inZielkursFixieren" :set-in-zielkurs-fixieren="setInZielkursFixieren">
							<svws-ui-button size="small" type="transparent" @click="openModal"><i-ri-group-line /> Schülerzuordnung </svws-ui-button>
						</s-gost-kursplanung-schueler-auswahl-umkursen-modal>
						<svws-ui-button-select type="transparent" :dropdown-actions="actionsKursSchuelerzuordnung" :default-action="{ text: 'Leeren…', action: () => {} }" no-default>
							<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <i-ri-delete-bin-line v-else /> </template>
						</svws-ui-button-select>
						<template v-if="allowRegeln">
							<svws-ui-button-select type="transparent" :dropdown-actions="actionsRegeln" :default-action="{ text: 'Fixieren…', action: () => {} }" no-default>
								<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <i-ri-pushpin-line v-else /> </template>
							</svws-ui-button-select>
							<svws-ui-button @click="removeKurse(getKursauswahl())" :disabled="getKursauswahl().size() < 1" :class="getKursauswahl().size() < 1 ? 'opacity-50' : 'text-error'" size="small" type="transparent" title="Kurse aus Auswahl löschen">
								<i-ri-delete-bin-line /> Entfernen
							</svws-ui-button>
						</template>
						<template v-else>
							<div class="pl-2">
								<svws-ui-tooltip>
									<span><i-ri-prohibited-line /></span>
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
					<div v-if="(regelzahl > 1) || (allowRegeln)" class="flex gap-0.5 items-center leading-none">
						<div class="border-l border-black/10 dark:border-white/10 ml-6 h-5 w-7" />
						<div class="text-button font-normal mr-1 -mt-px">Regeln:</div>
						<svws-ui-button @click="onToggle" size="small" type="transparent" title="Alle Regeln anzeigen" :class="{'mr-2': regelzahl > 0}">
							<i-ri-settings3-line /> Detailansicht&nbsp;<template #badge v-if="regelzahl"> {{ regelzahl }} </template>
						</svws-ui-button>
					</div>
				</svws-ui-sub-nav>
			</Teleport>
			<s-card-gost-kursansicht :zeige-schienenbezeichnungen="zeigeSchienenbezeichnungen" :set-zeige-schienenbezeichnungen="setZeigeSchienenbezeichnungen"
				:halbjahr="halbjahr" :faecher-manager="faecherManager" :hat-ergebnis="hatErgebnis"
				:get-datenmanager="getDatenmanager" :get-kursauswahl="getKursauswahl" :get-ergebnismanager="getErgebnismanager"
				:map-fachwahl-statistik="mapFachwahlStatistik" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter" :kurssortierung="kurssortierung"
				:regeln-update="regelnUpdate" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
				:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurse="removeKurse" :add-kurs-lehrer="addKursLehrer"
				:patch-schiene="patchSchiene" :add-schiene="addSchiene" :remove-schiene="removeSchiene"
				:remove-kurs-lehrer="removeKursLehrer" :ergebnis-aktivieren="ergebnisAktivieren" :existiert-schuljahresabschnitt="existiertSchuljahresabschnitt"
				:ergebnis-hochschreiben="ergebnisHochschreiben" :api-status="apiStatus"
				:toggle-blockungstabelle="toggleBlockungstabelle"
				:blockungstabelle-visible="!blockungstabelleHidden()"
				:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :combine-kurs="combineKurs" :split-kurs="splitKurs" />
			<router-view name="gost_kursplanung_schueler_auswahl" />
			<router-view />
			<Teleport to="body">
				<aside class="app-layout--aside max-w-2xl h-auto" v-if="!collapsed && !(regelzahl < 1 && getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() > 1)">
					<div class="app-layout--aside-container relative h-auto max-h-full">
						<h2 class="text-headline-md flex justify-between">
							<span>Regeln zur Blockung</span>
							<svws-ui-button type="transparent" @click="onToggle"> Schließen </svws-ui-button>
						</h2>
						<s-card-gost-regelansicht :get-datenmanager="getDatenmanager" :faecher-manager="faecherManager" :map-schueler="mapSchueler" :regeln-update="regelnUpdate" :get-ergebnismanager="getErgebnismanager" :api-status="apiStatus" />
					</div>
				</aside>
			</Teleport>
		</template>
		<div v-else class="col-span-full">
			<div class="p-3 border-2 border-dashed border-black/10 dark:border-white/10 rounded-lg max-w-xl">
				<div class="text-headline-md mb-5">Keine Blockung ausgewählt</div>
				<div class="opacity-75 leading-tight flex flex-col gap-2">
					<div>
						<svws-ui-button type="icon" class="inline align-middle" title="Neue Blockung hinzufügen" @click.stop="addBlockung"> <i-ri-add-line /> </svws-ui-button>
						<span class="align-middle">Neue Blockung erstellen</span>
					</div>
					<div v-if="persistiert">
						<svws-ui-button :disabled="apiStatus.pending" type="icon" class="inline align-middle" title="Erstelle eine Blockung aus der Persistierung in den Leistungsdaten" @click.stop="restoreBlockung"> <i-ri-arrow-turn-back-line class="-mb-0.5" /> </svws-ui-button>
						<span class="align-middle">Wiederherstellen einer Blockung aus den bestehenden Leistungsdaten</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, onMounted } from "vue";
	import type { GostKursplanungProps } from "./SGostKursplanungProps";
	import type { DownloadPDFTypen } from "./DownloadPDFTypen";
	import { ArrayList, HashSet } from "@core";

	const props = defineProps<GostKursplanungProps>();

	const collapsed = ref<boolean>(true);
	const regelzahl = computed<number>(() => props.hatBlockung ? props.getDatenmanager().regelGetAnzahl() : 0);
	const blockungsname = computed<string>(() => props.getDatenmanager().daten().name);
	const allowRegeln = computed<boolean>(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));
	const vergangenheit = computed<boolean>(()=> props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id+1]);
	const persistiert = computed<boolean>(()=> props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id])
	const aktivieren_moeglich = computed<boolean>(() => !vergangenheit.value && !persistiert.value && props.existiertSchuljahresabschnitt);
	const synchronisieren_moeglich = computed<boolean>(()=> !vergangenheit.value && persistiert.value);

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const onToggle = () => collapsed.value = !collapsed.value;
	const toggleBlockungstabelle = () => props.setBlockungstabelleHidden(!props.blockungstabelleHidden());

	const dropdownList = [
		{ text: "Schülerliste markierte Kurse", action: () => downloadPDF("Schülerliste markierte Kurse"), default: true },
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
		const kursIdsAlle = new ArrayList<number>();
		for (const k of props.getErgebnismanager().getKursmenge())
			kursIdsAlle.add(k.id);
		result.push({ text: "Leere alle Kurse, beachte Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01_LEERE_ALLE_KURSE(false)) });
		result.push({ text: "Leere alle Kurse, ignoriere Fixierungen", action: async () => await props.updateKursSchuelerZuordnungen(props.getErgebnismanager().kursSchuelerUpdate_01_LEERE_ALLE_KURSE(true)) });
		if ((props.getKursauswahl().size() !== 0) && (props.getDatenmanager().kursGetAnzahl() !== props.getKursauswahl().size())) {
			// TODO
			result.push({ text: "Kursauswahl: Leere Kurse, beachte Fixierungen", action: async () => await props.updateKurseLeeren("leereKurseKursauswahl") });
			result.push({ text: "Kursauswahl: Leere Kurse, ignoriere Fixierungen", action: async () => await props.updateKurseLeeren("leereKurseKursauswahl") });
		}
		if (filter.kurs !== undefined) {
			// TODO
			const list = new ArrayList<number>();
			list.add(filter.kurs.id);
			result.push({ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Leere Kurs, beachte Fixierungen`, action: async () => await props.updateKurseLeeren("leereKursFilterKurs", list) });
			result.push({ text: `${props.getErgebnismanager().getOfKursName(filter.kurs.id)}: Leere Kurs, ignoriere Fixierungen`, action: async () => await props.updateKurseLeeren("leereKursFilterKurs", list) });
		}
		if (filter.fach !== undefined) {
			// TODO
			const list = new ArrayList<number>();
			let namen = "";
			for (const k of props.getErgebnismanager().getOfFachKursmenge(filter.fach)) {
				const kursart = filter.kursart;
				if ((kursart !== undefined) && (k.kursart !== kursart.id))
					continue;
				list.add(k.id);
				namen += props.getErgebnismanager().getOfKursName(k.id) + ', ';
			}
			namen = namen.slice(0, -2);
			if (list.size() > 0) {
				// TODO
				result.push({ text: `${namen}: Leere Kurse, beachte Fixierungen`, action: async () => await props.updateKurseLeeren("leereKurseFilterFach", list) });
				result.push({ text: `${namen}: Leere Kurse, ignoriere Fixierungen`, action: async () => await props.updateKurseLeeren("leereKurseFilterFach", list) });
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
				result.push({ text: "Fixiere alle Schüler mit Abiturkursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04i_SCHUELER_FIXIEREN_ALLER_AB())});
				result.push({ text: "Fixiere alle Schüler in Leistungskursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04e_SCHUELER_FIXIEREN_ALLER_LKS())});
				result.push({ text: "Fixiere alle Schüler im dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04f_SCHUELER_FIXIEREN_ALLER_AB3())});
				result.push({ text: "Fixiere alle Schüler im Leistungskurs und dritten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04g_SCHUELER_FIXIEREN_ALLER_LK_UND_AB3())});
				result.push({ text: "Fixiere alle Schüler im vierten Abiturfach", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04h_SCHUELER_FIXIEREN_ALLER_AB4())});
				result.push({ text: "Fixiere alle Schüler in schriftlichen Kursen", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04j_SCHUELER_FIXIEREN_ALLER_SCHRIFTLICHEN())});
			}
		} else {
			result.push({ text: "Kursauswahl: Fixiere Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(props.getKursauswahl()))});
			result.push({ text: "Kursauswahl: Löse fixierte Kurse", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_02b_KURS_FIXIERE_MENGE_IN_IHREN_SCHIENEN(props.getKursauswahl()))});
			result.push({ text: "Kursauswahl: Fixiere Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(props.getKursauswahl()))});
			result.push({ text: "Kursauswahl: Löse fixierte Schüler", action: async () => await props.regelnUpdate(props.getErgebnismanager().regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(props.getKursauswahl()))});
			if (hatAbiturkurse) {
				result.push({ text: "Kursauswahl: Fixiere Schüler mit Abiturkursen", action: async () => { /* TODO */ }});
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

<style lang="postcss" scoped>

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-12;
		grid-auto-rows: 100%;
		grid-template-columns: minmax(min-content, 1.5fr) minmax(18rem, 0.4fr) 1fr;
		grid-auto-columns: max-content;

		&.svws-blockungstabelle-hidden {
			grid-template-columns: 0 minmax(20rem, 0.15fr) 1fr;

			.s-gost-kursplanung-schueler-auswahl {
				@apply -ml-8 lg:-ml-12;
			}
		}
	}

</style>
