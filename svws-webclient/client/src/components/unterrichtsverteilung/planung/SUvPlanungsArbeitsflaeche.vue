<template>
	<div class="flex h-full min-h-0 flex-col p-3">
		<s-uv-lehrer-zuordnung-bearbeiten-modal v-if="editZuordnungId !== null"
			:id-zuordnung="editZuordnungId"
			:patch-zuordnung
			:remove-zuordnung
			@closed="editZuordnungId = null"
			@saved="zuordnungGeaendert" />
		<div class="uv-deaktivierbar mb-3 flex shrink-0 flex-wrap items-center gap-3" :inert="arbeitsmodusAktiv">
			<p class="min-w-0 flex-1 text-sm text-ui-secondary">{{ plan === null ? 'Planungsabschnitt auswählen' : `${besetzteAnzahl} von ${lerngruppen.length} Lerngruppen besetzt · ${offeneAnzahl} offen` }}</p>
			<svws-ui-button v-if="selectedLerngruppeId !== null && rechts !== 'lehrer'"
				type="secondary"
				@click="bereichWechseln('rechts', 'lehrer')">
				Lehrkräfte zuordnen
			</svws-ui-button>
			<svws-ui-button v-if="auswahl !== null" type="secondary" @click="auswahlAufheben">Auswahl aufheben</svws-ui-button>
			<svws-ui-button v-if="plan !== null && plan.id !== -1"
				type="secondary"
				@click="planungsabschnittOeffnen">
				Planungsabschnitt bearbeiten
			</svws-ui-button>
		</div>
		<output v-if="zuordnungMeldung" class="mb-3 text-sm text-ui-secondary">{{ zuordnungMeldung }}</output>
		<svws-ui-notification v-if="zuordnungFehler" type="error" class="mb-3">{{ zuordnungFehler }}</svws-ui-notification>
		<div ref="arbeitsbereiche" class="flex min-h-0 min-w-0 flex-1 overflow-x-auto">
			<template v-for="seite in seiten" :key="seite">
				<div v-if="seite === 'rechts'" role="separator" aria-label="Breite der Arbeitsbereiche ändern" aria-orientation="vertical"
					:inert="arbeitsmodusAktiv"
					class="mx-1 flex w-3 shrink-0 cursor-col-resize items-center justify-center rounded hover:bg-ui-selected"
					@pointerdown="breitenanpassungStarten" @pointermove="breiteAnpassen" @pointerup="breitenanpassungBeenden"
					@pointercancel="breitenanpassungBeenden">
					<span class="h-12 w-1 rounded bg-ui-brand/40" />
				</div>
				<div class="relative flex min-h-0 min-w-80"
					:class="[seite === 'links' ? 'shrink-0' : 'flex-1', bearbeiteteSeite === seite ? 'rounded-lg ring-2 ring-ui-brand' : '']"
					:style="seite === 'links' ? { flexBasis: `${linksBreite}%` } : undefined"
					@dragover="dragUeberBereich($event, seite)" @drop="ablegen($event, seite)">
					<div class="uv-deaktivierbar flex min-h-0 min-w-0 flex-1" :inert="bereichGesperrt(seite)">
						<s-uv-planungs-arbeitsbereich class="min-w-0 flex-1"
							:seite="seite === 'links' ? 'Links' : 'Rechts'"
							:art="bereichArt(seite)"
							:selected-lerngruppe-id
							:selected-lehrer-id
							:auswahl
							:sortier-auswahl="lehrerMitgliedschaft?.quelle === seite || (auswahlQuelle?.seite === seite && auswahlQuelle.art === bereichArt(seite)) ? null : auswahl"
							:auswahl-manager
							:mitgliedschaft-bearbeiten="lehrerMitgliedschaft?.quelle === seite"
							:mitgliedschaft-pending
							:verwendete-lehrer-ids
							:navigation-gesperrt="arbeitsmodusAktiv"
							@update:art="bereichWechseln(seite, $event)"
							:lehrer-auswaehlen="id => lehrerAuswaehlen(id, seite)"
							:auswaehlen="(art, id) => auswaehlen(art, id, seite)"
							:details-anzeigen="(art, id) => detailsOeffnen(art, id, seite)"
							:lehrer-zuordnen="id => lehrerZuordnungStarten(id, seite)"
							:zuordnung-bearbeiten="(id) => editZuordnungId = id"
							:erstellen="(art) => neuanlageStarten(art, seite)"
							:mitgliedschaft-bearbeitung-starten="() => lehrerMitgliedschaftStarten(seite)"
							:mitgliedschaft-bearbeitung-beenden="lehrerMitgliedschaftBeenden"
							:mitgliedschaft-entfernen="lehrerMitgliedschaftEntfernen">
							<template #details>
								<s-uv-lehrer-zuordnung-neu v-if="lehrerZuordnung?.seite === seite && selectedLerngruppeId !== null" :id-lerngruppe="selectedLerngruppeId"
									:id-lehrer="lehrerZuordnung.idLehrer" :assign-lehrer :schliessen="lehrerZuordnungBeenden" @saved="lehrerZugeordnet" />
								<s-uv-lerngruppe-neu v-else-if="neu?.seite === seite && neu.art === 'lerngruppen'" :selected-schueler="entwurfSchueler"
									:schueler-mitglieder="entwurfSchuelerDaten.mitglieder" :schueler-bearbeitung="schuelerEntwurfBearbeitung"
									:abbrechen="neuanlageAbbrechen" @created="eintragErstellt" :schueler-bearbeiten="() => schuelerEntwurfBearbeitung = true"
									:schueler-entfernen="entwurfSchuelerEntfernen" @schuelergruppe-change="entwurfSchuelergruppeWechseln" />
								<s-uv-klasse-neu v-else-if="neu?.seite === seite && neu.art === 'klassen'" :abbrechen="neuanlageAbbrechen" @created="eintragErstellt" />
								<s-uv-schiene-neu v-else-if="neu?.seite === seite && neu.art === 'schienen'" :abbrechen="neuanlageAbbrechen" @created="eintragErstellt" />
								<s-uv-regel-neu v-else-if="neu?.seite === seite && neu.art === 'regeln'"
									:auswahl-manager
									:abbrechen="neuanlageAbbrechen"
									@created="eintragErstellt" />
								<s-uv-schuelergruppe-neu v-else-if="neu?.seite === seite && neu.art === 'schuelergruppen' && neu.ziel !== undefined" :ziel="neu.ziel"
									:abbrechen="neuanlageAbbrechen" @saved="neuanlageAbbrechen" />
								<section v-else-if="planDetail && seite === 'rechts'" class="flex min-h-0 flex-1 flex-col">
									<div class="flex items-center justify-between border-b border-ui-10 p-3">
										<h3 class="font-semibold">Planungsabschnitt</h3>
										<svws-ui-button type="secondary" :disabled="bearbeitung !== null" @click="detailSchliessen(seite)">
											Schließen
										</svws-ui-button>
									</div>
									<div class="min-h-0 flex-1 overflow-auto">
										<s-uv-planungsabschnitt-details :manager="planungsabschnittManager" :patch="patchPlanungsabschnitt" :benutzer-kompetenzen :bearbeitung
											:goto-zeitraster="gotoZeitrasterWennFrei" :bearbeiten="bearbeitungStarten" />
									</div>
								</section>
								<s-uv-planungs-detail v-else-if="detail !== null"
									:key="`${detail.art}-${detail.id}`"
									:schueler-aenderungsentwuerfe
									:art="detail.art"
									:id="detail.id"
									:schuljahr
									:goto-grunddaten
									:goto-schild-schueler
									:add-schueler
									:schueler-entfernen="schuelerZuordnungEntfernen"
									:zuordnung-pending
									:bearbeitung
									:schliessen="() => detailSchliessen(seite)"
									:auswaehlen="(art, id) => auswaehlen(art, id, seite)"
									:bearbeiten="(art, id) => bearbeitungStarten(art, id, seite)"
									:schuelergruppe-erstellen="(art, id, bezeichnung) => schuelergruppeAnlageStarten(art, id, bezeichnung, seite)"
									@deleted="eintragGeloescht" />
								<p v-else class="p-4 text-sm text-ui-secondary">Eintrag in der anderen Ansicht auswählen.</p>
							</template>
						</s-uv-planungs-arbeitsbereich>
					</div>
					<s-uv-zuordnung-auswahl v-if="bearbeitung !== null && bearbeitung.seite === seite"
						:key="`${bearbeitung.art}-${bearbeitung.idZiel}`"
						:bearbeitung
						:aenderung-aktiv="zuordnungPending"
						:hinzufuegen="zuordnungHinzufuegen"
						class="absolute inset-0 z-20"
						:schliessen="bearbeitungBeenden" />
					<s-uv-planungs-lehrer-auswahl v-if="lehrerMitgliedschaft?.seite === seite" :pending="mitgliedschaftPending" class="absolute inset-0 z-20"
						:schliessen="lehrerMitgliedschaftBeenden" :hinzufuegen="lehrerMitgliedschaftHinzufuegen" />
					<s-uv-lerngruppe-neu-schueler-auswahl v-if="schuelerEntwurfBearbeitung && neu?.seite === gegenseite(seite)"
						:items="entwurfSchuelerDaten.kandidaten"
						class="absolute inset-0 z-20"
						:schliessen="() => schuelerEntwurfBearbeitung = false"
						:hinzufuegen="entwurfSchuelerHinzufuegen" />
					<div v-if="bearbeitung !== null && bearbeitung.seite === gegenseite(seite)" class="pointer-events-none absolute inset-x-3 top-3 z-10 flex justify-center">
						<span class="rounded border border-ui-brand bg-ui px-3 py-1 text-xs font-semibold text-ui-brand shadow-sm">Hier ablegen oder {{ seite === 'links' ? 'rechts' : 'links' }} Plus wählen</span>
					</div>
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, onUnmounted, provide, ref, shallowReactive, shallowRef, watch } from 'vue';

	import type { UvLerngruppenLehrer } from '@core/core/data/uv/UvLerngruppenLehrer';
	import type { UvPlanungsabschnitt } from '@core/core/data/uv/UvPlanungsabschnitt';
	import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
	import type { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { useUvState } from '@ui/states/UvState';
	import type { UvPlanungsabschnitteListeManager } from '@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager';

	import SUvPlanungsabschnittDetails from './details/SUvPlanungsabschnitt.vue';
	import SUvKlasseNeu from './SUvKlasseNeu.vue';
	import SUvLehrerZuordnungBearbeitenModal from './SUvLehrerZuordnungBearbeitenModal.vue';
	import SUvLehrerZuordnungNeu from './SUvLehrerZuordnungNeu.vue';
	import SUvLerngruppeNeu from './SUvLerngruppeNeu.vue';
	import SUvLerngruppeNeuSchuelerAuswahl from './SUvLerngruppeNeuSchuelerAuswahl.vue';
	import SUvPlanungsArbeitsbereich from './SUvPlanungsArbeitsbereich.vue';
	import SUvPlanungsDetail from './SUvPlanungsDetail.vue';
	import SUvPlanungsLehrerAuswahl from './SUvPlanungsLehrerAuswahl.vue';
	import SUvRegelNeu from './SUvRegelNeu.vue';
	import SUvSchieneNeu from './SUvSchieneNeu.vue';
	import SUvSchuelergruppeNeu from './SUvSchuelergruppeNeu.vue';
	import SUvZuordnungAuswahl from './SUvZuordnungAuswahl.vue';
	import { type UvDragAktion, type UvDragDaten, uvDragDatenLesen, uvDragFormat, uvDragLehrerAktionLesen } from './UvDragDaten';
	import { istUvPlanungsAuswahlArt, type UvBereichArt, type UvDetailZiel, type UvPlanungsAuswahl, type UvSchuelerAenderungsentwurf, type UvZuordnungArt, type UvZuordnungBearbeitung } from './UvPlanung';
	import { uvPlanungsManagerKey } from './UvPlanungsKontext';
	import { UvPlanungsManager } from './UvPlanungsManager';

	const props = defineProps<{
		detailZiel: UvDetailZiel | null;
		gotoGrunddaten: (art: 'lehrer' | 'stundentafeln' | 'raeume' | 'zeitraster', id: number) => Promise<void>;
		gotoSchildSchueler: (id: number) => Promise<void>;
		gotoDetail: (ziel: UvDetailZiel) => Promise<void>;
		gotoOverview: () => Promise<void>;
		planungsabschnittManager: () => UvPlanungsabschnitteListeManager;
		patchPlanungsabschnitt: (data: Partial<UvPlanungsabschnitt>) => Promise<boolean>;
		benutzerKompetenzen: Set<BenutzerKompetenz>;
		schuljahr: number;
		assignLehrer: (idLerngruppe: number, idLehrer: number, wochenstunden: number, angerechnet: number) => Promise<void>;
		patchZuordnung: (id: number, data: Partial<UvLerngruppenLehrer>) => Promise<void>;
		removeZuordnung: (id: number) => Promise<void>;
		addSchueler: (idSchuelergruppe: number, idSchueler: number) => Promise<void>;
		removeSchueler: (idSchuelergruppe: number, idSchueler: number) => Promise<void>;
	}>();

	// Bearbeitungsmodi und die jeweils benötigten Daten.
	type NeuModus = {
		art: 'lerngruppen' | 'klassen' | 'schienen' | 'regeln' | 'schuelergruppen';
		seite: 'links' | 'rechts';
		vorherigeArt: UvBereichArt;
		ziel?: {
			art: 'klassen' | 'kurse';
			id: number;
			bezeichnung: string;
		};
	};

	type LehrerMitgliedschaftModus = {
		quelle: 'links' | 'rechts';
		seite: 'links' | 'rechts';
	};

	type LehrerZuordnungModus = {
		idLehrer: number;
		seite: 'links' | 'rechts';
		vorherigeArt: UvBereichArt;
	};

	type Arbeitsmodus =
		| { typ: 'neu'; daten: NeuModus }
		| { typ: 'bearbeitung'; daten: UvZuordnungBearbeitung }
		| { typ: 'lehrer-mitgliedschaft'; daten: LehrerMitgliedschaftModus }
		| { typ: 'lehrer-zuordnung'; daten: LehrerZuordnungModus };

	const state = useUvState();
	// Die Detailkomponente wird bei einem Schülerwechsel neu erzeugt.
	const schuelerAenderungsentwuerfe = shallowRef(shallowReactive(new Map<string, UvSchuelerAenderungsentwurf>()));
	watch(() => state.uvManager, () => {
		schuelerAenderungsentwuerfe.value = shallowReactive(new Map<string, UvSchuelerAenderungsentwurf>());
	}, { flush: 'sync' });

	const plan = computed(() => state.planungsabschnitt);
	const auswahlManager = computed(() => plan.value === null || plan.value.id === -1 ? null : new UvPlanungsManager(state.uvManager, plan.value, state.regelliste));

	provide(uvPlanungsManagerKey, auswahlManager);

	// Ansichten und Breiten der beiden Seiten.
	const seiten = ['links', 'rechts'] as const;
	const links = ref<UvBereichArt>('matrix');
	const rechts = ref<UvBereichArt>('lehrer');
	const arbeitsbereiche = ref<HTMLElement | null>(null);
	const linksBreite = ref(55);
	const breitenanpassungAktiv = ref(false);

	// Auswahl und die beim Schließen wiederherzustellenden Detailansichten.
	const auswahl = ref<UvPlanungsAuswahl | null>(null);
	const auswahlQuelle = ref<{ seite: 'links' | 'rechts'; art: UvBereichArt } | null>(null);
	const selectedLerngruppeId = computed(() => auswahl.value?.art === 'lerngruppen' ? auswahl.value.id : null);
	const selectedLehrerId = computed(() => auswahl.value?.art === 'lehrer' ? auswahl.value.id : null);
	const detail = ref<UvPlanungsAuswahl | null>(null);
	const planDetail = ref(false);
	const vorherigeDetailAnsicht = ref<{ links: UvBereichArt | null; rechts: UvBereichArt | null }>({ links: null, rechts: null });
	const editZuordnungId = ref<number | null>(null);

	// Der aktive Modus bestimmt Bearbeitungsseite, Hervorhebung und Sperrung.
	const arbeitsmodus = ref<Arbeitsmodus | null>(null);
	const neu = computed(() => arbeitsmodus.value?.typ === 'neu' ? arbeitsmodus.value.daten : null);
	const bearbeitung = computed(() => arbeitsmodus.value?.typ === 'bearbeitung' ? arbeitsmodus.value.daten : null);
	const lehrerMitgliedschaft = computed(() => arbeitsmodus.value?.typ === 'lehrer-mitgliedschaft' ? arbeitsmodus.value.daten : null);
	const lehrerZuordnung = computed(() => arbeitsmodus.value?.typ === 'lehrer-zuordnung' ? arbeitsmodus.value.daten : null);
	const arbeitsmodusAktiv = computed(() => arbeitsmodus.value !== null);
	const bearbeiteteSeite = computed(() => {
		const modus = arbeitsmodus.value;
		if (modus === null) {
			return null;
		}

		if (modus.typ === 'bearbeitung') {
			return gegenseite(modus.daten.seite);
		}
		return modus.typ === 'lehrer-mitgliedschaft' ? modus.daten.quelle : modus.daten.seite;
	});

	function bereichGesperrt(seite: 'links' | 'rechts'): boolean {
		return arbeitsmodusAktiv.value && bearbeiteteSeite.value !== seite;
	}

	// Noch nicht gespeicherte Schülerauswahl einer neuen Lerngruppe.
	const schuelerEntwurfBearbeitung = ref(false);
	const entwurfSchuelergruppeId = ref<number | null>(null);
	const entwurfSchueler = ref<UvPlanungsabschnittSchueler[]>([]);
	const entwurfSchuelerDaten = computed(() => auswahlManager.value?.schuelerEntwurf(entwurfSchuelergruppeId.value, entwurfSchueler.value) ?? { mitglieder: [], kandidaten: [] });

	const mitgliedschaftPending = ref(false);
	const mitgliedschaftDetailEntfernt = ref(false);
	const zuordnungPending = ref(false);
	const zuordnungMeldung = ref('');
	const zuordnungFehler = ref('');

	const lerngruppen = computed(() => plan.value === null || plan.value.id === -1 ? [] : [...state.uvManager.lerngruppeGetMengeByPlanungsabschnitt(plan.value)]);
	const besetzteAnzahl = computed(() => auswahlManager.value?.besetzteLerngruppenAnzahl() ?? 0);
	const offeneAnzahl = computed(() => lerngruppen.value.length - besetzteAnzahl.value);
	const verwendeteLehrerIds = computed(() => lehrerMitgliedschaft.value === null ? new Set<number>() : auswahlManager.value?.lehrerPlanungsabschnittAuswahl().verwendet ?? new Set<number>());

	// Änderungen am Abschnitt und am Routing mit dem lokalen UI-Zustand abgleichen.
	watch(arbeitsmodusAktiv, value => {
		state.setBearbeitungAktiv(value);
	});

	watch(lerngruppen, items => {
		if (selectedLerngruppeId.value !== null && !items.some(item => item.id === selectedLerngruppeId.value)) {
			auswahl.value = null;
			auswahlQuelle.value = null;
		}
	});

	watch([
			() => props.detailZiel?.art,
			() => props.detailZiel !== null && 'id' in props.detailZiel ? props.detailZiel.id : null,
			() => plan.value?.id,
		],
		(werte, vorherigeWerte) => {
			if (werte[2] !== vorherigeWerte[2]) {
				auswahl.value = null;
				auswahlQuelle.value = null;
				editZuordnungId.value = null;
				zuordnungMeldung.value = '';
				zuordnungFehler.value = '';
				vorherigeDetailAnsicht.value = { links: null, rechts: null };
			}

			arbeitsmodus.value = null;
			mitgliedschaftDetailEntfernt.value = false;
			schuelerEntwurfZuruecksetzen();

			const ziel = props.detailZiel;
			detail.value = ziel === null || ziel.art === 'abschnitt' ? null : ziel;
			planDetail.value = ziel?.art === 'abschnitt';

			if (planDetail.value) {
				detailBereichOeffnen('rechts');
			}
			if (detail.value !== null) {
				if (auswahl.value?.art !== detail.value.art || auswahl.value.id !== detail.value.id) {
					auswahlQuelle.value = null;
				}
				auswahl.value = detail.value;
				if (links.value !== 'details' && rechts.value !== 'details') {
					detailBereichOeffnen('rechts');
				}
			}
		}, { immediate: true });

	onUnmounted(() => {
		arbeitsmodus.value = null;
		state.setBearbeitungAktiv(false);
	});

	// Zugriff auf die beiden Seiten.
	function gegenseite(seite: 'links' | 'rechts'): 'links' | 'rechts' {
		return seite === 'links' ? 'rechts' : 'links';
	}

	function bereichArt(seite: 'links' | 'rechts'): UvBereichArt {
		return seite === 'links' ? links.value : rechts.value;
	}

	function bereichWertSetzen(seite: 'links' | 'rechts', art: UvBereichArt): void {
		if (seite === 'links') {
			links.value = art;
		} else {
			rechts.value = art;
		}
	}


	// Gemeinsamer Einstieg und Abschluss der Bearbeitungsmodi.
	function modusStarten(modus: Arbeitsmodus): boolean {
		if (arbeitsmodus.value !== null) {
			return false;
		}

		arbeitsmodus.value = modus;
		zuordnungFehler.value = '';
		return true;
	}

	function modusBeenden(typ: Arbeitsmodus['typ']): void {
		if (arbeitsmodus.value?.typ === typ) {
			arbeitsmodus.value = null;
			zuordnungFehler.value = '';
		}
	}

	function detailModusZiel(quelle: 'links' | 'rechts'): { seite: 'links' | 'rechts'; vorherigeArt: UvBereichArt } {
		const seite = gegenseite(quelle);
		return { seite, vorherigeArt: bereichArt(seite) };
	}

	function detailModusStarten(modus: Extract<Arbeitsmodus, { typ: 'neu' | 'lehrer-zuordnung' }>): void {
		if (modusStarten(modus)) {
			bereichWertSetzen(modus.daten.seite, 'details');
		}
	}

	function detailModusBeenden(typ: 'neu' | 'lehrer-zuordnung'): void {
		const modus = arbeitsmodus.value;
		if (modus?.typ !== typ) {
			return;
		}

		const { seite, vorherigeArt } = modus.daten;
		modusBeenden(typ);
		if (vorherigeArt !== 'details') {
			bereichWertSetzen(seite, vorherigeArt);
		}
	}

	// Auswahl, Detailansichten und Navigation.
	async function auswaehlen(art: UvBereichArt, id: number, quelle: 'links' | 'rechts'): Promise<void> {
		const manager = auswahlManager.value;
		if (arbeitsmodusAktiv.value || manager === null) {
			return;
		}

		if (art === 'matrix') {
			art = 'lerngruppen';
		}

		if (!istUvPlanungsAuswahlArt(art)) {
			return;
		}

		const entscheidung = manager.auswaehlen(auswahl.value, art, id, quelle, links.value, rechts.value, detail.value !== null);
		auswahl.value = entscheidung.auswahl;
		auswahlQuelle.value = entscheidung.auswahl === null ? null : { seite: quelle, art: bereichArt(quelle) };
		if (entscheidung.navigation === 'detail') {
			await props.gotoDetail({ art, id });
		} else if (entscheidung.navigation === 'uebersicht') {
			await props.gotoOverview();
		}
	}

	async function lehrerAuswaehlen(id: number | null, quelle: 'links' | 'rechts'): Promise<void> {
		if (id === null) {
			await auswahlAufheben();
		} else {
			await auswaehlen('lehrer', id, quelle);
		}
	}

	async function auswahlAufheben(): Promise<void> {
		if (arbeitsmodusAktiv.value) {
			return;
		}

		auswahl.value = null;
		auswahlQuelle.value = null;
		await props.gotoOverview();
	}

	async function bereichWechseln(seite: 'links' | 'rechts', art: UvBereichArt): Promise<void> {
		const manager = auswahlManager.value;
		if (arbeitsmodusAktiv.value || manager === null) {
			return;
		}

		if (art === 'details') {
			detailBereichOeffnen(seite);
		} else {
			vorherigeDetailAnsicht.value[seite] = null;
			bereichWertSetzen(seite, art);
		}

		const navigation = manager.ansichtWechseln(art, links.value, rechts.value, auswahl.value, detail.value !== null || planDetail.value);
		if (navigation === 'detail' && auswahl.value !== null) {
			await props.gotoDetail(auswahl.value);
		} else if (navigation === 'uebersicht') {
			await props.gotoOverview();
		}
	}

	function detailBereichOeffnen(seite: 'links' | 'rechts'): void {
		const aktuell = bereichArt(seite);
		if (aktuell === 'details') {
			return;
		}

		vorherigeDetailAnsicht.value[seite] = aktuell;
		bereichWertSetzen(seite, 'details');
	}

	async function detailsOeffnen(art: UvBereichArt, id: number, quelle: 'links' | 'rechts'): Promise<void> {
		const ziel = auswahl.value;
		if (arbeitsmodusAktiv.value || ziel?.art !== art || ziel.id !== id) {
			return;
		}

		detailBereichOeffnen(gegenseite(quelle));
		await props.gotoDetail(ziel);
	}

	async function detailSchliessen(seite: 'links' | 'rechts'): Promise<void> {
		if (arbeitsmodusAktiv.value) {
			return;
		}

		const vorherigeArt = vorherigeDetailAnsicht.value[seite] ?? (seite === 'links' ? 'matrix' : 'lehrer');
		vorherigeDetailAnsicht.value[seite] = null;
		bereichWertSetzen(seite, vorherigeArt);
		if (links.value !== 'details' && rechts.value !== 'details') {
			await props.gotoOverview();
		}
	}

	async function planungsabschnittOeffnen(): Promise<void> {
		if (arbeitsmodusAktiv.value) {
			return;
		}

		detailBereichOeffnen('rechts');
		await props.gotoDetail({ art: 'abschnitt' });
	}

	async function gotoZeitrasterWennFrei(id: number): Promise<void> {
		if (bearbeitung.value === null) {
			await props.gotoGrunddaten('zeitraster', id);
		}
	}

	async function eintragGeloescht(_art: UvBereichArt, _id: number): Promise<void> {
		auswahl.value = null;
		auswahlQuelle.value = null;
		await props.gotoOverview();
	}

	// Neue Objekte auf der Gegenseite anlegen.
	function neuanlageStarten(art: 'lerngruppen' | 'klassen' | 'schienen' | 'regeln', quelle: 'links' | 'rechts'): void {
		if (!state.hatKompetenzAendern || arbeitsmodusAktiv.value) {
			return;
		}

		const { seite, vorherigeArt } = detailModusZiel(quelle);
		schuelerEntwurfZuruecksetzen();
		detailModusStarten({ typ: 'neu', daten: { art, seite, vorherigeArt } });
	}

	function schuelergruppeAnlageStarten(art: 'klassen' | 'kurse', id: number, bezeichnung: string, quelle: 'links' | 'rechts'): void {
		if (!state.hatKompetenzAendern || arbeitsmodusAktiv.value) {
			return;
		}

		const { seite, vorherigeArt } = detailModusZiel(quelle);
		detailModusStarten({ typ: 'neu', daten: { art: 'schuelergruppen', seite, vorherigeArt, ziel: { art, id, bezeichnung } } });
	}

	function neuanlageAbbrechen(): void {
		const entwurf = neu.value;
		if (entwurf === null) {
			return;
		}

		schuelerEntwurfZuruecksetzen();
		detailModusBeenden('neu');
	}

	async function eintragErstellt(id: number): Promise<void> {
		const art = neu.value?.art;
		if (art === undefined || art === 'schuelergruppen') {
			return;
		}

		modusBeenden('neu');
		schuelerEntwurfZuruecksetzen();
		auswahl.value = { art, id };
		auswahlQuelle.value = null;
		zuordnungFehler.value = '';
		await props.gotoDetail({ art, id });
	}

	// Schülerauswahl vor dem Speichern einer Lerngruppe.
	function schuelerEntwurfZuruecksetzen(): void {
		schuelerEntwurfBearbeitung.value = false;
		entwurfSchueler.value = [];
		entwurfSchuelergruppeId.value = null;
	}

	function entwurfSchuelergruppeWechseln(id: number | null): void {
		entwurfSchuelergruppeId.value = id;
		entwurfSchueler.value = [];
	}

	function entwurfSchuelerHinzufuegen(id: number): void {
		const schueler = entwurfSchuelerDaten.value.kandidaten.find(item => item.idSchueler === id);
		if (schueler !== undefined) {
			entwurfSchueler.value = [...entwurfSchueler.value, schueler];
		}
	}

	function entwurfSchuelerEntfernen(id: number): void {
		entwurfSchueler.value = entwurfSchueler.value.filter(item => item.idSchueler !== id);
	}

	// Bestehende Zuordnungen bearbeiten.
	function bearbeitungStarten(art: UvZuordnungArt, idZiel: number, seite: 'links' | 'rechts' = 'rechts'): void {
		if (!state.hatKompetenzAendern || arbeitsmodusAktiv.value || (detail.value === null && !planDetail.value)) {
			return;
		}

		modusStarten({ typ: 'bearbeitung', daten: { art, idZiel, seite: gegenseite(planDetail.value ? 'rechts' : seite) } });
	}

	function bearbeitungBeenden(): void {
		if (!zuordnungPending.value) {
			modusBeenden('bearbeitung');
		}
	}

	async function zuordnungHinzufuegen(id: number): Promise<void> {
		const aktuelleBearbeitung = bearbeitung.value;
		if (aktuelleBearbeitung === null || zuordnungPending.value || !state.hatKompetenzAendern) {
			return;
		}

		zuordnungPending.value = true;
		zuordnungFehler.value = '';

		try {
			await state.planung.addPlanungsZuordnung(aktuelleBearbeitung.art, aktuelleBearbeitung.idZiel, id);
		} finally {
			zuordnungPending.value = false;
		}
	}

	async function schuelerZuordnungEntfernen(idGruppe: number, idSchueler: number): Promise<void> {
		const aktuelleBearbeitung = bearbeitung.value;
		if (aktuelleBearbeitung?.art !== 'gruppe-schueler' || aktuelleBearbeitung.idZiel !== idGruppe
			|| zuordnungPending.value || !state.hatKompetenzAendern) {
			return;
		}

		const aktuellesDetail = detail.value;
		const manager = state.uvManager;
		zuordnungPending.value = true;
		zuordnungFehler.value = '';

		try {
			await props.removeSchueler(idGruppe, idSchueler);
		} catch (error) {
			if (bearbeitung.value === aktuelleBearbeitung && detail.value === aktuellesDetail && state.uvManager === manager) {
				zuordnungFehler.value = error instanceof Error ? error.message : 'Der Schüler konnte nicht entfernt werden.';
			}
		} finally {
			zuordnungPending.value = false;
		}
	}

	// Lehrkräfte zum Planungsabschnitt hinzufügen oder daraus entfernen.
	function lehrerMitgliedschaftStarten(quelle: 'links' | 'rechts'): void {
		if (!state.hatKompetenzAendern || arbeitsmodusAktiv.value || plan.value === null || plan.value.id === -1 || bereichArt(quelle) !== 'lehrer') {
			return;
		}

		modusStarten({ typ: 'lehrer-mitgliedschaft', daten: { quelle, seite: gegenseite(quelle) } });
	}

	async function lehrerMitgliedschaftBeenden(): Promise<void> {
		if (mitgliedschaftPending.value) {
			return;
		}

		modusBeenden('lehrer-mitgliedschaft');
		if (mitgliedschaftDetailEntfernt.value) {
			mitgliedschaftDetailEntfernt.value = false;
			await props.gotoOverview();
		}
	}

	async function lehrerMitgliedschaftHinzufuegen(ids: number[]): Promise<boolean> {
		if (lehrerMitgliedschaft.value === null || mitgliedschaftPending.value || ids.length === 0) {
			return false;
		}

		mitgliedschaftPending.value = true;
		zuordnungFehler.value = '';

		try {
			await state.planung.addLehrerToPlanungsabschnitt(ids);
			return true;
		} catch (error) {
			zuordnungFehler.value = error instanceof Error ? error.message : 'Die Lehrkräfte konnten nicht hinzugefügt werden.';
			return false;
		} finally {
			mitgliedschaftPending.value = false;
		}
	}

	async function lehrerMitgliedschaftEntfernen(id: number): Promise<void> {
		if (lehrerMitgliedschaft.value === null || mitgliedschaftPending.value) {
			return;
		}

		mitgliedschaftPending.value = true;
		zuordnungFehler.value = '';

		try {
			await state.planung.removeLehrerFromPlanungsabschnitt([id]);
			if (selectedLehrerId.value === id) {
				auswahl.value = null;
				auswahlQuelle.value = null;
			}
			if (detail.value?.art === 'lehrer' && detail.value.id === id) {
				detail.value = null;
				mitgliedschaftDetailEntfernt.value = true;
			}
		} catch (error) {
			zuordnungFehler.value = error instanceof Error ? error.message : 'Die Lehrkraft konnte nicht entfernt werden.';
		} finally {
			mitgliedschaftPending.value = false;
		}
	}

	// Einzelne Lehrkraftzuordnungen und deren Rückmeldungen.
	function lehrerZuordnungStarten(idLehrer: number, quelle: 'links' | 'rechts'): void {
		if (arbeitsmodusAktiv.value || selectedLerngruppeId.value === null) {
			return;
		}

		const { seite, vorherigeArt } = detailModusZiel(quelle);
		detailModusStarten({ typ: 'lehrer-zuordnung', daten: { idLehrer, seite, vorherigeArt } });
		zuordnungMeldung.value = '';
	}

	function lehrerZuordnungBeenden(): void {
		const entwurf = lehrerZuordnung.value;
		if (entwurf !== null) {
			detailModusBeenden('lehrer-zuordnung');
		}
	}

	function lehrerZugeordnet(): void {
		lehrerZuordnungBeenden();
		zuordnungMeldung.value = 'Die Lehrkraft wurde der Lerngruppe zugeordnet.';
	}

	function zuordnungGeaendert(aktion: 'geändert' | 'entfernt'): void {
		zuordnungMeldung.value = `Die Zuordnung wurde ${aktion}.`;
	}

	// Gültige Drop-Ziele hängen vom aktiven Modus und der Seite ab.

	function dropEffekt(seite: 'links' | 'rechts', aktion?: UvDragAktion): 'copy' | 'move' | null {
		const mitgliedschaft = lehrerMitgliedschaft.value;
		if (mitgliedschaft !== null) {
			if (aktion === undefined || mitgliedschaftPending.value) {
				return null;
			}
			const ziel = aktion === 'hinzufuegen' ? mitgliedschaft.quelle : mitgliedschaft.seite;
			return seite === ziel ? 'move' : null;
		}

		const entwurf = neu.value;
		const zuordnung = bearbeitung.value;
		let zielAktion: UvDragAktion;
		if (schuelerEntwurfBearbeitung.value && entwurf !== null) {
			zielAktion = seite === entwurf.seite ? 'hinzufuegen' : 'entfernen';
		} else if (zuordnung !== null && !zuordnungPending.value) {
			if (seite === zuordnung.seite && zuordnung.art !== 'gruppe-schueler') {
				return null;
			}
			zielAktion = seite === zuordnung.seite ? 'entfernen' : 'hinzufuegen';
		} else {
			return null;
		}
		if (aktion !== undefined && aktion !== zielAktion) {
			return null;
		}
		return zielAktion === 'hinzufuegen' ? 'copy' : 'move';
	}

	function dragUeberBereich(event: DragEvent, seite: 'links' | 'rechts'): void {
		const transfer = event.dataTransfer;
		if (transfer?.types.includes(uvDragFormat) !== true) {
			return;
		}
		const effekt = dropEffekt(seite, uvDragLehrerAktionLesen(transfer) ?? undefined);
		if (effekt !== null) {
			event.preventDefault();
			transfer.dropEffect = effekt;
		}
	}

	function dropDatenPassenZumModus(daten: UvDragDaten): boolean {
		switch (daten.typ) {
			case 'lehrer-mitgliedschaft': {
				return lehrerMitgliedschaft.value !== null;
			}
			case 'lerngruppe-entwurf': {
				return schuelerEntwurfBearbeitung.value && neu.value !== null;
			}
			case 'zuordnung': {
				const modus = bearbeitung.value;
				return modus?.art === daten.art && modus.idZiel === daten.idZiel;
			}
		}
	}

	async function ablegen(event: DragEvent, seite: 'links' | 'rechts'): Promise<void> {
		const daten = event.dataTransfer === null ? null : uvDragDatenLesen(event.dataTransfer);
		if (daten === null || !dropDatenPassenZumModus(daten) || dropEffekt(seite, daten.aktion) === null) {
			return;
		}

		event.preventDefault();

		try {
			switch (daten.typ) {
				case 'lehrer-mitgliedschaft': {
					if (daten.aktion === 'hinzufuegen') {
						await lehrerMitgliedschaftHinzufuegen([daten.id]);
					} else {
						await lehrerMitgliedschaftEntfernen(daten.id);
					}
					break;
				}
				case 'lerngruppe-entwurf': {
					if (daten.aktion === 'hinzufuegen') {
						entwurfSchuelerHinzufuegen(daten.id);
					} else {
						entwurfSchuelerEntfernen(daten.id);
					}
					break;
				}
				case 'zuordnung': {
					if (daten.aktion === 'hinzufuegen') {
						await zuordnungHinzufuegen(daten.id);
					} else {
						await schuelerZuordnungEntfernen(daten.idZiel, daten.id);
					}
					break;
				}
			}
		} catch (error) {
			zuordnungFehler.value = error instanceof Error ? error.message : 'Die Änderung konnte nicht gespeichert werden.';
		}
	}

	// Breitenänderung mit dem verschiebbaren Trenner.
	function breitenanpassungStarten(event: PointerEvent): void {
		breitenanpassungAktiv.value = true;
		(event.currentTarget as HTMLElement).setPointerCapture(event.pointerId);
	}

	function breiteAnpassen(event: PointerEvent): void {
		const container = arbeitsbereiche.value;
		if (!breitenanpassungAktiv.value || container === null) {
			return;
		}

		const rect = container.getBoundingClientRect();
		linksBreite.value = Math.max(25, Math.min(75, Math.round((event.clientX - rect.left) / rect.width * 100)));
	}

	function breitenanpassungBeenden(event: PointerEvent): void {
		breitenanpassungAktiv.value = false;
		const element = event.currentTarget as HTMLElement;
		if (element.hasPointerCapture(event.pointerId)) {
			element.releasePointerCapture(event.pointerId);
		}
	}

</script>
