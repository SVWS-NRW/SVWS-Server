<template>
	<div class="page page-grid-cards" v-if="keineKlassenVorhanden">
		<div class="col-span-1 font-bold">
			Neue Schüler können erst angelegt werden, wenn Klassen vorhanden sind.
			Legen Sie bitte vor der Eingabe von Schülern die benötigten Klassen an.
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
			</div>
		</div>
	</div>
	<div class="page page-grid-cards" v-else>
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<ui-select label="Status" v-model="selectedStatus" :manager="statusManager" :removable="false" :readonly="true" />
				<ui-select label="Schuljahresabschnitt" v-model="schuljahresabschnitt" :manager="schuljahresabschnittsManager" :readonly="routedFromSchnelleingabe" required />
				<ui-select label="Jahrgang" v-model="jahrgang" :manager="jahrgangManager" :readonly="(data.schuljahresabschnitt <= 0) || (routedFromSchnelleingabe)" required />
				<ui-select label="Klasse" v-model="klasse" :manager="klassenManager" :readonly="((data.schuljahresabschnitt <= 0) || ((data.jahrgangID === null) || (data.jahrgangID <= 0)) || (routedFromSchnelleingabe))" required />
				<svws-ui-spacing />
				<ui-select label="Einschulungsart" v-model="einschulungsart" :manager="einschulungsartManager" :removable="true" v-if="schulenMitPrimaerstufe" />
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" v-model="data.anmeldedatum" :valid="istAnmeldedatumGueltig" />
				<div v-if="!istAnmeldedatumGueltig(data.anmeldedatum)" class="flex mt-1">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>Das Anmeldedatum darf nicht in der Zukunft liegen</p>
				</div>
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" v-model="data.aufnahmedatum" :valid="istAufnahmedatumGueltig" />
				<div v-if="!istAufnahmedatumGueltig(data.aufnahmedatum)" class="flex mt-1">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>Das Aufnahmedatum darf nicht vor dem Anmeldedatum liegen</p>
				</div>
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-model="data.beginnBildungsgang" :valid="istBeginnBildungsgangGueltig" v-if="schulenMitBKoderSK" />
				<div v-if="(schulenMitBKoderSK && !istBeginnBildungsgangGueltig(data.beginnBildungsgang))" class="flex mt-1">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>Der Beginn des Bildungsgangs darf nicht vor dem Aufnahmedatum liegen</p>
				</div>
				<svws-ui-input-number placeholder="Dauer Bildungsgang" type="date" v-model="data.dauerBildungsgang" v-if="schulenMitBKoderSK" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Nachname"
					v-model="data.nachname"
					:valid="fieldIsValid('nachname')" :min-len="1" :max-len="120" required />
				<svws-ui-text-input placeholder="Rufname"
					v-model="data.vorname"
					:valid="fieldIsValid('vorname')" :min-len="1" :max-len="80" required />
				<svws-ui-text-input placeholder="Alle Vornamen"
					v-model="data.alleVornamen"
					:valid="fieldIsValid('alleVornamen')" :max-len="255" />
				<ui-select label="Geschlecht" v-model="geschlecht" :manager="geschlechtManager" :removable="false" required />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" v-model="data.geburtsdatum" :valid="istGeburtsdatumGueltig" />
				<div v-if="!istGeburtsdatumGueltig(data.geburtsdatum)" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>Das Alter muss zwischen 4 und 50 Jahren liegen.</p>
				</div>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="(!formIsValid) || (!hatKompetenzUpdate)">Anlegen</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
	import type { KlassenDaten, SchuelerStatusKatalogEintrag } from "@core";
	import { Geschlecht, SchuelerStatus, Schulform, SchuelerSchulbesuchsdaten, BenutzerKompetenz, SchuelerStammdatenNeu, DateUtils } from "@core";
	import { computed, ref, watch } from "vue";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { mandatoryInputIsValid, optionalInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuelerNeuProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const data = ref(new SchuelerStammdatenNeu());
	const dataSchulbesuchsdaten = ref(new SchuelerSchulbesuchsdaten());
	const isLoading = ref<boolean>(false);

	const keineKlassenVorhanden = computed(() => props.schuelerListeManager().klassen.list().isEmpty());

	watch(() => data.value, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(props.schulform);
	});

	const schulenMitBKoderSK = computed(() => props.schulform === Schulform.BK || props.schulform === Schulform.SK);

	// validation logic
	function fieldIsValid(field: keyof SchuelerStammdatenNeu | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'schuljahresabschnitt': {
					const id = data.value.schuljahresabschnitt;
					if (id <= 0) {
						return false;
					}
					return gefilterteSchuljahresabschnitte.value.some(i => i.id === id);
				}
				case 'jahrgangID': {
					const id = data.value.jahrgangID;
					if (id === null || id <= 0) {
						return false;
					}
					return jahrgaenge.value.some(i => i.id === id);
				}
				case 'klassenID': {
					const id = data.value.klassenID;
					if (id === null || id <= 0) {
						return false;
					}
					return gefilterteKlassen.value.some(i => i.id === id);
				}
				case 'nachname':
					return mandatoryInputIsValid(data.value.nachname, 120);
				case 'vorname':
					return mandatoryInputIsValid(data.value.vorname, 80);
				case 'alleVornamen':
					return optionalInputIsValid(data.value.alleVornamen, 255);
				case 'anmeldedatum':
					return istAnmeldedatumGueltig(data.value.anmeldedatum);
				case 'aufnahmedatum':
					return istAufnahmedatumGueltig(data.value.aufnahmedatum);
				case 'beginnBildungsgang':
					return istBeginnBildungsgangGueltig(data.value.beginnBildungsgang);
				case 'geschlecht':
					return Geschlecht.fromValue(data.value.geschlecht) !== null;
				case 'geburtsdatum':
					return ((istGeburtsdatumGueltig(data.value.geburtsdatum) && (data.value.geburtsdatum !== null)));
				default:
					return true;
			}
		};
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof SchuelerStammdatenNeu);
			const fieldValue = data.value[field as keyof SchuelerStammdatenNeu] as string | null;
			return validateField(fieldValue);
		});
	});

	const statusManager = new CoreTypeSelectManager({ clazz: SchuelerStatus.class, schuljahr: schuljahr, schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text" });

	const selectedStatus = computed<SchuelerStatusKatalogEintrag | null>({
		get: (): SchuelerStatusKatalogEintrag | null => SchuelerStatus.data().getWertByKuerzel('' + data.value.status)?.daten(schuljahr.value) ?? null,
		set: (value) => data.value.status = value?.id ?? -1,
	});

	const schuljahresabschnitte = computed(() => Array.from(props.schuelerListeManager().schuljahresabschnitte.list()));

	const gefilterteSchuljahresabschnitte = computed(() => {
		const akt = props.aktAbschnitt;
		const ids = new Set<number>();
		ids.add(akt.id);
		if (akt.idFolgeAbschnitt !== null) {
			ids.add(akt.idFolgeAbschnitt);
		}
		return schuljahresabschnitte.value.filter(s => ids.has(s.id));
	});

	const schuljahresabschnittsManager = new SelectManager({ options: gefilterteSchuljahresabschnitte, optionDisplayText: i => `${i.schuljahr}/${(i.schuljahr + 1) % 100}.${i.abschnitt}`, selectionDisplayText: i => `${i.schuljahr}/${(i.schuljahr + 1) % 100}.${i.abschnitt}` });

	const schuljahresabschnitt = computed({
		get: () => gefilterteSchuljahresabschnitte.value.find(i => i.id === data.value.schuljahresabschnitt) ?? null,
		set: (value) => {
			data.value.schuljahresabschnitt = value?.id ?? -1;
			data.value.jahrgangID = -1;
			data.value.klassenID = -1;

			if (value) {
				const vorschlag = defaultBeginnBildungsgang(value);
				if (vorschlag !== null) {
					data.value.beginnBildungsgang = vorschlag;
				}
			}
			void loadKlassenFuerAbschnitt(value?.id ?? -1);
		},
	});

	const jahrgaenge = computed(() => Array.from(props.schuelerListeManager().jahrgaenge.list()));

	const gefilterteJahrgaenge = computed(() => {
		if (!schuljahresabschnitt.value) {
			return [];
		}
		const saId = schuljahresabschnitt.value.id;
		return jahrgaenge.value.filter(jahrgang => {
			const von = jahrgang.gueltigVon ?? Number.MIN_SAFE_INTEGER;
			const bis = jahrgang.gueltigBis ?? Number.MAX_SAFE_INTEGER;
			return saId >= von && saId <= bis && jahrgang.istSichtbar;
		});
	});

	const jahrgangManager = new SelectManager({ options: gefilterteJahrgaenge, optionDisplayText: i => i.kuerzel ?? '', selectionDisplayText: i => i.kuerzel ?? '' });

	const jahrgang = computed({
		get: () => gefilterteJahrgaenge.value.find(i => i.id === data.value.jahrgangID) ?? null,
		set: (value) => {
			data.value.jahrgangID = value?.id ?? -1;
			data.value.klassenID = -1;
		},
	});

	const klassenFuerAbschnitt = ref<KlassenDaten[]>([]);

	async function loadKlassenFuerAbschnitt(idAbschnitt: number) {
		if (idAbschnitt <= 0) {
			klassenFuerAbschnitt.value = [];
			return;
		}
		klassenFuerAbschnitt.value = Array.from(await props.getSchuelerKlassenFuerAbschnitt(idAbschnitt));
	}

	const gefilterteKlassen = computed<KlassenDaten[]>(() => {
		if (schuljahresabschnitt.value === null) {
			return [];
		}
		const jgId = data.value.jahrgangID;

		return klassenFuerAbschnitt.value.filter(k => {
			return k.idJahrgang === jgId;
		});
	});

	const klassenManager = new SelectManager({ options: gefilterteKlassen, optionDisplayText: i => i.kuerzel ?? "", selectionDisplayText: i => i.kuerzel ?? "" });

	const klasse = computed({
		get: () => gefilterteKlassen.value.find(i => i.id === data.value.klassenID) ?? null,
		set: (value: KlassenDaten | null | undefined) => data.value.klassenID = value?.id ?? -1,
	});

	const einschulungsarten = computed(() => props.mapEinschulungsarten.values());
	const einschulungsartManager = new SelectManager({ options: einschulungsarten, optionDisplayText: i => i.text, selectionDisplayText: i => i.text });

	const einschulungsart = computed({
		get: () => props.mapEinschulungsarten.get(dataSchulbesuchsdaten.value.grundschuleEinschulungsartID ?? -1) ?? null,
		set: (value) => dataSchulbesuchsdaten.value.grundschuleEinschulungsartID = value?.id ?? -1,
	});

	const geschlechtManager = new SelectManager({ options: Geschlecht.values(), optionDisplayText: i => i.text, selectionDisplayText: i => i.text });

	const geschlecht = computed({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? null,
		set: (value) => data.value.geschlecht = value?.id ?? -1 });

	function parseISOToDate(strDate: string | null) {
		if (strDate === null) {
			return null;
		}
		try {
			const d = DateUtils.extractFromDateISO8601(strDate);
			return new Date(d[0], d[1] - 1, d[2]);
		} catch (e) {
			return null;
		}
	}

	function istAnmeldedatumGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		const d = parseISOToDate(strDate);
		if (d === null) {
			return false;
		}
		const today = new Date();
		// Datum darf nicht in der Zukunft liegen (heutige Datum ist erlaubt)
		return d.getTime() <= new Date(today.getFullYear(), today.getMonth(), today.getDate()).getTime();
	}

	function istAufnahmedatumGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		const aufnahme = parseISOToDate(strDate);
		if (aufnahme === null) {
			return false;
		}
		// Aufnahmedatum darf nicht vor Anmeldedatum liegen
		const anmeld = parseISOToDate(data.value.anmeldedatum);
		if (anmeld !== null) {
			return aufnahme.getTime() >= anmeld.getTime();
		}
		return true;
	}

	function istBeginnBildungsgangGueltig(strDate: string | null) {
		if (!schulenMitBKoderSK.value || (strDate === null)) {
			return true;
		}
		const beginn = parseISOToDate(strDate);
		if (beginn === null) {
			return false;
		}
		// Beginn des Bildungangs darf nicht vor Aufnahmedatum liegen
		const aufnahme = parseISOToDate(data.value.aufnahmedatum);
		if (aufnahme !== null) {
			return beginn.getTime() >= aufnahme.getTime();
		}
		return true;
	}

	function istGeburtsdatumGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		try {
			const date = DateUtils.extractFromDateISO8601(strDate);
			const curDate = new Date();
			const diffYear = curDate.getFullYear() - date[0];
			return (diffYear > 3) && (diffYear < 51);
		} catch (e) {
			return false;
		}
	}

	function defaultBeginnBildungsgang(abschnitt: { schuljahr: number, abschnitt: number }): string | null {
		if (!schulenMitBKoderSK.value) {
			return null;
		}
		if (abschnitt.abschnitt === 1) {
			return `${abschnitt.schuljahr}-08-01`;
		} // immer 1. August des Startjahres
		if (abschnitt.abschnitt === 2) {
			return `${abschnitt.schuljahr + 1}-02-01`;
		} // immer 1. Februar des Folgejahres
		return null;
	}

	async function addSchuelerStammdaten() {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;

		const { id, ...partialData } = data.value;
		const result = await props.addSchueler(partialData, data.value.schuljahresabschnitt);

		const { grundschuleEinschulungsartID } = dataSchulbesuchsdaten.value;
		const schulbesuchsdaten: Partial<SchuelerSchulbesuchsdaten> = {
			grundschuleEinschulungsartID,
		};
		await props.patchSchuelerSchulbesuchdaten(schulbesuchsdaten, result.id);
		await props.gotoSchnelleingabeView(true, result.id);
		isLoading.value = false;
	}

	function cancel() {
		props.checkpoint.active = false;
		props.schuelerListeManager().schuelerstatus.auswahlClear();
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		void props.gotoDefaultView(null);
	}

	const initialeSchuelerDaten = computed(() => props.initialeSchuelerDaten() ?? null);
	const routedFromSchnelleingabe = computed(() => initialeSchuelerDaten.value !== null);

	watch(initialeSchuelerDaten, async (val) => {
		if (val === null) {
			return;
		}

		if (val.anmeldedatum !== null) {
			data.value.anmeldedatum = val.anmeldedatum;
		}
		if (val.aufnahmedatum !== null) {
			data.value.aufnahmedatum = val.aufnahmedatum;
		}
		if (val.beginnBildungsgang !== null) {
			data.value.beginnBildungsgang = val.beginnBildungsgang;
		}
		if (val.dauerBildungsgang !== null) {
			data.value.dauerBildungsgang = val.dauerBildungsgang;
		}

		schuljahresabschnitt.value = gefilterteSchuljahresabschnitte.value.find(s => s.id === val.schuljahresabschnitt) ?? null;
		await loadKlassenFuerAbschnitt(val.schuljahresabschnitt);
		if (val.jahrgangID !== null) {
			data.value.jahrgangID = val.jahrgangID;
		}
		if (val.klassenID !== null) {
			data.value.klassenID = val.klassenID;
		}

	}, { immediate: true });

</script>
