<template>
	<div v-if="ladeFehler" class="page p-4">
		<p>Die Leitungsdaten konnten für die Konferenzübersicht nicht geladen werden.</p>
	</div>
	<div v-else-if="manager === null" class="page p-4">
		<p>Die Konferenzübersicht wird geladen …</p>
	</div>
	<div v-else-if="berechtigt" class="page page-flex-col gap-3 p-3 konferenz-druck-seite">
		<div v-if="punkteToggleSichtbar" class="flex items-center justify-end gap-2 text-black print:!hidden">
			<span>Noten</span>
			<svws-ui-checkbox v-model="punkteAnsicht" type="toggle">Punkte</svws-ui-checkbox>
		</div>
		<div v-if="mahnungsToggleSichtbar" class="flex items-center justify-end gap-2 text-black print:!hidden">
			<svws-ui-checkbox v-model="mahnungsAnsicht" type="toggle">Mahnungsübersicht</svws-ui-checkbox>
		</div>
		<div class="flex justify-end print:!hidden">
			<svws-ui-button @click="druckeKonferenzuebersicht">
				<span class="icon i-ri-printer-line" />
				Drucken
			</svws-ui-button>
		</div>
		<header>
			<h1 class="text-headline-lg konferenz-titel">
				<span class="konferenz-titel-bildschirm">Konferenzübersicht</span>
				<span class="konferenz-titel-druck">Konferenzübersicht {{ klassenLabelDruck }} - {{ druckdatum }}</span>
			</h1>
			<p class="text-black">Fächer, Fachnoten und Teilleistungen aller Schüler der ausgewählten Klasse.</p>
		</header>
		<div v-if="schuelerZeilenAngezeigt.length === 0" class="rounded-lg bg-ui-75 p-4 text-black">
			{{ mahnungsAnsicht ? 'Keine gefährdeten Schüler in der ausgewählten Klasse.' : 'Keine Schüler in der ausgewählten Klasse.' }}
		</div>
		<div v-else class="min-h-0 flex-1 overflow-auto konferenz-druck-liste">
			<div class="flex w-fit min-w-0 flex-col items-start gap-8 konferenz-druck-karten">
				<article v-for="zeile in schuelerZeilenAngezeigt" :key="`${zeile.klasse.id}-${zeile.schueler.id}`" class="w-fit max-w-full overflow-hidden rounded-lg border border-ui-50 konferenz-schueler-tabelle">
					<header class="flex items-baseline gap-2 border-b border-ui-50 bg-ui px-3 py-1.5 konferenz-schueler-kopf">
						<h2 class="text-[1.3125rem] font-semibold konferenz-schueler-name">{{ zeile.schueler.nachname }}, {{ zeile.schueler.vorname }}</h2>
						<span class="text-xs text-black konferenz-klassenkuerzel">{{ zeile.klasse.kuerzelAnzeige ?? zeile.klasse.kuerzel }}</span>
					</header>
					<div class="overflow-x-auto konferenz-druck-scroll">
						<div class="min-w-max">
							<div class="grid konferenz-raster border-b border-ui-50 bg-ui-75" :style="rasterStyle(zeile)">
								<div class="border-r border-ui-50 px-1 py-1 text-center text-xs font-semibold uppercase">Fach</div>
								<div v-for="fach in zeile.faecher" :key="fach.fachID" class="border-r border-ui-50 px-1 py-1 text-center last:border-r-0" :class="fach.lerngruppe.toLocaleUpperCase().includes('LK') ? 'bg-sky-100' : ''">
									<div class="konferenz-kopftext text-sm font-bold" :title="fach.lerngruppe">{{ fach.lerngruppe || fach.kuerzel }}</div>
									<div class="konferenz-kopftext text-xs text-black" :title="fach.lehrkraefte">{{ fach.lehrkraefte }}</div>
								</div>
							</div>
							<div class="grid konferenz-raster border-b-2 border-ui-50 bg-ui-75" :style="rasterStyle(zeile)">
								<div class="grid place-items-center border-r border-ui-50 px-2 py-1 text-center text-xs font-semibold">Note</div>
								<div v-for="fach in zeile.faecher" :key="`note-${fach.fachID}`" class="grid min-h-8 place-items-center border-r border-ui-50 px-1 py-1 text-center last:border-r-0">
									<div class="flex h-full w-full items-center justify-center text-center">
										<svws-ui-badge v-if="(fach.leistung.note !== null) && (getNotenBadge(zeile.klasse, fach.leistung.note) !== null)" type="light" class="!mx-auto !px-1.5 !py-0 !text-sm" :style="badgeStyle(zeile.klasse, fach.leistung.note)">{{ getAnzeigewert(zeile.klasse, fach.leistung.note) }}</svws-ui-badge>
										<span v-else-if="fach.leistung.note !== null" class="text-sm text-black">{{ getAnzeigewert(zeile.klasse, fach.leistung.note) }}</span>
										<span v-else class="text-xs text-black">–</span>
									</div>
								</div>
							</div>
							<template v-if="istMittelstufe(zeile.klasse)">
								<div v-for="art in zeile.teilleistungsarten" :key="art.id" class="grid konferenz-raster border-b border-ui-50 last:border-b-0" :style="rasterStyle(zeile)">
									<div class="grid place-items-center border-r border-ui-50 px-2 py-1 text-center text-sm">{{ art.bezeichnung ?? `Teilleistung ${art.id}` }}</div>
									<div v-for="fach in zeile.faecher" :key="`${art.id}-${fach.fachID}`" class="grid min-h-7 place-items-center gap-0.5 border-r border-ui-50 px-1 py-0.5 text-center last:border-r-0">
										<template v-for="(note, index) in getTeilleistungen(fach.leistung, art.id)" :key="index">
											<div class="flex w-full items-center justify-center text-center">
												<svws-ui-badge v-if="(note !== null) && (getNotenBadge(zeile.klasse, note) !== null)" type="light" class="!mx-auto !px-1.5 !py-0 !text-base" :style="badgeStyle(zeile.klasse, note)">{{ getAnzeigewert(zeile.klasse, note) }}</svws-ui-badge>
												<span v-else class="text-base text-black">{{ getAnzeigewert(zeile.klasse, note) ?? '–' }}</span>
											</div>
										</template>
									</div>
								</div>
							</template>
							<template v-else>
								<div v-for="gruppe in teilleistungsgruppen" :key="gruppe" class="grid konferenz-raster border-b border-ui-50 last:border-b-0" :style="rasterStyle(zeile)">
									<div class="grid place-items-center border-r border-ui-50 px-2 py-1 text-center text-sm font-medium">{{ gruppe === 'So' ? 'SoMi' : 'Klausuren' }}</div>
									<div v-for="fach in zeile.faecher" :key="`${gruppe}-${fach.fachID}`" class="grid min-h-7 grid-cols-2 items-center justify-items-center gap-1 border-r border-ui-50 px-1 py-0.5 text-center last:border-r-0">
										<div v-for="art in getTeilleistungsartenGruppe(zeile, gruppe)" :key="art.id" class="flex flex-col items-center gap-0.5" :title="art.bezeichnung ?? undefined">
											<template v-for="(note, indexNote) in getTeilleistungen(fach.leistung, art.id)" :key="`${art.id}-${indexNote}`">
												<div class="flex w-full items-center justify-center text-center">
													<svws-ui-badge v-if="(note !== null) && (getNotenBadge(zeile.klasse, note) !== null)" type="light" class="!mx-auto !px-1.5 !py-0 !text-base" :style="badgeStyle(zeile.klasse, note)">{{ getAnzeigewert(zeile.klasse, note) }}</svws-ui-badge>
													<span v-else class="text-base text-black">{{ getAnzeigewert(zeile.klasse, note) ?? '–' }}</span>
												</div>
											</template>
										</div>
									</div>
								</div>
							</template>
						</div>
					</div>
				</article>
			</div>
		</div>
	</div>
	<div v-else class="page p-4">
		<p>Für diese Übersicht ist eine Klassen- oder Abteilungsleitungsberechtigung erforderlich.</p>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from 'vue';

	import { ENMLehrerFunktion } from '@core/core/data/enm/ENMLehrerFunktion';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import type { ENMv2Leistung } from '@core/core/data/enm/v2/ENMv2Leistung';
	import type { ENMv2Schueler } from '@core/core/data/enm/v2/ENMv2Schueler';
	import type { ENMv2Teilleistungsart } from '@core/core/data/enm/v2/ENMv2Teilleistungsart';

	import type { EnmManager } from './EnmManager';
	import { ermittleNotenWarnstufe } from './EnmNotenWarnstufe';

	interface SchuelerEintrag {
		schueler: ENMv2Schueler;
		klasse: ENMv2Klasse;
	}

	interface SchuelerZeile extends SchuelerEintrag {
		faecher: FachMitLeistung[];
		teilleistungsarten: ENMv2Teilleistungsart[];
	}

	interface FachEintrag {
		fachID: number;
		fachSortierung: number;
		kuerzel: string;
		lerngruppe: string;
		lehrkraefte: string;
	}

	interface FachMitLeistung extends FachEintrag {
		leistung: ENMv2Leistung;
	}

	const props = defineProps<{
		ladeManagerNotenuebersicht: () => Promise<EnmManager>;
		auswahlAnsicht: () => ENMLehrerFunktion;
		auswahlEinzel: () => ENMv2Klasse | null;
		auswahlMehrfach: () => ENMv2Klasse[];
	}>();
	const manager = shallowRef<EnmManager | null>(null);
	const ladeFehler = ref(false);
	const punkteAnsicht = ref(false);
	const mahnungsAnsicht = ref(false);
	const druckdatum = new Intl.DateTimeFormat('de-DE').format(new Date());
	const klassenLabelDruck = computed(() => {
		const kuerzel = new Set(schuelerListe.value.map(eintrag => eintrag.klasse.kuerzelAnzeige ?? eintrag.klasse.kuerzel ?? ''));
		const klassen = [...kuerzel].filter(value => value.length > 0);
		return klassen.length === 0 ? 'Klasse' : klassen.join(', ');
	});
	const punkteToggleSichtbar = computed(() => schuelerListe.value.some(eintrag => !istMittelstufe(eintrag.klasse)));
	const mahnungsToggleSichtbar = computed(() => {
		const enmManager = manager.value;
		if ((enmManager === null) || (enmManager.lehrerFunktion !== ENMLehrerFunktion.ABTEILUNGSLEITUNG)) {
			return false;
		}
		for (const klasse of enmManager.listKlassenKlassenlehrer) {
			if (istKlasseZugaenglich(klasse, enmManager) && (klassenAuswahl.value === null || klassenAuswahl.value.has(klasse.id)) && istMahnungsJahrgang(klasse)) {
				return true;
			}
		}
		return false;
	});
	let ladeVersion = 0;

	watch(() => props.auswahlAnsicht(), async () => {
		const version = ++ladeVersion;
		manager.value = null;
		ladeFehler.value = false;
		try {
			const result = await props.ladeManagerNotenuebersicht();
			if (version === ladeVersion) {
				manager.value = result;
			}
		} catch {
			if (version === ladeVersion) {
				ladeFehler.value = true;
			}
		}
	}, { immediate: true });

	const berechtigt = computed(() => {
		if (manager.value === null) {
			return false;
		}
		for (const klasse of manager.value.listKlassenKlassenlehrer) {
			if (istKlasseZugaenglich(klasse, manager.value)) {
				return true;
			}
		}
		return false;
	});

	const klassenAuswahl = computed(() => {
		const mehrfach = props.auswahlMehrfach();
		if (mehrfach.length > 0) {
			return new Set(mehrfach.map(klasse => klasse.id));
		}
		const einzel = props.auswahlEinzel();
		return einzel === null ? null : new Set([einzel.id]);
	});

	const schuelerListe = computed((): SchuelerEintrag[] => {
		if ((berechtigt.value === false) || (manager.value === null)) {
			return [];
		}
		const result: SchuelerEintrag[] = [];
		for (const klasse of manager.value.listKlassenKlassenlehrer) {
			if (!istKlasseZugaenglich(klasse, manager.value) || (klassenAuswahl.value !== null && !klassenAuswahl.value.has(klasse.id))) {
				continue;
			}
			for (const schueler of manager.value.mapKlassenSchueler.get(klasse.id) ?? []) {
				result.push({ schueler, klasse });
			}
		}
		return result.sort((a, b) => {
			const klasseSortierung = (a.klasse.kuerzel ?? '').localeCompare(b.klasse.kuerzel ?? '', 'de');
			if (klasseSortierung !== 0) {
				return klasseSortierung;
			}
			const nachnameSortierung = (a.schueler.nachname ?? '').localeCompare(b.schueler.nachname ?? '', 'de');
			return nachnameSortierung !== 0 ? nachnameSortierung : (a.schueler.vorname ?? '').localeCompare(b.schueler.vorname ?? '', 'de');
		});
	});

	const schuelerZeilen = computed((): SchuelerZeile[] => {
		if (manager.value === null) {
			return [];
		}
		return schuelerListe.value.map(eintrag => {
			const fachMap = new Map<number, FachMitLeistung>();
			const artIds = new Set<number>();
			for (const leistung of eintrag.schueler.leistungsdaten) {
				const lerngruppe = manager.value!.mapLerngruppen.get(leistung.lerngruppenID);
				const fach = lerngruppe === null ? null : manager.value!.mapFaecher.get(lerngruppe.fachID);
				if ((lerngruppe === null) || (fach === null)) {
					continue;
				}
				const lehrkraefte: string[] = [];
				for (const id of lerngruppe.idsLehrer) {
					const kuerzel = manager.value!.mapLehrer.get(id)?.kuerzel;
					if ((kuerzel !== null) && (kuerzel !== undefined) && (kuerzel !== '')) {
						lehrkraefte.push(kuerzel);
					}
				}
				fachMap.set(fach.id, {
					fachID: fach.id,
					fachSortierung: fach.sortierung,
					kuerzel: fach.kuerzelAnzeige !== '' ? fach.kuerzelAnzeige : fach.kuerzel,
					lerngruppe: lerngruppe.bezeichnung ?? '',
					lehrkraefte: lehrkraefte.join(', '),
					leistung,
				});
				for (const teilleistung of leistung.teilleistungen) {
					artIds.add(teilleistung.artID);
				}
			}
			const faecher = [...fachMap.values()].sort((a, b) => {
				const sortierung = a.fachSortierung - b.fachSortierung;
				return sortierung !== 0 ? sortierung : a.kuerzel.localeCompare(b.kuerzel, 'de');
			});
			const teilleistungsarten = [...artIds].map(id => manager.value!.mapTeilleistungsarten.get(id))
				.filter((art): art is ENMv2Teilleistungsart => art !== null)
				.sort((a, b) => {
					const sortierung = (a.sortierung ?? 0) - (b.sortierung ?? 0);
					return sortierung !== 0 ? sortierung : (a.bezeichnung ?? '').localeCompare(b.bezeichnung ?? '', 'de');
				});
			return { ...eintrag, faecher, teilleistungsarten };
		});
	});
	const schuelerZeilenAngezeigt = computed(() => {
		if ((mahnungsAnsicht.value === false) || (mahnungsToggleSichtbar.value === false)) {
			return schuelerZeilen.value;
		}
		return schuelerZeilen.value.filter(zeile => istMahnungsJahrgang(zeile.klasse) && hatMahnungsnote(zeile));
	});

	const teilleistungsgruppen = ['So', 'Kl'] as const;
	function rasterStyle(zeile: SchuelerZeile) {
		const fachBreiteMm = Math.min(16, 172 / Math.max(zeile.faecher.length, 1));
		return {
			'--anzahl-faecher': String(zeile.faecher.length),
			'--druck-fach-breite': `${fachBreiteMm}mm`,
		};
	}
	const badgeGelbStyle = { backgroundColor: '#FCD34D', borderColor: '#FBBF24', color: '#000000' };
	const badgeRotStyle = { backgroundColor: '#EF4444', borderColor: '#DC2626', color: '#FFFFFF' };

	function istKlasseZugaenglich(klasse: ENMv2Klasse, enmManager: EnmManager): boolean {
		if (enmManager.lehrerFunktion === ENMLehrerFunktion.KLASSENLEITUNG) {
			return enmManager.klasseIstKlassenleitung(klasse.id);
		}
		if (enmManager.lehrerFunktion === ENMLehrerFunktion.ABTEILUNGSLEITUNG) {
			return enmManager.klasseIstAbteilungsleitung(klasse.id);
		}
		return false;
	}

	function badgeStyle(klasse: ENMv2Klasse, wert: string | null): Record<string, string> | undefined {
		const art = getNotenBadge(klasse, wert);
		return art === 'gelb' ? badgeGelbStyle : art === 'rot' ? badgeRotStyle : undefined;
	}

	function getAnzeigewert(klasse: ENMv2Klasse, wert: string | null): string | null {
		if ((wert === null) || (punkteAnsicht.value === false) || istMittelstufe(klasse) || (manager.value === null)) {
			return wert;
		}
		const kuerzel = wert.trim().replace(/[−–]/g, '-').toLocaleLowerCase();
		for (const note of manager.value.daten.noten) {
			if ((note.kuerzel ?? '').trim().replace(/[−–]/g, '-').toLocaleLowerCase() === kuerzel) {
				return note.notenpunkte === null ? wert : String(note.notenpunkte);
			}
		}
		return wert;
	}

	function getTeilleistungen(leistung: ENMv2Leistung, artID: number): (string | null)[] {
		const result: (string | null)[] = [];
		for (const teilleistung of leistung.teilleistungen) {
			if (teilleistung.artID === artID) {
				result.push(teilleistung.note);
			}
		}
		return result;
	}

	function istMittelstufe(klasse: ENMv2Klasse): boolean {
		return /^(05|06|07|08|09|10)/.test(klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '');
	}

	function getJahrgangKuerzel(klasse: ENMv2Klasse): string {
		const jahrgang = (klasse.idJahrgang === null) ? null : manager.value?.mapJahrgaenge.get(klasse.idJahrgang);
		return (jahrgang?.kuerzel ?? jahrgang?.kuerzelAnzeige ?? klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '').toUpperCase();
	}

	function istMahnungsJahrgang(klasse: ENMv2Klasse): boolean {
		return istMittelstufe(klasse) || getJahrgangKuerzel(klasse) === 'EF';
	}

	function hatMahnungsnote(zeile: SchuelerZeile): boolean {
		const jahrgang = getJahrgangKuerzel(zeile.klasse);
		for (const fach of zeile.faecher) {
			const wert = fach.leistung.note;
			if (wert === null) {
				continue;
			}
			const note = wert.trim().replace(/[−–]/g, '-').toUpperCase();
			if (['4+', '4', '4-', '5+', '5', '5-', '6'].includes(note)) {
				return true;
			}
			if (jahrgang === 'EF' && /^\d+$/.test(note) && Number(note) <= 6) {
				return true;
			}
		}
		return false;
	}

	function druckeKonferenzuebersicht(): void {
		const titel = `Konferenzübersicht ${klassenLabelDruck.value} - ${druckdatum}`;
		const druckStil = document.createElement('style');
		druckStil.id = 'enm-konferenz-druckseiten';
		druckStil.media = 'print';
		druckStil.textContent = `
			@page {
				size: A4 portrait;
				margin: 18mm 8mm 14mm;
				@top-center {
					content: ${JSON.stringify(titel)};
					font-family: Arial, Helvetica, sans-serif;
					font-size: 11pt;
					font-weight: 600;
					text-align: center;
					vertical-align: top;
					padding-top: 5mm;
					padding-bottom: 0;
				}
				@bottom-center {
					content: "Seite " counter(page) " von " counter(pages);
					font-family: Arial, Helvetica, sans-serif;
					font-size: 9pt;
					text-align: center;
				}
			}`;
		document.getElementById(druckStil.id)?.remove();
		document.head.append(druckStil);
		window.addEventListener('afterprint', () => druckStil.remove(), { once: true });
		window.print();
	}

	function getTeilleistungsartenGruppe(zeile: SchuelerZeile, gruppe: 'So' | 'Kl'): ENMv2Teilleistungsart[] {
		const passende = zeile.teilleistungsarten.filter(art => {
			const bezeichnung = (art.bezeichnung ?? '').toLocaleLowerCase();
			return gruppe === 'So' ? /^(so|somi|sonst)/.test(bezeichnung) : /^(kl|klaus)/.test(bezeichnung);
		});
		if (passende.length > 0) {
			return passende.slice(0, 2);
		}
		const grenzwert = Math.min(2, zeile.teilleistungsarten.length);
		return gruppe === 'So' ? zeile.teilleistungsarten.slice(0, grenzwert) : zeile.teilleistungsarten.slice(grenzwert, grenzwert + 2);
	}

	function getNotenBadge(klasse: ENMv2Klasse, wert: string | null): 'gelb' | 'rot' | null {
		const jahrgang = klasse.idJahrgang === null ? '' : manager.value?.mapJahrgaenge.get(klasse.idJahrgang)?.kuerzel
			?? manager.value?.mapJahrgaenge.get(klasse.idJahrgang)?.kuerzelAnzeige ?? klasse.kuerzel ?? klasse.kuerzelAnzeige ?? '';
		return ermittleNotenWarnstufe(jahrgang, klasse.kuerzel ?? '', wert);
	}
