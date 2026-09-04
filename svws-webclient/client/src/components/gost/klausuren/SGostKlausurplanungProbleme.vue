<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl />
	</Teleport>
	<div class="page page-flex-col min-w-128 max-w-256">
		<s-gost-klausurplanung-problem-card problem-id="stundenplan_fehlend" v-model:current-action="currentAction"
			:show="(state.abschnitt !== undefined) && !state.manager.stundenplanManagerGeladenAndExistsByAbschnitt(state.abschnitt.id)" icon="i-ri-calendar-event-line" :fehler="ValidatorFehlerart.MUSS"
			title="Kein Stundenplan" subtitle="Es existiert kein Stundenplan für diesen Schuljahresabschnitt.">
			<p>Zur Terminierung von Klausurschienen und Raumplanung muss zwingend ein Stundenplan definiert sein.</p>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Stundenplandefinition" @click="gotoStundenplan" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Stundenplandefinition
				</svws-ui-button>
			</template>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="vorgaben_fehlend" v-model:current-action="currentAction" :show="!vorgabenAlle().isEmpty()" icon="i-ri-draft-line"
			title="Fehlende Klausurvorgaben" :fehler="vorgaben().size() > 0 ? ValidatorFehlerart.MUSS : ValidatorFehlerart.UNGENUTZT"
			:can-open="!vorgaben().isEmpty()" :collapsible="!vorgaben().isEmpty()">
			<template #subtitle>
				<span>{{ vorgaben().size() }} fehlende Klausurvorgabe{{ vorgaben().size() === 1 ? "" : "n" }} gefunden.
					<template v-if="state.halbjahr.istEinfuehrungsphase()">
						<span v-if="vorgabenAnzahlAusgeblendet() > 0" class="inline-flex items-center italic"> ( {{ vorgabenAnzahlAusgeblendet() }} ausgeblendet
							<span @click.stop>
								<svws-ui-button @click="ignoreVorgabenToggle = !ignoreVorgabenToggle; currentAction = 'vorgaben_fehlend'" type="icon" :title="'Ignorierte Vorgaben ' + (ignoreVorgabenToggle ? 'anzeigen' : 'ausblenden')">
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
					<span class="svws-ui-badge" :title="fachFehltText(value)" :style="`color: var(--color-text-uistatic); background-color: ${getBgColor(value)}`">{{ fachBezeichnungById(value) }}</span>
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
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="schuelerklausurmenge_abweichend" v-model:current-action="currentAction" :show="!schuelerklausuren().isEmpty()"
			icon="i-ri-group-line" title="Abweichende Schülerklausurmenge" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="schuelerklausuren().size() + ' Abweichung' + (schuelerklausuren().size() === 1 ? '' : 'en') + ' gefunden.'">
			<svws-ui-table :items="schuelerklausuren()" :columns="addStatusColumn(colsSchuelerklausuren)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button v-if="rowData.id === -1" type="icon" @click="async () => {
						if (isAwaiting) return;
						isAwaiting = true;
						try {
							await state.erzeugeSchuelerklausuren(ListUtils.create1(rowData));
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
							await state.loescheSchuelerklausuren(ListUtils.create1(rowData));
						} finally {
							isAwaiting = false;
						}
					}" :disabled="isAwaiting">
						<span class="icon i-ri-delete-bin-line" />
					</svws-ui-button>
				</template>
				<template #cell(name)="{ rowData }">
					{{ presenter.schuelerNameBySchuelerklausur(rowData) }}
				</template>
				<template #cell(kurs)="{ rowData }">
					<s-gost-klausurplanung-kurs-badge :kursklausur="state.manager.kursklausurBySchuelerklausur(rowData)" :tooltip="false" :show-bemerkungen="false" />
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ state.manager.vorgabeBySchuelerklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="kursklausuren_fehlend" v-model:current-action="currentAction" :show="!kursklausuren().isEmpty()"
			icon="i-ri-book-2-line" title="Abweichende Kursklausurmenge" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="kursklausuren().size() + ' Abweichung' + (kursklausuren().size() === 1 ? '' : 'en') + ' gefunden.'">
			<svws-ui-table :items="kursklausuren()" :columns="(kursklausuren().toArray() as GostKursklausur[]).some(kk => kk.id !== -1) ? addStatusColumn(colsKursklausuren) : colsKursklausuren">
				<template #cell(status)="{ rowData }">
					<svws-ui-button v-if="rowData.id !== -1" type="transparent" @click="async () => {
						if (isAwaiting) return;
						isAwaiting = true;
						try {
							await state.loescheKursklausuren(ListUtils.create1(rowData));
						} finally {
							isAwaiting = false;
						}
					}" title="löschen" :disabled="isAwaiting">
						<span class="icon i-ri-delete-bin-line" /> löschen
					</svws-ui-button>
				</template>
				<template #cell(kurs)="{ rowData }">
					<s-gost-klausurplanung-kurs-badge :kursklausur="rowData" :tooltip="false" :show-bemerkungen="false" />
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ state.manager.kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ state.manager.vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button v-if="!(kursklausuren().toArray() as GostKursklausur[]).every(kk => kk.id !== -1)" title="Fehlende Kursklausuren erstellen" @click="erzeugeKursklausurenAusVorgabenOrModal" class="mt-2">
					<span class="icon i-ri-play-line" />
					Fehlende Kursklausuren erstellen
				</svws-ui-button>
			</template>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="kursklausuren_nicht_verteilt" v-model:current-action="currentAction" :show="!kursklausurenNichtVerteilt().isEmpty()"
			icon="i-ri-book-2-line" title="Nicht verteilte Kursklausuren" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="kursklausurenNichtVerteilt().size() + ' nicht verteilte Kursklausur' + (kursklausurenNichtVerteilt().size() === 1 ? '' : 'en') + ' gefunden.'">
			<svws-ui-table :items="kursklausurenNichtVerteilt()" :columns="colsKursklausuren">
				<template #cell(kurs)="{ rowData }">
					<s-gost-klausurplanung-kurs-badge :kursklausur="rowData" :tooltip="false" :show-bemerkungen="false" />
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ state.manager.kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ state.manager.vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Schienenansicht" @click="() => gotoSchienen(undefined)" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Schienenansicht
				</svws-ui-button>
			</template>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="termine_ohne_stundenplan" v-model:current-action="currentAction" :show="!termineOhneStundenplan().isEmpty()"
			icon="i-ri-calendar-event-line" title="Klausurtermine ohne gültigen Stundenplan" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="termineOhneStundenplan().size() + ' Klausurtermin' + (termineOhneStundenplan().size() === 1 ? '' : 'e') + ' ohne gültigen Stundenplan gefunden.'">
			<svws-ui-table :items="termineOhneStundenplan()" :columns="colsTermine">
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<s-gost-klausurplanung-kurs-badge v-for="klausur in kursklausurenByTermin(rowData)" :key="klausur.id" class="mr-1"
							:kursklausur="klausur" :termin="rowData" :tooltip="false" :show-bemerkungen="false" />
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
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="klausurtermine_mit_schuelerkonflikten" v-model:current-action="currentAction" :show="!termineMitKonflikten().isEmpty()"
			icon="i-ri-alert-line" title="Klausurtermine mit Schülerkonflikten" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="termineMitKonflikten().size() + ' Klausurtermin' + (termineMitKonflikten().size() === 1 ? '' : 'e') + ' mit Schülerkonflikten gefunden.'">
			<svws-ui-table :items="termineMitKonflikten()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent" @click="gotoSchienen(rowData)"
						title="Schiene anzeigen" size="small">
						<span class="icon i-ri-link" /> anzeigen
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<s-gost-klausurplanung-kurs-badge v-for="klausur in kursklausurenByTermin(rowData)" :key="klausur.id" class="mr-1"
							:kursklausur="klausur" :termin="rowData" :tooltip="false" :show-bemerkungen="false" />
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="termine_ohne_datum" v-model:current-action="currentAction" :show="!termineOhneDatum().isEmpty()"
			icon="i-ri-calendar-event-line" title="Klausurtermine ohne Datum" :fehler="ValidatorFehlerart.KANN"
			:subtitle="termineOhneDatum().size() + ' Klausurtermin' + (termineOhneDatum().size() === 1 ? '' : 'e') + ' ohne Datum gefunden.'">
			<svws-ui-table :items="termineOhneDatum()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent" @click="gotoKalenderdatum(undefined, rowData)"
						title="Datum setzen" size="small"
						:disabled="(state.abschnitt === undefined) || !state.manager.stundenplanManagerExistsByAbschnitt(state.abschnitt.id)">
						<span class="icon i-ri-link" /> datieren
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<s-gost-klausurplanung-kurs-badge v-for="klausur in kursklausurenByTermin(rowData)" :key="klausur.id" class="mr-1"
							:kursklausur="klausur" :termin="rowData" :tooltip="false" :show-bemerkungen="false" />
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="termine_ohne_raumplanung" v-model:current-action="currentAction" :show="!termineUnvollstaendigeRaumzuweisung().isEmpty()"
			icon="i-ri-team-line" title="Klausurtermine mit unvollständiger Raumplanung" :fehler="ValidatorFehlerart.KANN"
			:subtitle="termineUnvollstaendigeRaumzuweisung().size() + ' Klausurtermin' + (termineUnvollstaendigeRaumzuweisung().size() === 1 ? '' : 'e') + ' mit unvollständiger Raumplanung gefunden.'">
			<svws-ui-table :items="termineUnvollstaendigeRaumzuweisung()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoRaumzeitTermin(rowData.abiturjahrgang, GostHalbjahr.fromIDorException(rowData.halbjahr), rowData.id)"
						title="Räume planen" size="small">
						<span class="icon i-ri-link" /> Raumplan
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<s-gost-klausurplanung-kurs-badge v-for="klausur in kursklausurenByTermin(rowData)" :key="klausur.id" class="mr-1"
							:kursklausur="klausur" :termin="rowData" :tooltip="false" :show-bemerkungen="false" />
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="termine_raumkapazität" v-model:current-action="currentAction"
			:show="termineOhneStundenplan().isEmpty() && !raumkapazitaetUeberschritten().isEmpty()" icon="i-ri-team-line" title="Raumkapazität überschritten" :fehler="ValidatorFehlerart.KANN"
			:subtitle="raumkapazitaetUeberschritten().size() + ' Klausurtermin' + (raumkapazitaetUeberschritten().size() === 1 ? '' : 'e') + ' mit überschrittener Raumkapazität gefunden.'">
			<svws-ui-table :items="raumkapazitaetUeberschritten()" :columns="addStatusColumn(colsTermine, 'Gehe zu')">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoRaumzeitTermin(rowData.abiturjahrgang, GostHalbjahr.fromIDorException(rowData.halbjahr), rowData.id)"
						title="Räume planen" size="small">
						<span class="icon i-ri-link" /> Raumplan
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					<div class="flex flex-wrap">
						<s-gost-klausurplanung-kurs-badge v-for="klausur in kursklausurenByTermin(rowData)" :key="klausur.id" class="mr-1"
							:kursklausur="klausur" :termin="rowData" :tooltip="false" :show-bemerkungen="false" />
					</div>
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="nachschreibklausuren_nicht_zugewiesen" v-model:current-action="currentAction"
			:show="!nachschreibklausurenNichtZugewiesen().isEmpty()" icon="i-ri-spam-3-line" title="Nicht zugewiesene Nachschreibklausuren" :fehler="ValidatorFehlerart.KANN"
			:subtitle="nachschreibklausurenNichtZugewiesen().size() + ' nicht zugewiesene Nachschreibklausur' + (nachschreibklausurenNichtZugewiesen().size() === 1 ? '' : 'en') + ' gefunden.'">
			<svws-ui-table :items="nachschreibklausurenNichtZugewiesen()" :columns="colsSchuelerklausuren">
				<template #cell(name)="{ rowData }">
					<span>{{ presenter.schuelerNameBySchuelerklausurtermin(rowData) }}</span>
				</template>
				<template #cell(kurs)="{ rowData }">
					<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="rowData" :tooltip="false" :show-bemerkungen="false" />
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ state.manager.vorgabeBySchuelerklausurtermin(rowData).quartal }}
				</template>
			</svws-ui-table>

			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Nachschreiberansicht"
					@click="gotoNachschreiber(state.jahrgangsdaten.abiturjahr, state.halbjahr)"
					class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Nachschreiberansicht
				</svws-ui-button>
			</template>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="konflikt_drei_wochenklausuren" v-model:current-action="currentAction" icon="i-ri-alert-line"
			:collapsible="!klausurenProKwWarning().isEmpty()" :can-open="!klausurenProKwWarning().isEmpty()" :fehler="ValidatorFehlerart.KANN">
			<template #title>
				<div class="ui-card--header--title flex items-center gap-3">
					Warnung für Schüler mit
					<div class="w-20 shrink-0" @click.stop>
						<svws-ui-input-number class="w-full" headless :model-value="state.kwWarnLimit" @update:model-value="state.setKwWarnLimit" :min="2" :max="5" />
					</div>
					oder mehr Klausuren in einer Woche
				</div>
			</template>
			<template #subtitle>
				<div class="flex">
					<span>{{ klausurenProKwWarning().size() === 0 ? 'Keine' : klausurenProKwWarning().size() }} Schüler mit {{ state.kwWarnLimit }}&nbsp;</span>
					<span v-if="state.kwErrorLimit - 1 > state.kwWarnLimit">bis {{ state.kwErrorLimit - 1 }}&nbsp;</span>
					<span>Klausuren in einer Woche gefunden.</span>
				</div>
			</template>
			<svws-ui-table :items="klausurenProKwWarning()" :columns="colsKwKonflikte">
				<template #cell(kw)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoKalenderdatum(state.manager.terminOrExceptionBySchuelerklausurtermin(rowData.b.getFirst()!).datum!, undefined)"
						title="Springe zu Kalenderwoche" size="small">
						<span class="icon i-ri-link" /> {{ rowData.a.a }}
					</svws-ui-button>
				</template>
				<template #cell(schueler)="{ rowData }">
					{{ schuelerName(rowData.a.b) }}
				</template>
				<template #cell(klausuren)="{rowData}">
					<div class="grid grid-cols-4 gap-x-1 gap-y-2">
						<span v-for="klausur in rowData.b" :key="klausur.id" class="flex flex-col items-center">
							<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="klausur" :tooltip="false" :show-bemerkungen="false" />
							<span class="text-sm font-medium">{{ datumText(klausur) }}</span>
						</span>
					</div>
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>

		<s-gost-klausurplanung-problem-card problem-id="konflikt_vier_wochenklausuren" v-model:current-action="currentAction" icon="i-ri-alert-fill"
			:collapsible="!klausurenProKwError().isEmpty()" :can-open="!klausurenProKwError().isEmpty()" :fehler="ValidatorFehlerart.MUSS"
			:subtitle="(klausurenProKwError().size() === 0 ? 'Keine' : klausurenProKwError().size()) + ' Schüler mit ' + state.kwErrorLimit + ' oder mehr Klausuren in einer Woche gefunden.'">
			<template #title>
				<div class="ui-card--header--title flex items-center gap-3">
					Fehler für Schüler mit
					<div class="w-20 shrink-0" @click.stop>
						<svws-ui-input-number class="w-full" headless :model-value="state.kwErrorLimit" @update:model-value="state.setKwErrorLimit" :min="3" :max="5" />
					</div>
					oder mehr Klausuren in einer Woche
				</div>
			</template>
			<svws-ui-table :items="klausurenProKwError()" :columns="colsKwKonflikte">
				<template #cell(kw)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoKalenderdatum(state.manager.terminOrExceptionBySchuelerklausurtermin(rowData.b.getFirst()!).datum!, undefined)"
						title="Springe zu Kalenderwoche" size="small">
						<span class="icon i-ri-link" /> {{ rowData.a.a }}
					</svws-ui-button>
				</template>
				<template #cell(schueler)="{ rowData }">
					<span>
						{{ schuelerName(rowData.a.b) }}
					</span>
				</template>
				<template #cell(klausuren)="{rowData}">
					<div class="grid grid-cols-4 gap-x-1 gap-y-2">
						<span v-for="klausur in rowData.b" :key="klausur.id" class="flex flex-col items-center">
							<s-gost-klausurplanung-kurs-badge :schuelerklausurtermin="klausur" :tooltip="false" :show-bemerkungen="false" />
							<span class="text-sm font-medium">{{ datumText(klausur) }}</span>
						</span>
					</div>
				</template>
			</svws-ui-table>
		</s-gost-klausurplanung-problem-card>
	</div>

	<s-gost-klausurplanung-modal v-model:show="modalVorgaben" :text="modalError" :jump-to="gotoVorgaben" jump-to-text="Zu den Klausurvorgaben" abbrechen-text="OK" />
