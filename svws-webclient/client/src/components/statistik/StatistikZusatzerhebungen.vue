<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-zusatzerhebungen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-content-card v-if="showInternationaleKontakte" title="Internationale Kontakte">
				<svws-ui-input-wrapper :grid="1">
					<svws-ui-checkbox v-model="model.proxy.hatInternationaleKontakte" statistics>
						Hat Internationale Kontakte
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card v-if="showSchulanmeldungen" title="Schulanmeldungen und Zurückstellungen">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number placeholder="Schulanmeldungen nach § 35 SchulG" v-model="model.proxy.anzahlSchulanmeldungen"
						:validation="() => model.getFehler('anzahlSchulanmeldungen')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="Zurückstellungsverfahren" v-model="model.proxy.anzahlZurueckstellungsverfahren"
						:validation="() => model.getFehler('anzahlZurueckstellungsverfahren')" @change="model.patch" :min="0" statistics />
					<div class="col-span-full font-bold mt-2">Tatsächliche Zurückstellungen</div>
					<svws-ui-input-number placeholder="Insgesamt" v-model="model.proxy.anzahlZurueckstellungenInsgesamt"
						:validation="() => model.getFehler('anzahlZurueckstellungenInsgesamt')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="Davon weiblich" v-model="model.proxy.anzahlZurueckstellungenWeiblich"
						:validation="() => model.getFehler('anzahlZurueckstellungenWeiblich')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="Ausländer zusammen" v-model="model.proxy.anzahlZurueckstellungenAuslaenderZusammen"
						:validation="() => model.getFehler('anzahlZurueckstellungenAuslaenderZusammen')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="Ausländer weiblich" v-model="model.proxy.anzahlZurueckstellungenAuslaenderWeiblich"
						:validation="() => model.getFehler('anzahlZurueckstellungenAuslaenderWeiblich')" @change="model.patch" :min="0" statistics />
					<div class="col-span-full font-bold mt-2">Zurückgestellte Kinder nach dem Quartal, in dem sie 6 Jahre alt geworden sind</div>
					<svws-ui-input-number placeholder="4. Quartal des Vorjahres" v-model="model.proxy.anzahlZurueckstellungenInsgesamtQuartal4"
						:validation="() => model.getFehler('anzahlZurueckstellungenInsgesamtQuartal4')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="1. Quartal des Erhebungsjahres" v-model="model.proxy.anzahlZurueckstellungenInsgesamtQuartal1"
						:validation="() => model.getFehler('anzahlZurueckstellungenInsgesamtQuartal1')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="2. Quartal des Erhebungsjahres" v-model="model.proxy.anzahlZurueckstellungenInsgesamtQuartal2"
						:validation="() => model.getFehler('anzahlZurueckstellungenInsgesamtQuartal2')" @change="model.patch" :min="0" statistics />
					<svws-ui-input-number placeholder="3. Quartal des Erhebungsjahres" v-model="model.proxy.anzahlZurueckstellungenInsgesamtQuartal3"
						:validation="() => model.getFehler('anzahlZurueckstellungenInsgesamtQuartal3')" @change="model.patch" :min="0" statistics />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Internatsplätze">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Art des Internats" class="col-span-full"
						v-model="model.internatArt.value"
						:manager="internatArtManager"
						statistics />
					<template v-if="model.hatInternatsplaetze.value">
						<div class="col-span-full font-bold mt-2">Jungen</div>
						<svws-ui-input-number placeholder="Mögliche Plätze" v-model="model.proxy.anzahlInternatplaetzeJungenMoeglich"
							:validation="() => model.getFehler('anzahlInternatplaetzeJungenMoeglich')" @change="model.patch" :min="0"
							:disabled="!model.erlaubtJungenplaetze.value" statistics />
						<svws-ui-input-number placeholder="Belegte Plätze" v-model="model.proxy.anzahlInternatplaetzeJungenBelegt"
							:validation="() => model.getFehler('anzahlInternatplaetzeJungenBelegt')" @change="model.patch" :min="0"
							:disabled="!model.erlaubtJungenplaetze.value" statistics />
						<div class="col-span-full font-bold mt-2">Mädchen</div>
						<svws-ui-input-number placeholder="Mögliche Plätze" v-model="model.proxy.anzahlInternatplaetzeMaedchenMoeglich"
							:validation="() => model.getFehler('anzahlInternatplaetzeMaedchenMoeglich')" @change="model.patch" :min="0"
							:disabled="!model.erlaubtMaedchenplaetze.value" statistics />
						<svws-ui-input-number placeholder="Belegte Plätze" v-model="model.proxy.anzahlInternatplaetzeMaedchenBelegt"
							:validation="() => model.getFehler('anzahlInternatplaetzeMaedchenBelegt')" @change="model.patch" :min="0"
							:disabled="!model.erlaubtMaedchenplaetze.value" statistics />
						<div class="col-span-full font-bold mt-2">Geschlechtsneutral</div>
						<svws-ui-input-number placeholder="Mögliche Plätze" v-model="model.proxy.anzahlInternatplaetzeNeutralMoeglich"
							:validation="() => model.getFehler('anzahlInternatplaetzeNeutralMoeglich')" @change="model.patch" :min="0"
							:disabled="!model.erlaubtNeutralplaetze.value" statistics />
						<svws-ui-input-number placeholder="Belegte Plätze" v-model="model.proxy.anzahlInternatplaetzeNeutralBelegt"
							:validation="() => model.getFehler('anzahlInternatplaetzeNeutralBelegt')" @change="model.patch" :min="0"
							:disabled="!model.erlaubtNeutralplaetze.value" statistics />
					</template>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card v-if="showKlinikschule" title="Klinikschule">
				<svws-ui-input-wrapper :grid="2">
					<div class="col-span-full">
						Durchschnittliche Schülerzahl zur Ermittlung des Lehrerstellenbedarfs gem. BASS 11-11 Nr. 4
						(Schülerzahl des vorangegangenen Schuljahres auf eine Nachkommastelle gerundet)
					</div>
					<div class="font-bold mt-2">
						<svws-ui-tooltip>
							Insgesamt
							<template #content>
								{{ tooltipInsgesamt }}
							</template>
						</svws-ui-tooltip>
					</div>
					<div class="font-bold mt-2">
						<svws-ui-tooltip>
							Davon bei Schwerstbehinderung
							<template #content>
								{{ tooltipSchwerstbehinderte }}
							</template>
						</svws-ui-tooltip>
					</div>
					<svws-ui-input-number placeholder="Allgemeinbildend" v-model="model.proxy.durchschnittKlinikschuleAllgemeinbildendInsgesamt"
						:validation="() => model.getFehler('durchschnittKlinikschuleAllgemeinbildendInsgesamt')" @change="model.patch"
						:min="0" :decimal-places="1" :steps="0.1" statistics />
					<svws-ui-input-number placeholder="Allgemeinbildend" v-model="model.proxy.durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte"
						:validation="() => model.getFehler('durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte')" @change="model.patch"
						:min="0" :decimal-places="1" :steps="0.1" statistics />
					<svws-ui-input-number placeholder="Berufsbildend in Teilzeit" v-model="model.proxy.durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt"
						:validation="() => model.getFehler('durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt')" @change="model.patch"
						:min="0" :decimal-places="1" :steps="0.1" statistics />
					<svws-ui-input-number placeholder="Berufsbildend in Teilzeit" v-model="model.proxy.durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte"
						:validation="() => model.getFehler('durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte')" @change="model.patch"
						:min="0" :decimal-places="1" :steps="0.1" statistics />
					<svws-ui-input-number placeholder="Berufsbildend in Vollzeit" v-model="model.proxy.durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt"
						:validation="() => model.getFehler('durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt')" @change="model.patch"
						:min="0" :decimal-places="1" :steps="0.1" statistics />
					<svws-ui-input-number placeholder="Berufsbildend in Vollzeit" v-model="model.proxy.durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte"
						:validation="() => model.getFehler('durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte')" @change="model.patch"
						:min="0" :decimal-places="1" :steps="0.1" statistics />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</svws-ui-input-wrapper>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import { InternatArt } from "@core/asd/types/schule/InternatArt";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useStatistikState } from "@ui/states/statistik/StatistikState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	import { ZusatzerhebungenModelProxy } from "./modelproxy/ZusatzerhebungenModelProxy";
	import type { StatistikZusatzerhebungenProps } from "./StatistikZusatzerhebungenProps";

	const props = defineProps<StatistikZusatzerhebungenProps>();
	const schuleState = useSchuleState();
	const statistikState = useStatistikState();

	const model = new ZusatzerhebungenModelProxy(() => statistikState.statistikGesamt.zusatzerhebungen, statistikState.patchZusatzerhebungen);

	const showInternationaleKontakte = [Schulform.G, Schulform.H, Schulform.V, Schulform.S, Schulform.R, Schulform.PS, Schulform.SK, Schulform.GE, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GY, Schulform.WB, Schulform.BK, Schulform.KS, Schulform.SR, Schulform.SG, Schulform.SB]
		.includes(schuleState.schulform);
	const showSchulanmeldungen = [Schulform.G, Schulform.V, Schulform.PS, Schulform.FW, Schulform.HI, Schulform.WF]
		.includes(schuleState.schulform);

	/** Der Schuljahresabschnitt, für den die Zusatzerhebungen erfasst werden */
	const abschnitt = computed<Schuljahresabschnitt>(() => {
		const id = statistikState.statistikGesamt.zusatzerhebungen.idSchuljahresabschnitt;
		for (const a of statistikState.statistikGesamt.schule.abschnitte) {
			if (a.id === id) {
				return a;
			}
		}
		return schuleState.abschnitt;
	});

	const showKlinikschule = computed<boolean>(() => Schulform.istKlinikschule(schuleState.stammdaten.schulNr, schuleState.schulform, abschnitt.value));

	const internatArtManager = new CoreTypeSelectManager({
		clazz: InternatArt.class,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const tooltipInsgesamt = "Durchschnittliche Schülerzahl = Summe der Unterrichtstage aller Schülerinnen und Schüler (einschließlich intensivpädagogisch zu "
		+ "fördernde Schülerinnen und Schüler bei Schwerstbehinderung) / maximal erteilbare Unterrichtstage des vorangegangenen Schuljahres. Die Summe der "
		+ "Unterrichtstage aller Schülerinnen und Schüler ist die Summe der Tage mit Unterricht, die je Schülerin oder Schüler im vorangegangenen Schuljahr an "
		+ "der Klinikschule tatsächlich erteilt wurden. Ferientage, gesetzliche Feiertage sowie Samstage und Sonntage gelten nicht als Unterrichtstage.";

	const tooltipSchwerstbehinderte = "Durchschnittliche Schülerzahl der gemäß intensivpädagogischer Förderung bei Schwerstbehinderung zu fördernden "
		+ "Schülerinnen und Schüler gemäß § 15 AO-SF (BASS 13-41). Die Berechnung erfolgt analog zur Spalte „Insgesamt“, hier jedoch nur für die "
		+ "intensivpädagogisch zu fördernden Schülerinnen und Schüler bei Schwerstbehinderung.";

</script>
