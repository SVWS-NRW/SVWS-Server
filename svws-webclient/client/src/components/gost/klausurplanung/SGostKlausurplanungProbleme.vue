<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page page-flex-col min-w-128 max-w-256">
		<ui-card v-if="abschnitt !== undefined && !kMan().stundenplanManagerGeladenAndExistsByAbschnitt(abschnitt.id)" icon="i-ri-calendar-event-line" :fehler="ValidatorFehlerart.MUSS"
			title="Kein Stundenplan" subtitle="Es existiert kein Stundenplan für diesen Schuljahresabschnitt." :is-open="currentAction === 'stundenplan_fehlend'"
			@update:is-open="(isOpen) => setCurrentAction('stundenplan_fehlend', isOpen)">
			<p>Zur Terminierung von Klausurschienen und Raumplanung muss zwingend ein Stundenplan definiert sein.</p>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Stundenplandefinition" @click="gotoStundenplan" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Stundenplandefinition
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!vorgabenAlle().isEmpty()" icon="i-ri-draft-line" title="Fehlende Klausurvorgaben" :fehler="vorgaben().size() > 0 ? ValidatorFehlerart.MUSS : ValidatorFehlerart.UNGENUTZT"
			:is-open="!vorgaben().isEmpty() && currentAction === 'vorgaben_fehlend'"
			@update:is-open="(isOpen) => setCurrentAction('vorgaben_fehlend', isOpen)" :collapsible="!vorgaben().isEmpty()">
			<template #subtitle>
				<span>{{ vorgaben().size() }} fehlende Klausurvorgabe{{ vorgaben().size() === 1 ? "" : "n" }} gefunden.
					<template v-if="halbjahr.istEinfuehrungsphase()">
						<span v-if="vorgabenAnzahlAusgeblendet() > 0" class="inline-flex items-center italic"> ( {{ vorgabenAnzahlAusgeblendet() }} ausgeblendet
							<span @click.stop>
								<svws-ui-button @click="ignoreVorgabenToggle = !ignoreVorgabenToggle; setCurrentAction('vorgaben_fehlend', true)" type="icon" :title="'Ignorierte Vorgaben ' + (ignoreVorgabenToggle ? 'anzeigen' : 'ausblenden')">
									<span v-if="ignoreVorgabenToggle" class="icon i-ri-eye-off-line" />
									<span v-else class="icon i-ri-eye-line" :class="vorgaben().size() > 0 ? 'icon-ui-ondanger' : ''" />
								</svws-ui-button>
							</span>
							)
						</span>
					</template>
				</span>
			</template>
			<svws-ui-table :items="vorgaben()" :columns="addStatusColumn(colsVorgaben, '')">
				<template #cell(idFach)="{ value }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(value)}`">{{ kMan().getFaecherManager(jahrgangsdaten!.abiturjahr).get(value)?.bezeichnung }}</span>
				</template>
				<template #cell(quartal)="{ value }">
					{{ value }}
				</template>
				<template #cell(status)="{ rowData }">
					<svws-ui-button v-if="vorgabenIgnoreManager.contains(rowData)" @click="vorgabenIgnoreManager.remove(rowData)" type="icon" title="Vorgabe nicht mehr ignorieren">
						<span class="icon i-ri-eye-off-line" />
					</svws-ui-button>
					<svws-ui-button v-else-if="rowData.halbjahr <= 1" @click="vorgabenIgnoreManager.add(rowData)" type="icon" title="Vorgabe ignorieren">
						<span class="icon i-ri-eye-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Vorgabenansicht" @click="gotoVorgaben" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Vorgabenansicht
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!schuelerklausuren().isEmpty()" icon="i-ri-group-line" title="Abweichende Schülerklausurmenge" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="schuelerklausuren().size() + ' Abweichung' + (schuelerklausuren().size() === 1 ? '' : 'en') + ' gefunden.'" :is-open="currentAction === 'schuelerklausurmenge_abweichend'"
			@update:is-open="(isOpen) => setCurrentAction('schuelerklausurmenge_abweichend', isOpen)">
			<svws-ui-table :items="schuelerklausuren()" :columns="addStatusColumn(colsSchuelerklausuren)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button v-if="rowData.id === -1" type="icon" @click="async () => {
						if (isAwaiting) return;
						isAwaiting = true;
						try {
							await erzeugeSchuelerklausuren(ListUtils.create1(rowData));
						} finally {
							isAwaiting = false;
						}
					}" :disabled="isAwaiting">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
					<svws-ui-button v-else type="icon" @click="async () => {
						if (isAwaiting) return;
						isAwaiting = true;
						try {
							await loescheSchuelerklausuren(ListUtils.create1(rowData));
						} finally {
							isAwaiting = false;
						}
					}" :disabled="isAwaiting">
						<span class="icon i-ri-delete-bin-line" />
					</svws-ui-button>
				</template>
				<template #cell(name)="{ rowData }">
					{{ kMan().schuelerGetBySchuelerklausur(rowData).nachname }}, {{ kMan().schuelerGetBySchuelerklausur(rowData).vorname }}
				</template>
				<template #cell(kurs)="{ rowData }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(rowData)}`">{{ kMan().kursdatenBySchuelerklausur(rowData).kuerzel }}</span>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeBySchuelerklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!kursklausuren().isEmpty()" icon="i-ri-book-2-line" title="Abweichende Kursklausurmenge" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="kursklausuren().size() + ' Abweichung' + (kursklausuren().size() === 1 ? '' : 'en') + ' gefunden.'" :is-open="currentAction === 'kursklausuren_fehlend'"
			@update:is-open="(isOpen) => setCurrentAction('kursklausuren_fehlend', isOpen)">
			<svws-ui-table :items="kursklausuren()" :columns="(kursklausuren().toArray() as GostKursklausur[]).some(kk => kk.id !== -1) ? addStatusColumn(colsKursklausuren) : colsKursklausuren">
				<template #cell(status)="{ rowData }">
					<svws-ui-button v-if="rowData.id !== -1" type="transparent" @click="async () => {
						if (isAwaiting) return;
						isAwaiting = true;
						try {
							await loescheKursklausuren(ListUtils.create1(rowData));
						} finally {
							isAwaiting = false;
						}
					}" title="löschen" :disabled="isAwaiting">
						<span class="icon i-ri-delete-bin-line" /> löschen
					</svws-ui-button>
				</template>
				<template #cell(kurs)="{ rowData }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(rowData)}`">{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}</span>
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button v-if="!(kursklausuren().toArray() as GostKursklausur[]).every(kk => kk.id !== -1)" title="Fehlende Kursklausuren erstellen" @click="erzeugeKursklausurenAusVorgabenOrModal" class="mt-2">
					<span class="icon i-ri-play-line" />
					Fehlende Kursklausuren erstellen
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!kursklausurenNichtVerteilt().isEmpty()" icon="i-ri-book-2-line" title="Nicht verteilte Kursklausuren" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="kursklausurenNichtVerteilt().size() + ' nicht verteilte Kursklausur' + (kursklausurenNichtVerteilt().size() === 1 ? '' : 'en') + ' gefunden.'"
			:is-open="currentAction === 'kursklausuren_nicht_verteilt'" @update:is-open="(isOpen) => setCurrentAction('kursklausuren_nicht_verteilt', isOpen)">
			<svws-ui-table :items="kursklausurenNichtVerteilt()" :columns="colsKursklausuren">
				<template #cell(kurs)="{ rowData }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(rowData)}`">{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}</span>
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Schienenansicht" @click="() => gotoSchienen(undefined)" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Schienenansicht
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!termineOhneStundenplan().isEmpty()" icon="i-ri-calendar-event-line" title="Klausurtermine ohne gültigen Stundenplan" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="termineOhneStundenplan().size() + ' Klausurtermin' + (termineOhneStundenplan().size() === 1 ? '' : 'e') + ' ohne gültigen Stundenplan gefunden.'" :is-open="currentAction === 'termine_ohne_stundenplan'"
			@update:is-open="(isOpen) => setCurrentAction('termine_ohne_stundenplan', isOpen)">
			<svws-ui-table :items="termineOhneStundenplan()" :columns="colsTermine">
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<span v-for="kurs in terminBezeichnung(rowData)" :key="kurs.text ?? kurs" class="svws-ui-badge mr-1 whitespace-nowrap" :style="kurs.text ? `color: var(--color-text-uistatic); background-color: ${kurs.farbe}` : ''">
							{{ kurs.text ?? kurs }}
						</span>
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Stundenplandefinition" @click="gotoStundenplan" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Stundenplandefinition
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!termineMitKonflikten().isEmpty()" icon="i-ri-alert-line" title="Klausurtermine mit Schülerkonflikten" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="termineMitKonflikten().size() + ' Klausurtermin' + (termineMitKonflikten().size() === 1 ? '' : 'e') + ' mit Schülerkonflikten gefunden.'"
			:is-open="currentAction === 'klausurtermine_mit_schuelerkonflikten'"
			@update:is-open="(isOpen) => setCurrentAction('klausurtermine_mit_schuelerkonflikten', isOpen)">
			<svws-ui-table :items="termineMitKonflikten()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent" @click="gotoSchienen(rowData)"
						title="Schiene anzeigen" size="small">
						<span class="icon i-ri-link" /> anzeigen
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<span v-for="kurs in terminBezeichnung(rowData)" :key="kurs.text ?? kurs" class="svws-ui-badge mr-1 whitespace-nowrap" :style="kurs.text ? `color: var(--color-text-uistatic); background-color: ${kurs.farbe}` : ''">
							{{ kurs.text ?? kurs }}
						</span>
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!termineOhneDatum().isEmpty()" icon="i-ri-calendar-event-line" title="Klausurtermine ohne Datum" :fehler="ValidatorFehlerart.KANN"
			:subtitle="termineOhneDatum().size() + ' Klausurtermin' + (termineOhneDatum().size() === 1 ? '' : 'e') + ' ohne Datum gefunden.'" :is-open="currentAction === 'termine_ohne_datum'"
			@update:is-open="(isOpen) => setCurrentAction('termine_ohne_datum', isOpen)">
			<svws-ui-table :items="termineOhneDatum()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent" @click="gotoKalenderdatum(undefined, rowData)"
						title="Datum setzen" size="small"
						:disabled="abschnitt === undefined || !kMan().stundenplanManagerExistsByAbschnitt(abschnitt.id)">
						<span class="icon i-ri-link" /> datieren
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<span v-for="kurs in terminBezeichnung(rowData)" :key="kurs.text ?? kurs" class="svws-ui-badge mr-1 whitespace-nowrap" :style="kurs.text ? `color: var(--color-text-uistatic); background-color: ${kurs.farbe}` : ''">
							{{ kurs.text ?? kurs }}
						</span>
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!termineUnvollstaendigeRaumzuweisung().isEmpty()" icon="i-ri-team-line" title="Klausurtermine mit unvollständiger Raumplanung" :fehler="ValidatorFehlerart.KANN"
			:subtitle="termineUnvollstaendigeRaumzuweisung().size() + ' Klausurtermin' + (termineUnvollstaendigeRaumzuweisung().size() === 1 ? '' : 'e') + ' mit unvollständiger Raumplanung gefunden.'"
			:is-open="currentAction === 'termine_ohne_raumplanung'" @update:is-open="(isOpen) => setCurrentAction('termine_ohne_raumplanung', isOpen)">
			<svws-ui-table :items="termineUnvollstaendigeRaumzuweisung()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoRaumzeitTermin(rowData.abijahr, GostHalbjahr.fromIDorException(rowData.halbjahr), rowData.id)"
						title="Räume planen" size="small">
						<span class="icon i-ri-link" /> Raumplan
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<span v-for="kurs in terminBezeichnung(rowData)" :key="kurs.text ?? kurs" class="svws-ui-badge mr-1 whitespace-nowrap" :style="kurs.text ? `color: var(--color-text-uistatic); background-color: ${kurs.farbe}` : ''">
							{{ kurs.text ?? kurs }}
						</span>
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!raumkapazitaetUeberschritten().isEmpty()" icon="i-ri-team-line" title="Raumkapazität überschritten" :fehler="ValidatorFehlerart.KANN"
			:subtitle="raumkapazitaetUeberschritten().size() + ' Klausurtermin' + (raumkapazitaetUeberschritten().size() === 1 ? '' : 'e') + ' mit überschrittener Raumkapazität gefunden.'"
			:is-open="currentAction === 'termine_raumkapazität'" @update:is-open="(isOpen) => setCurrentAction('termine_raumkapazität', isOpen)">
			<svws-ui-table :items="raumkapazitaetUeberschritten()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoRaumzeitTermin(rowData.abijahr, GostHalbjahr.fromIDorException(rowData.halbjahr), rowData.id)"
						title="Räume planen" size="small">
						<span class="icon i-ri-link" /> Raumplan
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<span v-for="kurs in terminBezeichnung(rowData)" :key="kurs.text ?? kurs" class="svws-ui-badge mr-1 whitespace-nowrap" :style="kurs.text ? `color: var(--color-text-uistatic); background-color: ${kurs.farbe}` : ''">
							{{ kurs.text ?? kurs }}
						</span>
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!nachschreibklausurenNichtZugewiesen().isEmpty()" icon="i-ri-spam-3-line" title="Nicht zugewiesene Nachschreibklausuren" :fehler="ValidatorFehlerart.KANN"
			:subtitle="nachschreibklausurenNichtZugewiesen().size() + ' nicht zugewiesene Nachschreibklausur' + (nachschreibklausurenNichtZugewiesen().size() === 1 ? '' : 'en') + ' gefunden.'"
			:is-open="currentAction === 'nachschreibklausuren_nicht_zugewiesen'"
			@update:is-open="(isOpen) => setCurrentAction('nachschreibklausuren_nicht_zugewiesen', isOpen)">
			<svws-ui-table :items="nachschreibklausurenNichtZugewiesen()" :columns="colsSchuelerklausuren">
				<template #cell(name)="{ rowData }">
					<span>{{ kMan().schuelerGetBySchuelerklausurtermin(rowData).nachname }},
						{{ kMan().schuelerGetBySchuelerklausurtermin(rowData).vorname }}</span>
				</template>
				<template #cell(kurs)="{ rowData }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(rowData)}`">{{ kMan().kursdatenBySchuelerklausurTermin(rowData).kuerzel }}</span>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeBySchuelerklausurTermin(rowData).quartal }}
				</template>
			</svws-ui-table>

			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Nachschreiberansicht"
					@click="gotoNachschreiber(jahrgangsdaten!.abiturjahr, halbjahr)"
					class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Nachschreiberansicht
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card icon="i-ri-alert-line" :collapsible="!klausurenProKwWarning().isEmpty()" :fehler="ValidatorFehlerart.KANN"
			:is-open="!klausurenProKwWarning().isEmpty() && currentAction === 'konflikt_drei_wochenklausuren'" @update:is-open="(isOpen) => setCurrentAction('konflikt_drei_wochenklausuren', isOpen)">
			<template #title>
				<div class="ui-card--header--title flex items-center gap-3">
					Warnung für Schüler mit
					<div @click.stop>
						<svws-ui-input-number headless v-model="kwWarnLimit" :min="2" :max="5" />
					</div>
					oder mehr Klausuren in einer Woche
				</div>
			</template>
			<template #subtitle>
				<div class="flex">
					<span>{{ klausurenProKwWarning().size() === 0 ? 'Keine' : klausurenProKwWarning().size() }} Schüler mit {{ kwWarnLimit }}&nbsp;</span>
					<span v-if="kwErrorLimit - 1 > kwWarnLimit">bis {{ kwErrorLimit - 1 }}&nbsp;</span>
					<span>Klausuren in einer Woche gefunden.</span>
				</div>
			</template>
			<svws-ui-table :items="klausurenProKwWarning()" :columns="colsKwKonflikte">
				<template #cell(kw)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoKalenderdatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!, undefined)"
						title="Springe zu Kalenderwoche" size="small">
						<span class="icon i-ri-link" /> {{ rowData.a.a }}
					</svws-ui-button>
				</template>
				<template #cell(schueler)="{ rowData }">
					{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.nachname }},
					{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.vorname }}
				</template>
				<template #cell(klausuren)="{rowData}">
					<span v-for="klausur in rowData.b" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`color: var(--color-text-uistatic); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
						<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
						<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum!) }}</span>
					</span>
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card icon="i-ri-alert-fill" :collapsible="!klausurenProKwError().isEmpty()" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="(klausurenProKwError().size() === 0 ? 'Keine' : klausurenProKwError().size()) + ' Schüler mit ' + kwErrorLimit + ' oder mehr Klausuren in einer Woche gefunden.'"
			:is-open="!klausurenProKwError().isEmpty() && currentAction === 'konflikt_vier_wochenklausuren'" @update:is-open="(isOpen) => setCurrentAction('konflikt_vier_wochenklausuren', isOpen)">
			<template #title>
				<div class="ui-card--header--title flex items-center gap-3">
					Fehler für Schüler mit
					<div @click.stop>
						<svws-ui-input-number headless v-model="kwErrorLimit" :min="3" :max="5" />
					</div>
					oder mehr Klausuren in einer Woche
				</div>
			</template>
			<svws-ui-table :items="klausurenProKwError()" :columns="colsKwKonflikte">
				<template #cell(kw)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoKalenderdatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!, undefined)"
						title="Springe zu Kalenderwoche" size="small">
						<span class="icon i-ri-link" /> {{ rowData.a.a }}
					</svws-ui-button>
				</template>
				<template #cell(schueler)="{ rowData }">
					<span>
						{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.nachname }},
						{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.vorname }}
					</span>
				</template>
				<template #cell(klausuren)="{rowData}">
					<span v-for="klausur in rowData.b" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`color: var(--color-text-uistatic); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
						<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
						<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum!) }}</span>
					</span>
				</template>
			</svws-ui-table>
		</ui-card>
	</div>

	<s-gost-klausurplanung-modal v-model:show="modalVorgaben" :text="modalError" :jump-to="gotoVorgaben" jump-to-text="Zu den Klausurvorgaben" abbrechen-text="OK" />