</template>

<script setup lang="ts">
	import { ref, onMounted } from 'vue';
	import { SGostKlausurplanungVorgabenIgnoreManager } from "~/components/gost/klausuren/SGostKlausurplanungVorgabenIgnoreManager";
	import SGostKlausurplanungProblemCard from "./SGostKlausurplanungProblemCard.vue";
	import { useKlausurplanungPresenter } from "./SGostKlausurplanungPresenter";
	import { OpenApiError } from '@core/api/OpenApiError.js';
	import type { GostKlausurtermin } from '@core/core/data/gost/klausuren/GostKlausurtermin.js';
	import type { GostKursklausur } from '@core/core/data/gost/klausuren/GostKursklausur.js';
	import type { GostSchuelerklausurtermin } from '@core/core/data/gost/klausuren/GostSchuelerklausurtermin.js';
	import type { SchuelerListeEintrag } from '@core/core/data/schueler/SchuelerListeEintrag.js';
	import { GostHalbjahr } from '@core/core/types/gost/GostHalbjahr.js';
	import { DateUtils } from '@core/core/utils/DateUtils.js';
	import { useConfigState } from '@ui/states/ConfigState.js';
	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState.js';
	import type { DataTableColumn } from '@ui/types.js';
	import { ValidatorFehlerart } from '@core/asd/validate/ValidatorFehlerart.js';
	import { ListUtils } from '@core/core/utils/ListUtils.js';

	type KlausurplanungProblemId =
		| "stundenplan_fehlend"
		| "vorgaben_fehlend"
		| "schuelerklausurmenge_abweichend"
		| "kursklausuren_fehlend"
		| "kursklausuren_nicht_verteilt"
		| "termine_ohne_stundenplan"
		| "klausurtermine_mit_schuelerkonflikten"
		| "termine_ohne_datum"
		| "termine_ohne_raumplanung"
		| "termine_raumkapazität"
		| "nachschreibklausuren_nicht_zugewiesen"
		| "konflikt_drei_wochenklausuren"
		| "konflikt_vier_wochenklausuren";

	const { gotoKalenderdatum, gotoNachschreiber, gotoRaumzeitTermin, gotoSchienen, gotoStundenplan, gotoVorgaben } = defineProps<{
		gotoKalenderdatum: (datum: string | undefined, termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoNachschreiber: (abiturjahr: number, halbjahr: GostHalbjahr) => Promise<void>;
		gotoRaumzeitTermin: (abiturjahr: number, halbjahr: GostHalbjahr, idtermin: number | undefined) => Promise<void>;
		gotoSchienen: (termin: GostKlausurtermin | undefined) => Promise<void>;
		gotoStundenplan: () => Promise<void>;
		gotoVorgaben: () => Promise<void>;
	}>();
	const state = useGostKlausurplanungState();
	const configState = useConfigState();
	const presenter = useKlausurplanungPresenter(state);

	const vorgabenIgnoreManager = new SGostKlausurplanungVorgabenIgnoreManager(
		(key, fromJSON) => configState.config.getObjectValue(key, fromJSON),
		(key, value, toJSON) => configState.config.setObjectValue(key, value, toJSON)
	);

	const ignoreVorgabenToggle = ref<boolean>(true);

	const vorgaben = () => state.manager.vorgabefehlendGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, (ignoreVorgabenToggle.value ? vorgabenIgnoreManager.getAll() : null));
	const vorgabenAlle = () => state.manager.vorgabefehlendGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, null);
	const vorgabenAnzahlAusgeblendet = () => vorgabenIgnoreManager.countContained(vorgabenAlle());
	const kursklausuren = () => state.manager.kursklausurfehlendGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const kursklausurenNichtVerteilt = () => state.manager.kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const schuelerklausuren = () => state.manager.schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const termineOhneDatum = () => state.manager.terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const termineOhneStundenplan = () => state.manager.terminOhneStundenplanGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const termineMitKonflikten = () => state.manager.terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const termineUnvollstaendigeRaumzuweisung = () => state.manager.terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const raumkapazitaetUeberschritten = () => state.manager.terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const nachschreibklausurenNichtZugewiesen = () => state.manager.schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal);
	const klausurenProKwWarning = () => state.manager.klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, state.kwWarnLimit, state.kwErrorLimit);
	const klausurenProKwError = () => state.manager.klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, state.kwErrorLimit, -1);

	const currentAction = ref<KlausurplanungProblemId | "">('');
	const isAwaiting = ref(false);

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

	function getBgColor(idFach: number): string {
		const fach = state.manager.getFaecherManager(state.jahrgangsdaten.abiturjahr).get(idFach);
		const kuerzel = fach?.kuerzel ?? null;
		return presenter.fachFarbeByKuerzel(kuerzel);
	}

	function fachBezeichnungById(idFach: number): string {
		return state.manager.getFaecherManager(state.jahrgangsdaten.abiturjahr).get(idFach)?.bezeichnung ?? `Fach-ID ${idFach}`;
	}

	function fachFehltText(idFach: number): string | undefined {
		return state.manager.getFaecherManager(state.jahrgangsdaten.abiturjahr).get(idFach) === null ? `Fach mit ID ${idFach} ist nicht als Fach der Oberstufe definiert.` : undefined;
	}

	const kursklausurenByTermin = (termin: GostKlausurtermin) => state.manager.kursklausurGetMengeByTermin(termin);

	function datumText(klausur: GostSchuelerklausurtermin): string {
		return DateUtils.gibDatumGermanFormat(state.manager.terminOrExceptionBySchuelerklausurtermin(klausur).datum!).slice(0, 6);
	}

	function schuelerName(schueler: SchuelerListeEintrag): string {
		return `${schueler.nachname}, ${schueler.vorname}`;
	}

	const modalVorgaben = ref<boolean>(false);
	const modalError = ref<string | undefined>(undefined);

	async function erzeugeKursklausurenAusVorgabenOrModal() {
		try {
			await state.erzeugeKursklausurenAusVorgaben(state.quartal);
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