</script>

<style scoped>
	.konferenz-titel-druck {
		display: none;
	}

	.konferenz-raster {
		grid-template-columns: 5rem repeat(var(--anzahl-faecher), 6rem);
	}

	@page {
		size: A4 portrait;
		margin: 8mm;
	}

	@media print {
		.konferenz-druck-seite,
		.konferenz-druck-seite * {
			print-color-adjust: exact !important;
			-webkit-print-color-adjust: exact !important;
		}

		:global(body:has(.konferenz-druck-seite)),
		:global(body:has(.konferenz-druck-seite) #app),
		:global(body:has(.konferenz-druck-seite) .svws-ui-tab-content),
		:global(body:has(.konferenz-druck-seite) .svws-ui-page),
		:global(body:has(.konferenz-druck-seite) .app--layout),
		:global(body:has(.konferenz-druck-seite) .app--content),
		:global(body:has(.konferenz-druck-seite) .app--content-container),
		:global(body:has(.konferenz-druck-seite) .app--page),
		:global(body:has(.konferenz-druck-seite) .app--page > div:has(.konferenz-druck-seite)) {
			display: block !important;
			height: auto !important;
			min-height: 0 !important;
			overflow: visible !important;
		}

		.konferenz-titel-bildschirm {
			display: none !important;
		}

		.konferenz-druck-seite > header h1 {
			display: none !important;
		}

		.konferenz-druck-seite > header p {
			display: none !important;
		}

		.konferenz-druck-liste {
			overflow: visible !important;
			height: auto !important;
			min-height: 0 !important;
			flex: none !important;
		}

		.konferenz-druck-seite {
			display: block !important;
			height: auto !important;
			min-height: 0 !important;
			overflow: visible !important;
		}

		.konferenz-druck-liste > div {
			display: block !important;
			width: fit-content !important;
			height: auto !important;
			min-width: 0 !important;
			overflow: visible !important;
		}

		.konferenz-druck-scroll {
			overflow: visible !important;
		}

		.konferenz-schueler-tabelle {
			overflow: hidden !important;
			width: max-content !important;
			max-width: none !important;
			break-inside: avoid-page !important;
			page-break-inside: avoid !important;
			margin-bottom: 2rem;
		}

		.konferenz-schueler-tabelle:last-child {
			margin-bottom: 0;
		}

		.konferenz-raster {
			grid-template-columns: 13mm repeat(var(--anzahl-faecher), var(--druck-fach-breite)) !important;
		}
	}
</style>
