<template>
	<div v-if="hatAktionen" class="mt-2 flex flex-wrap items-center gap-2">
		<svws-ui-button v-if="art === 'klassen'" type="secondary" :disabled="pending" @click="props.erstellen('klassen')">
			Klasse erstellen
		</svws-ui-button>
		<svws-ui-button v-else-if="art === 'schienen'" type="secondary" :disabled="pending" @click="props.erstellen('schienen')">
			Schiene erstellen
		</svws-ui-button>
		<s-uv-schueler-import-modal v-else-if="art === 'schueler'" v-slot="{ openModal }">
			<svws-ui-button type="secondary" :disabled="pending" @click="openModal">Schüler importieren</svws-ui-button>
		</s-uv-schueler-import-modal>

		<svws-ui-button v-if="hatLerngruppenAktionen" type="secondary" :disabled="pending" @click="props.erstellen('lerngruppen')">
			Lerngruppe erstellen
		</svws-ui-button>
		<template v-if="art === 'lerngruppen'">
			<s-uv-lerngruppen-import-modal v-slot="{ openModal }">
				<svws-ui-button type="secondary" :disabled="pending" @click="openModal">Kursunterricht importieren</svws-ui-button>
			</s-uv-lerngruppen-import-modal>
			<svws-ui-button type="secondary" :disabled="pending" @click="klassenLerngruppenErzeugen">
				Klassen-Lerngruppen erzeugen
			</svws-ui-button>
		</template>
		<svws-ui-button v-if="hatLerngruppenAktionen" type="secondary" :disabled="pending" @click="blockungDialogOeffnen">Blocken</svws-ui-button>

		<!-- Der Dialog bleibt nach der Datenaktualisierung bis zur deleted-Meldung eingebunden. -->
		<s-uv-auswahl-loeschen :sichtbar="loeschAuswahl.length > 0" :items="loeschAuswahl" :art="auswahlTitel"
			:bezeichnung="loeschBezeichnung" :loeschen :disabled="pending" @deleted="geloescht" />

		<svws-ui-notification v-if="erzeugenFehler" type="error">{{ erzeugenFehler }}</svws-ui-notification>
		<output v-if="meldung && !blockenDialog" class="text-xs text-ui-success">{{ meldung }}</output>
	</div>

	<svws-ui-modal v-model:show="blockenDialog" size="medium" :auto-close="!pending" :close-in-title="!pending">
		<template #modalTitle>Lehrkraftzuordnungen blocken</template>
		<template #modalContent>
			<div class="grid gap-3">
				<p v-if="blockungStatus === 'bereit'">
					Das Blocken überschreibt alle bestehenden Lehrkraftzuordnungen zu Lerngruppen in diesem Planungsabschnitt.
					Die Berechnung läuft, bis Sie sie stoppen.
				</p>
				<p v-else-if="blockungStatus === 'laeuft'">
					Die Blockung läuft. Sie können jederzeit stoppen und das beste bisherige Ergebnis übernehmen.
				</p>
				<p v-else-if="blockungStatus === 'speichern'">Die Berechnung wird beendet und das beste Ergebnis übernommen.</p>
				<p v-else>Die Lehrkräfte wurden zugeordnet.</p>

				<div v-if="guete !== ''" class="rounded border border-ui-25 bg-ui p-3">
					<p class="mb-1 text-sm font-semibold">Aktuelle Güte nach {{ schritte }} Berechnungsintervallen (je 200 ms)</p>
					<output class="block max-h-40 overflow-auto break-all font-mono text-xs">Malus: {{ guete }}</output>
				</div>
				<svws-ui-notification v-if="blockungFehler" type="error">{{ blockungFehler }}</svws-ui-notification>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button v-if="blockungStatus === 'bereit' || blockungStatus === 'fertig'" type="secondary" @click="blockenDialog = false">
				{{ blockungStatus === 'bereit' ? 'Abbrechen' : 'Schließen' }}
			</svws-ui-button>
			<svws-ui-button v-if="blockungStatus === 'bereit'" :disabled="pending" @click="blocken">Blocken</svws-ui-button>
			<svws-ui-button v-if="blockungStatus === 'laeuft'" type="secondary" @click="blockungStoppen">
				Stoppen und Ergebnis übernehmen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { computed, onUnmounted, ref } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	import SUvSchuelerImportModal from '../schueler/SUvSchuelerImportModal.vue';
	import SUvAuswahlLoeschen from '../SUvAuswahlLoeschen.vue';
	import { uvObjektTitel } from '../UvPresenter';

	import SUvLerngruppenImportModal from './SUvLerngruppenImportModal.vue';
	import { type UvBereichArt, uvBereichArten } from './UvPlanung';

	const props = defineProps<{
		art: UvBereichArt;
		selectedIds: number[];
		auswahlAufheben?: () => void;
		erstellen: (art: 'lerngruppen' | 'klassen' | 'schienen') => void;
	}>();

	type LoeschEintrag = {
		id: number;
		art: UvBereichArt;
	};

	const state = useUvState();
	const plan = computed(() => state.planungsabschnitt);
	const hatAktionen = computed(() => state.hatKompetenzAendern && plan.value !== null && plan.value.id !== -1
		&& ['klassen', 'schienen', 'schueler', 'lerngruppen', 'matrix'].includes(props.art));
	const hatLerngruppenAktionen = computed(() => props.art === 'lerngruppen' || props.art === 'matrix');
	const pending = ref(false);

	const loeschAuswahl = computed<LoeschEintrag[]>(() => [...new Set(props.selectedIds)].map(id => ({
		id, art: props.art,
	})));
	const auswahlTitel = computed(() => uvBereichArten.find(item => item.value === props.art)?.text ?? 'Einträge');
	const erzeugenFehler = ref('');
	const meldung = ref('');

	// Die Dialogdaten sind unabhängig von Rückmeldungen der übrigen Listenaktionen.
	const blockenDialog = ref(false);
	const blockungStatus = ref<'bereit' | 'laeuft' | 'speichern' | 'fertig'>('bereit');
	const blockungFehler = ref('');
	const guete = ref('');
	const schritte = ref(0);
	let eigeneBlockungAktiv = false;

	// Die Aufrufer binden diese Instanz per key an Abschnitt und Ansicht.
	// Ausstehende Rückmeldungen dürfen nach ihrem Abbau keine neue Auswahl verändern.
	let eingebunden = true;

	onUnmounted(() => {
		eingebunden = false;
		eigeneBlockungVerwerfen();
	});

	// Der gemeinsame Löschdialog hält die bestätigte Auswahl bis zum Abschluss fest.
	function loeschBezeichnung(item: LoeschEintrag): string {
		return `${uvObjektTitel(item.art)} (ID: ${item.id})`;
	}

	async function loeschen(items: LoeschEintrag[]): Promise<void> {
		if (pending.value || items.length === 0 || !eingebunden) {
			throw new Error('Die Auswahl kann in der aktuellen Ansicht nicht gelöscht werden.');
		}

		pending.value = true;
		erzeugenFehler.value = '';
		meldung.value = '';

		try {
			await state.planung.deletePlanungsAuswahl(items[0].art, items.map(item => item.id));
		} finally {
			pending.value = false;
		}
	}

	function geloescht(items: LoeschEintrag[]): void {
		if (items.length === 0 || !eingebunden) {
			return;
		}

		meldung.value = `${items.length} Einträge wurden gelöscht.`;
		props.auswahlAufheben?.();
	}

	async function klassenLerngruppenErzeugen(): Promise<void> {
		const aktuellerPlan = plan.value;
		if (pending.value || !state.hatKompetenzAendern || aktuellerPlan === null || aktuellerPlan.id === -1 || props.art !== 'lerngruppen') {
			return;
		}

		pending.value = true;
		erzeugenFehler.value = '';
		meldung.value = '';

		try {
			await state.planung.createLerngruppenFromKlassen();
			if (eingebunden) {
				meldung.value = 'Klassen-Lerngruppen wurden erzeugt.';
			}
		} catch (error) {
			if (eingebunden) {
				erzeugenFehler.value = error instanceof Error ? error.message : 'Die Klassen-Lerngruppen konnten nicht erzeugt werden.';
			}
		} finally {
			pending.value = false;
		}
	}

	// Nur eine von dieser Instanz gestartete Blockung wird beim Verlassen verworfen.
	function eigeneBlockungVerwerfen(): void {
		if (eigeneBlockungAktiv) {
			state.regeln.verwerfeBlockung();
			eigeneBlockungAktiv = false;
		}
	}

	function blockungDialogOeffnen(): void {
		if (pending.value) {
			return;
		}

		blockungStatus.value = 'bereit';
		blockungFehler.value = '';
		guete.value = '';
		schritte.value = 0;
		erzeugenFehler.value = '';
		meldung.value = '';
		blockenDialog.value = true;
	}

	function blockungStoppen(): void {
		if (blockungStatus.value === 'laeuft' && eigeneBlockungAktiv) {
			blockungStatus.value = 'speichern';
			state.regeln.stoppeBlockung();
		}
	}

	/** Startet die Blockung und aktualisiert den angezeigten Gütewert während der Berechnung. */
	async function blocken(): Promise<void> {
		const aktuellerPlan = plan.value;
		if (pending.value || !blockenDialog.value || !hatLerngruppenAktionen.value || !state.hatKompetenzAendern
			|| aktuellerPlan === null || aktuellerPlan.id === -1) {
			return;
		}

		pending.value = true;
		blockungStatus.value = 'laeuft';
		blockungFehler.value = '';
		guete.value = '';
		schritte.value = 0;

		try {
			await state.regeln.blockePlanungsabschnitt(beschreibung => {
				if (eingebunden) {
					eigeneBlockungAktiv = true;
					guete.value = beschreibung;
					schritte.value++;
				}
			});
			if (eingebunden) {
				blockungStatus.value = 'fertig';
				meldung.value = 'Lehrkräfte wurden zugeordnet.';
			}
		} catch (error) {
			if (eingebunden) {
				blockungStatus.value = 'bereit';
				blockungFehler.value = error instanceof Error ? error.message : 'Die Blockung konnte nicht abgeschlossen werden.';
			}
		} finally {
			eigeneBlockungAktiv = false;
			pending.value = false;
		}
	}

</script>