</template>

<script setup lang="ts">
	import { ref, onMounted, computed } from 'vue';
	import type { DataTableColumn } from "@ui";
	import type { GostKlausurtermin, GostKursklausur } from "@core";
	import { DateUtils, Fach, GostHalbjahr, ListUtils, OpenApiError, ValidatorFehlerart, GostSchuelerklausur, GostSchuelerklausurTermin	} from "@core";
	import type { GostKlausurplanungProblemeProps } from "./SGostKlausurplanungProblemeProps";
	import { SGostKlausurplanungVorgabenIgnoreManager } from "~/components/gost/klausurplanung/SGostKlausurplanungVorgabenIgnoreManager";

	const props = defineProps<GostKlausurplanungProblemeProps>();

	const vorgabenIgnoreManager = new SGostKlausurplanungVorgabenIgnoreManager(props.getObjectValue, props.setObjectValue);

	const ignoreVorgabenToggle = ref<boolean>(true);

	const vorgaben = () => props.kMan().vorgabefehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, (ignoreVorgabenToggle.value ? vorgabenIgnoreManager.getAll() : null));
	const vorgabenAlle = () => props.kMan().vorgabefehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, null);
	const vorgabenAnzahlAusgeblendet = () => vorgabenIgnoreManager.countContained(vorgabenAlle());
	const kursklausuren = () => props.kMan().kursklausurfehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const kursklausurenNichtVerteilt = () => props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const schuelerklausuren = () => props.kMan().schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineOhneDatum = () => props.kMan().terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineOhneStundenplan = () => props.kMan().terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineMitKonflikten = () => props.kMan().terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineUnvollstaendigeRaumzuweisung = () => props.kMan().terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const raumkapazitaetUeberschritten = () => props.kMan().terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const nachschreibklausurenNichtZugewiesen = () => props.kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const klausurenProKwWarning = () => props.kMan().klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, kwWarnLimit.value, kwErrorLimit.value);
	const klausurenProKwError = () => props.kMan().klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, kwErrorLimit.value, -1);

	const oldAction = ref({
		name: "",
		open: false,
	});
	const currentAction = ref<string>('');
	const isAwaiting = ref(false);

	const kwWarnLimit = computed<number>({
		get: () => props.getConfigNumberValue("kwWarnLimit"),
		set: (value) => {
			if (value > kwErrorLimit.value) {
				kwErrorLimit.value = value;
			}
			void props.setConfigValue("kwWarnLimit", value);
		},
	});

	const kwErrorLimit = computed<number>({
		get: () => props.getConfigNumberValue("kwErrorLimit"),
		set: (value) => {
			if (value < kwWarnLimit.value) {
				kwWarnLimit.value = value;
			}
			void props.setConfigValue("kwErrorLimit", value);
		},
	});

	function setCurrentAction(newAction: string, open: boolean) {
		if (newAction === oldAction.value.name && !open) {
			return;
		}
		oldAction.value.name = currentAction.value;
		oldAction.value.open = currentAction.value !== "";
		if (open) {
			currentAction.value = newAction;
		} else {
			currentAction.value = "";
		}
	}

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	const colsVorgaben: DataTableColumn[] = [
		{ key: 'idFach', label: 'Fach', span: 1.25, sortable: true },
		{ key: 'kursart', label: 'Kursart', span: 0.5, sortable: true },
		{ key: 'quartal', label: 'Quartal', span: 0.1, align: 'center' },
	];

	const colsKursklausuren: DataTableColumn[] = [
		{ key: 'kurs', label: 'Kurs', span: 1.25, sortable: true },
		{ key: 'lehrer', label: 'Lehrer', span: 0.25, sortable: true },
		{ key: 'quartal', label: 'Quartal', span: 0.1, align: 'center' },
	];

	const colsSchuelerklausuren: DataTableColumn[] = [
		{ key: 'name', label: 'Name', span: 1.5, sortable: true },
		{ key: 'kurs', label: 'Kurs', span: 0.4, sortable: true },
		{ key: 'quartal', label: 'Quartal', span: 0.1, align: 'center' },
	];

	const colsTermine: DataTableColumn[] = [
		{ key: 'kurse', label: 'Titel', span: 1.25, sortable: true },
		{ key: 'quartal', label: 'Quartal', span: 0.1, align: 'center' },
		{ key: 'datum', type: 'date', label: 'Datum', span: 0.3, align: 'center' },
	];

	const colsKwKonflikte: DataTableColumn[] = [
		{ key: 'kw', label: 'KW', span: 0.25, sortable: true },
		{ key: 'schueler', label: 'Schüler', span: 0.75, sortable: true },
		{ key: 'klausuren', label: 'Klausuren', sortable: true },
	];

	function addStatusColumn(columns: DataTableColumn[], label: string = 'Korrektur', span: number = 0.2): DataTableColumn[] {
		const newColumns = Array.from(columns);
		newColumns.push({ key: 'status', label, span, align: 'center' });
		return newColumns;
	}

	function getBgColor(input: string | number | null | GostKursklausur | GostSchuelerklausur | GostSchuelerklausurTermin): string {
		const kuerzel = resolveFachkuerzel(input);
		if (kuerzel === null) {
			return "rgb(220,220,220)";
		}
		return Fach.getBySchluesselOrDefault(kuerzel).getHMTLFarbeRGBA(props.jahrgangsdaten!.abiturjahr - 1, 1);
	}

	function resolveFachkuerzel(input: string | number | null | GostKursklausur | GostSchuelerklausur | GostSchuelerklausurTermin): string | null {
		if (input === null) {
			return null;
		}
		if (typeof input === "string") {
			return input;
		}
		if (typeof input === "number") {
			const fach = props.kMan().getFaecherManager(props.jahrgangsdaten!.abiturjahr).get(input);
			return fach?.kuerzel ?? null;
		}
		let vorgabe;
		if (input instanceof GostSchuelerklausur) {
			vorgabe = props.kMan().vorgabeBySchuelerklausur(input);
		} else if (input instanceof GostSchuelerklausurTermin) {
			vorgabe = props.kMan().vorgabeBySchuelerklausurTermin(input);
		} else {
			vorgabe = props.kMan().vorgabeByKursklausur(input);
		}
		const fach = props.kMan().getFaecherManager(props.jahrgangsdaten!.abiturjahr).get(vorgabe.idFach);
		return fach?.kuerzel ?? null;
	}

	type KursBadge = { text: string; farbe: string | null };
	const terminBezeichnung = (termin: GostKlausurtermin): KursBadge[] => {
		const wrap = (text: string): KursBadge => ({ text, farbe: null });
		if (termin.bezeichnung !== null && termin.bezeichnung.length > 0) {
			return [wrap(termin.bezeichnung)];
		}
		if (!termin.istHaupttermin) {
			return [wrap("Nachschreibtermin")];
		}
		const menge = props.kMan().kursklausurGetMengeByTermin(termin);
		if (menge.size() > 0) {
			return [...menge].map(k => ({
				text: props.kMan().kursKurzbezeichnungByKursklausur(k),
				farbe: getBgColor(k),
			}));
		}
		return [wrap("Leerer Klausurtermin")];
	};

	const modalVorgaben = ref<boolean>(false);
	const modalError = ref<string | undefined>(undefined);

	async function erzeugeKursklausurenAusVorgabenOrModal() {
		try {
			await props.erzeugeKursklausurenAusVorgaben(props.quartalsauswahl.value);
		} catch (err) {
			if (err instanceof OpenApiError) {
				modalError.value = await err.response?.text();
				modalVorgaben.value = true;
			} else {
				throw err;
			}
		}
	}

</script>

